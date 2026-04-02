package com.zeroone.star.reportmanagement.mapper.fundreport;


import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户对账单持久层实现
 *
 * @author toexpl
 * @since 2025/10/27
 */

@Mapper
public interface CustomerStatementMapper {

    /**
     * 查询客户对账表
     * @param query
     */
    List<CustomerStatementMainDTO> selectCustomerStatementMain(CustomerStatementQuery query);

}
