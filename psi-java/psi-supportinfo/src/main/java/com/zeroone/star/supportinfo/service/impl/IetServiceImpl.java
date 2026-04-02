package com.zeroone.star.supportinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.*;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetAddQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetUpdateQuery;
import com.zeroone.star.supportinfo.entity.Iet;
import com.zeroone.star.supportinfo.exception.ResourceNotFoundException;
import com.zeroone.star.supportinfo.mapper.IetMapper;
import com.zeroone.star.supportinfo.service.IIetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 收支类别 服务实现类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Service
public class IetServiceImpl extends ServiceImpl<IetMapper, Iet> implements IIetService {
    @Resource
    private IetMapper ietMapper;
    @Resource
    private MsIetMapper msIetMapper;

    /**
     * 按收支类别查询收支名称
     *
     * @param type
     * @return
     */
    @Override
    public List<IetNameDTO> selectIetName(Integer type) {
        QueryWrapper<Iet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type)
                .select("name")
                .select("id");
        return ietMapper.selectNameByType(queryWrapper);
    }

    /**
     * 查询收支类别列表
     *
     * @param query
     * @return
     */
    @Override
    public PageDTO<IetListDTO> listAll(IetQuery query) {
        //创建分页查询对象
        Page<Iet> page = new Page<>(query.getPageIndex(), query.getPageSize(), true);
        //创建查询条件对象
        //orderBy第一个参数condition表示是否拼接order by子句
        //orderBy第二个参数表示是否升序排序，true升序，false降序
        QueryWrapper<Iet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getType() != null, "type", query.getType())
                .like(!StringUtils.isEmpty(query.getName()), "name", query.getName())
                .orderBy(true, true, "id");
        //执行分页查询
        Page<Iet> ietPage = baseMapper.selectPage(page, queryWrapper);
        return PageDTO.create(ietPage, msIetMapper::IetToInandexListDTO);
    }

    /**
     * 获取收支类别详情
     *
     * @param id
     * @return
     */
    @Override
    public IetDetailDTO getInandexDetail(String id) {
        Iet iet = baseMapper.selectById(id);
        if (iet != null) {
            IetDetailDTO dto = msIetMapper.IetToInandexDetailDTO(iet);
            return dto;
        }
        return null;
    }

    @Override
    public String insertIet(IetAddDTO ietAddDto) {
        // 业务校验
        QueryWrapper<Iet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", ietAddDto.getName())
                .eq("type", ietAddDto.getType());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new IllegalArgumentException("该类型下已存在同名的收支类别");
        }

        Iet iet = new Iet();
        BeanUtils.copyProperties(ietAddDto, iet);
        this.save(iet);
        return iet.getId();
    }

    @Override
    public String  updateIet(IetUpdateDTO updateDTO) {
        Iet existingIet = this.getById(updateDTO.getId());
        if (existingIet == null) {
            throw new ResourceNotFoundException("更新失败：ID为" + updateDTO.getId() + "的收支类别不存在");
        }

        if (updateDTO.getName() != null) {
            QueryWrapper<Iet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", updateDTO.getName())
                    .eq("type", updateDTO.getType() != null ? updateDTO.getType() : existingIet.getType())
                    .ne("id", updateDTO.getId());
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new IllegalArgumentException("该类型下已存在同名的收支类别");
            }
        }

        Iet iet = new Iet();
        BeanUtils.copyProperties(updateDTO, iet);
        this.updateById(iet);
        return iet.getId();
    }
    @Override
    public String  deleteIet(String id) {
        // 阻挡空ID的删除请求
        Iet iet = super.getById(id);
        if (iet == null) {
            throw new ResourceNotFoundException("查询失败：ID为" + id + "的收支类别不存在");
        }
        this.removeById(id);
        return id;

    }
}
