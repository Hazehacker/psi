package com.zeroone.star.project.j7.sysargs.supportinfo.supportattri;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AddAttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.AttributeDetailDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.supportattri.UpdateAttributeDTO;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.supportattri.AttributeQuery;
import com.zeroone.star.project.vo.JsonVO;
/** 包名：com.zeroone.star.project.j7.sysargs.supportinfo.supportattri
 * 描述：辅助属性接口
 * 作者：昭熙
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
public interface AttributeApis {
    /**
     * 增加辅助属性
     *
     * @Param 增加辅助属性数据对象
     *
     *
     * */
    JsonVO<String> addSupportAttri(AddAttributeDTO supportAttriAddDTO);

    /**
     * 删除辅助属性
     *
     * @Param 辅助属性ID
     *
     * */
    JsonVO<String> removeSupportAttri(String id);

    /**
     * 修改辅助属性
     *
     * @Param 详细辅助属性数据对象
     *
     * */

    JsonVO<String> modifySupportAttri(UpdateAttributeDTO updateAttributeDTO);

    /**
     * 获取辅助属性列表
     */
    JsonVO<PageDTO<AttributeDTO>> querySupportAttri(AttributeQuery query);

    /**
     * 获取指定属性详情
     * @param id
     * @return
     */
    JsonVO<AttributeDetailDTO> querySupportAttriDetail(String id);

    /**
     * 获取属性名与属性内容列表
     */
    JsonVO<PageDTO<AttributeDetailDTO>> querySupportAttriList(PageQuery query);
}
