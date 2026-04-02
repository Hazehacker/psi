package com.zeroone.star.supportinfo.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AddAttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.UpdateAttributeDTO;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.supportattri.AttributeQuery;
import com.zeroone.star.supportinfo.entity.Attribute;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 辅助属性[基础] 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
public interface IAttributeService extends IService<Attribute> {
    /**
     * 获取辅助属性列表
     * @param query 查询所有数据
     * @return 所有数据
     */
    public PageDTO<AttributeDTO> listSupportAttri(AttributeQuery query);
    /**
     * 获取指定属性详情
     * @param id 获取单条数据
     * @return 单个数据
     */
    public AttributeDetailDTO getSupportAttriDetail(String id);
    /**
     * 获取属性名与属性内容列表
     * @return 所有数据
     */
    public PageDTO<AttributeDetailDTO> listSupportAttriList(PageQuery query);

    /**
     * 新增辅助属性
     * @param AddAttributeDTO 新增数据
     * @return 新增结果
     */
    public String saveAttribute(AddAttributeDTO AddAttributeDTO);

    /**
     * 删除指定属性
     * @param id 要删除对应数据的id
     * @return 删除结果
     */
    public boolean removeSupportAttri(String id);

    /**
     * 修改指定属性
     *
     * @param updateAttributeDTO 要修改的对应数据对象
     * @return 修改结果
     */
    public boolean updateSupportAttri(UpdateAttributeDTO updateAttributeDTO);
}
