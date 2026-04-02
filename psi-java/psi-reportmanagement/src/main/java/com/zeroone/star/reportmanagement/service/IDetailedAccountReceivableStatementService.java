package com.zeroone.star.reportmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;

import java.util.List;


public interface IDetailedAccountReceivableStatementService extends IService<DetailedAccountReceivableStatementDTO> {

    /**
     * 分页查询应收款明细表
     * @param query 查询条件
     * @return
     */
    PageDTO<DetailedAccountReceivableStatementDTO> listDetailedAccountReceivableStatement(DetailedAccountReceivableStatementQuery query);

    /**
     * 查询所有应收款明细表
     * @param query 查询条件
     * @return
     */
    List<DetailedAccountReceivableStatementDTO> listAllDetailedAccountReceivableStatement(DetailedAccountReceivableStatementQuery query);
}
