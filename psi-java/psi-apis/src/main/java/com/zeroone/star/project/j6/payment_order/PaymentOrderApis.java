package com.zeroone.star.project.j6.payment_order;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.payment_order.*;
import com.zeroone.star.project.query.j6.payment_order.OmyQuery;
import com.zeroone.star.project.vo.JsonVO;
import java.util.List;

public interface PaymentOrderApis {
    /**
     * 新增付款单(包含主表记录和关联信息表记录（自动审核）)
     * @param addDto 新增数据
     * @return 新增结果
     */
    JsonVO<String> addOmyWithInfo(OmyWithInfoAddDTO addDto);


    /**
     * 修改付款单
     * @param dto 修改数据
     * @return 修改结果
     */
    JsonVO<String> modifyOmyAll(OmyAllDTO dto,String id);

    /**
     * 获取付款单列表（包含主表和账单表数据）
     * @param condition 查询条件
     * @return 查询结果
     */
    JsonVO<PageDTO<OmyWithBillDTO>> queryAllWithBill(OmyQuery condition);

    /**
     * 获取指定付款单详情（包含主表和信息表数据）
     * @param id 编号
     * @return 查询结果
     */
    JsonVO<OmyWithInfoDTO> queryByIdWithInfo(String id);

    /**
     * 删除付款单主表记录(同时删除主表，bill表和info表记录)
     * @param ids 要删除数据的编号
     * @return 删除结果
     */
    JsonVO<String> removeOmy(List<String> ids);

    /**
     * 审核/反审核（切换审核状态）
     * @param ids 主表ID
     * @return 操作结果
     */
    JsonVO<String> modifyExamineStatus(List<String> ids);

}
