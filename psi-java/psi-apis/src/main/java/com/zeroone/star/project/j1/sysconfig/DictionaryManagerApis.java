package com.zeroone.star.project.j1.sysconfig;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictAddDTO;

import com.zeroone.star.project.dto.j1.sysconfig.DictDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictNamesDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictUpdateDTO;
import com.zeroone.star.project.query.j1.sysconfig.DictNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeNameQuery;
import com.zeroone.star.project.query.j1.sysconfig.DictTypeQury;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;


import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeDeleteDTO;
import com.zeroone.star.project.dto.j1.sysconfig.DictTypeUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 描述：字典管理
 * </p>
 * @author fish、zhao0527
 * @version 1.0.0
 */
public interface DictionaryManagerApis {

    /**
     * 获取字典名称列表
     * @param dictNameQuery 字典名称查询对象
     * @return 查询结果
     */
    public JsonVO<List<DictNamesDTO>> getDictNameList(DictNameQuery dictNameQuery);

    /**
     * 获取字典列表(条件+分页)
     * @param condition 查询条件
     * @return 查询结果
     */
    public JsonVO<PageDTO<DictDTO>> queryList(DictQuery condition);

    /**
     * 增加字典
     * @param dict 增加字典对象
     * @return 添加结果
     */
   JsonVO<String> addDict(DictAddDTO dict);

    /**
     * 修改字典
     * @param dict 修改字典对象
     * @return 修改结果
     */
    JsonVO<String> updateDict(DictUpdateDTO dict);

    /**
     * 删除字典
     * @param id 删除字典的id
     * @return 删除结果
     */
    JsonVO<String> deleteDict(String id);
    /*
     * 新增字典类型
     * 创建一个新的字典类型，用于管理系统中的字典分类
     * @param dictTypeDTO 字典类型数据
     * @return 新增结果
     * @author Allure1
     */
    JsonVO<String> addDictType(DictTypeDTO dictTypeDTO);

    /**
     * 修改字典类型
     * 根据ID修改字典类型的名称和备注信息（编码不可修改）
     * @param updateDTO 字典类型修改数据
     * @return 修改结果
     * @author Allure1
     */
    JsonVO<String> updateDictType(String id,DictTypeUpdateDTO updateDTO);

    /**
     * 删除字典类型
     * 根据ID删除字典类型，如果存在关联字典项需要先处理关联数据
     * @param id 字典类型删除数据
     * @return 删除结果
     * @author Allure1
     */
    JsonVO<String> deleteDictType(String id);

    /**
     * 获取字典类型名称类型
     * @return 查询结果
     * @author ZGjie20
     */
    public JsonVO<List<DictTypeNameQuery>> getDictTypeNameList();

    /**
     * 获取字典类型列表
     * @return 查询结果
      * @author ZGjie20
     */
    public JsonVO<PageDTO<DictTypeDTO>> getDictTypeList(DictTypeQury dictTypeQury);

}
