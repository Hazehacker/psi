package com.zeroone.star.reportmanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;

import java.util.List;

/**
 * 应付账款明细表服务接口
 * @author 天天困
 * @date 2025/10/28
 */
public interface DetailedAccountPayableStatementService {

    /**
     * 分页查询应付账款明细表
     * @param query 查询条件
     * @return 分页结果
     */
    PageDTO<DetailedAccountPayableStatementDTO> listDetailedAccountPayableStatement(DetailedAccountPayableStatementQuery query);

    /**
     * 查询所有应付账款明细用于导出
     * @param query 查询条件
     * @return 应付账款明细列表
     */
    List<DetailedAccountPayableStatementDTO> listAllForExport(DetailedAccountPayableStatementQuery query);
}
