package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;

import java.util.List;

/**
 * 其他收支明细表服务接口
 * @author 天天困
 * @date 2025/10/28
 */
public interface OtherIncomeExpenditureDetailService {

    /**
     * 分页查询其他收支明细表
     * @param query 查询条件
     * @return 分页结果
     */
    PageDTO<OtherIncomeExpenditureDetailDTO> listOtherIncomeExpenditureDetail(OtherIncomeExpenditureQuery query);

    /**
     * 查询所有其他收支明细用于导出
     * @param query 查询条件
     * @return 其他收支明细列表
     */
    List<OtherIncomeExpenditureDetailDTO> listAllForExport(OtherIncomeExpenditureQuery query);
}
