package com.zeroone.star.sysconfig.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateUpdateDTO;
import com.zeroone.star.project.query.j1.sysconfig.TempImportQuery;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportDownloadVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportVO;
import com.zeroone.star.sysconfig.entity.TmplImport;
import com.zeroone.star.sysconfig.enums.TemplateStatus;
import com.zeroone.star.sysconfig.mapper.TmplImportMapper;
import com.zeroone.star.sysconfig.service.ITmplImportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 导入模板 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
@Transactional
public class TmplImportServiceImpl extends ServiceImpl<TmplImportMapper, TmplImport> implements ITmplImportService {
    @Autowired
    TmplImportMapper tmplImportMapper;
    @Autowired
    FastDfsClientComponent fastDfsClientComponent;
    @Value("${fastdfs.nginx-servers}")
    private String fileServerUrl;
    @Value("${fastdfs.http-secret-key}")
    private String httpSecretKey;

    /*
    * 导入模板
    * @param templateAddDTO 模板信息
    * @author cwznb
    * @since 2025-10-29
    */
    @Override
    public int importTemplate(TemplateAddDTO templateAddDTO)  {
        MultipartFile file=templateAddDTO.getFile();
        TmplImport tmplImport=new TmplImport();

            try {
                String ext=getExt(file.getOriginalFilename());
                FastDfsFileInfo info=fastDfsClientComponent.uploadFile(file.getBytes(),ext);
                String path=info.getGroup()+","+info.getStorageId();
                tmplImport.setSavePath(path);

                String name=templateAddDTO.getTemplateName();
                tmplImport.setName(name==null?"":name);

                tmplImport.setRemark(templateAddDTO.getRemark());

                tmplImport.setStatus(TemplateStatus.DISABLE.getValue());

                tmplImport.setId(UUID.randomUUID().toString().replace("-", ""));

                tmplImport.setSaveType(ext);

                tmplImport.setCode(UUID.randomUUID().toString());

            }catch (Exception e){
                e.printStackTrace();
            }

            return tmplImportMapper.insert(tmplImport);

    }
    /*
     * 删除模板
     * @param templateAddDTO 模板信息
     * @author cwznb
     * @since 2025-10-29
     */

    @Override
    public int deleteTemplate(String id) {
        TmplImport tmplImport=tmplImportMapper.selectById(id);
        if(tmplImport==null){
            return 0;
        }

        try{
            String group=tmplImport.getSavePath().split(",")[0];
            String storageId=tmplImport.getSavePath().split(",")[1];
            FastDfsFileInfo info=FastDfsFileInfo.builder().group(group).storageId(storageId).build();
            fastDfsClientComponent.deleteFile(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tmplImportMapper.deleteById(id);
    }
    /*
     * 更新模板
     * @param templateAddDTO 模板信息
     * @author cwznb
     * @since 2025-10-29
     */

    @Override
    public int updateTemplate(TemplateUpdateDTO templateUpdateDTO) {
        TmplImport tmplImport=tmplImportMapper.selectById(templateUpdateDTO.getId());
        if(tmplImport==null){
            return 0;
        }
        String name=templateUpdateDTO.getTemplateName();
        if(name!=null&&!name.equals("")){
            tmplImport.setName(name);
        }
        String remark=templateUpdateDTO.getRemark();
        if(remark!=null&&!remark.equals("")){
            tmplImport.setRemark(remark);
        }
        int status=templateUpdateDTO.getStatus();
        tmplImport.setStatus(status);
        MultipartFile file=templateUpdateDTO.getFile();
        if(file!=null){
            try{
                FastDfsFileInfo info=FastDfsFileInfo.builder()
                        .group(tmplImport.getSavePath().split(",")[0])
                        .storageId(tmplImport.getSavePath().split(",")[1])
                        .build();
                //先删除旧的fastdfs里的文件
                fastDfsClientComponent.deleteFile(info);
                
               FastDfsFileInfo newinfo= fastDfsClientComponent.uploadFile(file.getBytes(),getExt(file.getOriginalFilename()));
               String path=newinfo.getGroup()+","+newinfo.getStorageId();

               tmplImport.setSavePath(path);
               tmplImport.setSaveType(file.getContentType());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return tmplImportMapper.updateById(tmplImport);
    }

    //获取文件名扩展名
    private String getExt(String name){
        int index=name.lastIndexOf(".");
        if(index==-1){
            return "";
        }
        return name.substring(index+1);
    }

    /*
     * 获取模板列表（条件+分页）
     * @param Query
     * @author nuochezi2cc
     * @return
     */
    @Override
    public PageDTO<TempImportVO> queryTemplateList(TempImportQuery query){
        Page<TmplImport> mpPage = new Page<>(query.getPageIndex(), query.getPageSize());

        QueryWrapper<TmplImport> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getTemplateName()), "name", query.getTemplateName());
        wrapper.eq(StringUtils.isNotBlank(query.getTemplateCode()), "code", query.getTemplateCode());
        wrapper.eq(StringUtils.isNotBlank(query.getSaveType()), "save_type", query.getSaveType());
        wrapper.eq(query.getStatus() != null, "status", query.getStatus());
        wrapper.orderByDesc("id");
        Page<TmplImport> pageResult = tmplImportMapper.selectPage(mpPage, wrapper);

        return PageDTO.create(pageResult, this::entityToVO);
    }

    /*
     * 字段映射
     * @param entity
     * @author nuochezi2cc
     */
    private TempImportVO entityToVO(TmplImport entity) {
        TempImportVO vo = new TempImportVO();
        vo.setId(entity.getId());
        vo.setTemplateName(entity.getName());
        vo.setTemplateCode(entity.getCode());
        vo.setSaveType(entity.getSaveType());
        vo.setRemark(entity.getRemark());
        vo.setStatus(entity.getStatus());
        return vo;
    }

    /*
     * 获取指定模板详情
     * @param id
     * @author nuochezi2cc
     * @return
     */
    @Override
    public TempImportDownloadVO getDownloadInfoById(String id) {
        TempImportDownloadVO vo = tmplImportMapper.selectDownloadInfoById(id);
        if (vo == null) {
            throw new RuntimeException("未找到模板，ID: " + id);
        }

        String savedPath = vo.getDownloadUrl();
        if (savedPath == null || !savedPath.contains(",")) {
            throw new RuntimeException("无效的文件存储路径: " + savedPath);
        }

        String[] parts = savedPath.split(",", 2);
        String group = parts[0];
        String storageId = parts[1];

        FastDfsFileInfo fileInfo = FastDfsFileInfo.builder()
                .group(group)
                .storageId(storageId)
                .build();

        boolean isToken = true;

        String downloadUrl = fastDfsClientComponent.fetchUrl(fileInfo, fileServerUrl, isToken);

        if (downloadUrl == null) {
            throw new RuntimeException("生成下载链接失败");
        }
        vo.setDownloadUrl(downloadUrl);

        return vo;
    }


}
