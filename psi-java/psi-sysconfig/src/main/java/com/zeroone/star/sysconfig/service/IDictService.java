package com.zeroone.star.sysconfig.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictNamesDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictQuery;
import com.zeroone.star.sysconfig.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IDictService extends IService<Dict> {
    /**
     *获取字典列表数据(条件+分页)
     * */
    public PageDTO<DictDTO> selectByCondition(DictQuery condition);

    /**
     * 获取字典名称列表
     * */
    public List<DictNamesDTO> selectByTcode(String typeCode);

    /**
     * 获取字典名称列表
     * */
    public List<DictNamesDTO> selectByTid(String typeId);

}
