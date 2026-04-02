package com.zeroone.star.supportinfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment.AnnexQuery;
import com.zeroone.star.supportinfo.entity.Annex;
import com.zeroone.star.supportinfo.mapper.AnnexMapper;
import com.zeroone.star.supportinfo.service.IAnnexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.PageDTO;

import java.io.File;
import io.github.bluemiaomiao.service.FastdfsClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 附件 服务实现类
 * </p>
 *
 * @author kai
 * @since 2025-10-23
 */
@Service
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex> implements IAnnexService {
    @Resource
    private FastdfsClientService dfs;

    @Resource
    private MsAnnexMapper msAnnexMapper;
    @Resource
    private AnnexMapper annexMapper;
    @Override
    public PageDTO<AnnexDTO> listByPage(AnnexQuery query) {
        QueryWrapper<Annex> wrapper = new QueryWrapper<>();
        wrapper.like(query.getFileType()!=null && !query.getFileType().isEmpty(),"file_type", query.getFileType());
        wrapper.like(query.getSaveType()!=null && !query.getSaveType().isEmpty(),"save_type", query.getSaveType());
        wrapper.like(query.getName()!=null && !query.getName().isEmpty(),"name", query.getName());
        wrapper.eq(query.getSavePath()!=null && !query.getSavePath().isEmpty(),"save_path", query.getSavePath());
        wrapper.eq(query.getStatus()!=null, "status", query.getStatus());
        Page<Annex> page=new Page<>(query.getPageIndex(), query.getPageSize());
        Page<Annex> entityPage=page(page, wrapper);
        //实体类转换为DTO
        List<AnnexDTO> dtoList= BeanUtil.copyToList(entityPage.getRecords(), AnnexDTO.class);
        //封装pageDTO
        Page<AnnexDTO> dto = new Page<>(query.getPageIndex(), query.getPageSize());
        dto.setTotal(entityPage.getTotal());
        dto.setRecords(dtoList);
        return PageDTO.create(dto);
    }


    @Override
    public void saveAttachment(MultipartFile file, FastDfsFileInfo info, String fileServerUrl) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        // 获取文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }
        Annex annex = new Annex();
        annex.setName(originalFilename);
        //存储后缀名
        annex.setFileType(suffix);
        annex.setSavePath(info.getGroup() + "/" + info.getStorageId());
        annex.setSaveType("FastDFS");
        annex.setStatus(1);
        // 保存到数据库
        save(annex);
    }





    @Override
    public boolean update(AnnexDTO dto) throws Exception {

        //修改附件逻辑本质是修改数据库的信息，文件的内容不会改变
        try {
            // 参数校验--id不为空
            if (dto == null || dto.getId() == null) {
                throw new Exception("附件ID不能为空");
            }
            // 查询原附件
            Annex oldAnnex = annexMapper.selectById(dto.getId());
            if (oldAnnex == null) {
                throw new Exception("附件不存在");
            }
            // 校验附件名称唯一性（排除当前附件自身）
            if (dto.getName() != null && !dto.getName().equals(oldAnnex.getName())) {
                // 查询是否存在相同名称的其他附件
                LambdaQueryWrapper<Annex> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Annex::getName, dto.getName())
                        .ne(Annex::getId, dto.getId()); // 排除自身
                Long count = annexMapper.selectCount(queryWrapper);
                if (count > 0) {
                    throw new Exception("附件名称已存在，请修改名称");
                }
            }

            // 文件路径校验
            if (dto.getSavePath() == null || dto.getSavePath().trim().isEmpty()) {
                throw new Exception("文件保存路径不能为空");
            }
            //去掉再次的文件校验，id存在那么数据库的save_path就是正确的
            Annex annex=msAnnexMapper.annexDtoToAnnex(dto);
            int res=baseMapper.updateById(annex);

            return  res>0;
        } catch (Exception e) {
            throw new Exception("更新附件失败: " + e.getMessage());
        }
    }

    /**
     * 附件修改详细
     * @param dto  附件数据传输对象
     * @param oldAnnex 原附件对象
     * @return 新的附件对象
     * @throws Exception
     */
    private Annex newAnnexDetail(AnnexDTO dto, Annex oldAnnex) throws Exception{
        Annex newAnnex = new Annex();
        newAnnex.setId(oldAnnex.getId());
        newAnnex.setName(dto.getName());
        newAnnex.setFileType(dto.getFileType());
        newAnnex.setSaveType(dto.getSaveType());
        newAnnex.setSavePath(dto.getSavePath());
        newAnnex.setRemark(dto.getRemark());
        newAnnex.setStatus(dto.getStatus());
        if (newAnnex.getSavePath() == null || newAnnex.getSavePath().trim().isEmpty()) {
            throw new Exception("文件保存路径不能为空");
        }
        return newAnnex;
    }

}
