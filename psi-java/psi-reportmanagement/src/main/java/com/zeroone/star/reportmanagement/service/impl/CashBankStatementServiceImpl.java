package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.CashBankStatementMapper;
import com.zeroone.star.reportmanagement.service.ICashBankStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CashBankStatementServiceImpl extends ServiceImpl<CashBankStatementMapper, CashBankStatementDTO> implements ICashBankStatementService {

    @Resource
    CashBankStatementMapper cashBankStatementMapper;

    @Override
    public PageDTO<CashBankStatementDTO> listCashBankStatement(CashBankStatementQuery query) {
        // 参数校验
        if (query == null) {
            throw new IllegalArgumentException("查询参数不能为空");
        }
        log.info("查询现金银行报表，参数：{}", query);
        // 构建分页查询对象
        Page<CashBankStatementDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 分页查询
        Page<CashBankStatementDTO> resultPage  = cashBankStatementMapper.selectByQuery(page,query);
        log.info("查询完成，共{}条记录", resultPage.getTotal());
        return PageDTO.create(resultPage);
    }

    @Override
    public List<CashBankStatementDTO> listAllCashBankStatement(CashBankStatementQuery query) {
        return cashBankStatementMapper.selectAllByQuery(query);
    }
}
