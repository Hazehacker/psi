package com.zeroone.star.sysconfig.service;


import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeQury;

import com.zeroone.star.sysconfig.entity.DictType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典类型 服务类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
public interface IDictTypeService extends IService<DictType> {


    JsonVO<String> addDictType(DictTypeDTO dictTypeDTO);

    JsonVO<String> updateDictType(String id,DictTypeUpdateDTO updateDTO);

    PageDTO<DictTypeDTO> getDictTypeList(DictTypeQury dictTypeQury);

    List<DictTypeNameQuery> getDictTypeNameList();

    JsonVO<String> deleteDictType(String id);

}
