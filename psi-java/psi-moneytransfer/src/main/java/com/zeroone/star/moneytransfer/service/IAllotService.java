package com.zeroone.star.moneytransfer.service;

import com.zeroone.star.moneytransfer.entity.Allot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.money.transfer.*;
import com.zeroone.star.project.query.j7.money.transfer.AllotExamineQuery;
import com.zeroone.star.project.query.j7.money.transfer.AllotQuery;

import java.util.List;

/**
 * <p>
 * 转账单 服务类
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
public interface IAllotService extends IService<Allot> {


    /**
     *
     * 获取转账单列表
     * @param query 分页查询条件
     * @return
     */
    PageDTO<AllotListDTO> listAllot(AllotQuery query);

    /**
     * 新增转账单
     * @param allotDTO 转账单对象
     * @return
     */
    String saveAllot(AddAllotDTO allotDTO);

    /**
     * 获取转账单详情
     * @param id 查询账单的id
     * @return
     */
    AllotDetailDTO getAllotdetail(String id);

//    /**
//     * 获取账户列表
//     * @return
//     */
//    List<String> getAccounts();
//
//    /**
//     * 获取关联人员列表
//     * @return
//     */
//    List<String> getPeopleList();

    /**
     * 批量删除转账单
     * @param ids 转账单ID列表
     * @return 是否删除成功
     */
    List<String> removeAllots(List<String> ids);

    /**
     * 批量审核/反审核转账单
     * @param query 审核状态参数
     * @param ids 转账单ID列表
     * @return 操作是否成功
     */
    List<String> examine(AllotExamineQuery query, List<String> ids);

    /**
     * 修改转账单
     * @param allotModifyDTO 转账单
     * @return 修改是否成功
     */
    boolean updateAllot(AllotModifyDTO allotModifyDTO);
}
