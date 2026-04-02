package com.zeroone.star.reportmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICashBankStatementService extends IService<CashBankStatementDTO> {

    /**
     * 分页查询现金银行报表
     * @param query 查询条件
     * @return
     */
    PageDTO<CashBankStatementDTO> listCashBankStatement(CashBankStatementQuery query);

    /**
     * 查询所有现金银行报表
     * @param query 查询条件
     * @return
     */
    List<CashBankStatementDTO> listAllCashBankStatement(CashBankStatementQuery query);
}
