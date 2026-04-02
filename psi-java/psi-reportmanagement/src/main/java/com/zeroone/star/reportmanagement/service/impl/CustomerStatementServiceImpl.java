package com.zeroone.star.reportmanagement.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.CustomerStatementMapper;
import com.zeroone.star.reportmanagement.service.CustomerStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户对账单逻辑实现
 *
 * @author toexpl
 * @since 2025/10/27
 */

@Service
public class CustomerStatementServiceImpl implements CustomerStatementService {

    @Autowired
    CustomerStatementMapper mapper;

    /**
     * 客户对账单查询
     *
     * @param query
     * @return
     */
    @Override
    public Page<CustomerStatementMainDTO> listCustomerStatement(CustomerStatementQuery query) {

        Page<CustomerStatementMainDTO> page = new Page<>(query.getPageIndex(),query.getPageSize());

        List<CustomerStatementMainDTO> list=mapper.selectCustomerStatementMain(query);

        if(query.isHideOrShowDetail()){
            for(CustomerStatementMainDTO dto:list){
                dto.setExpanded(false);
            }
        }else {
            for(CustomerStatementMainDTO dto:list){
                dto.setExpanded(true);
            }
        }

        page.setRecords(list);

        return page;
    }

    /**
     * 客户对账单信息导出
     * @param query
     */
    @Override
    public List<CustomerStatementMainDTO> exportCustomerStatement(CustomerStatementQuery query) {

        return mapper.selectCustomerStatementMain(query);

    }

}
