package com.zeroone.star.reportmanagement.mapper.salesreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.salesreport.SalesReceiptDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesReceiptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售收款表Mapper
 *
 * @author leyu
 * @date 2025-10-26
 */
@Mapper
public interface SalesReceiptMapper {

    /**
     * 带分页的条件查询
     * @param page 分页信息
     * @param query 查询条件
     * @return 返回分页后的销售订单跟踪 DTO 列表，封装在 Page 对象中
     */
    Page<SalesReceiptDTO> selectSalesReceipt(Page<SalesReceiptDTO> page, SalesReceiptQuery query);

    /**
     * 不带分页的条件查询
     * @param query 查询条件
     * @return 返回符合条件的销售订单跟踪 DTO 列表
     */
    List<SalesReceiptDTO> selectAllWithCondition(@Param("query") SalesReceiptQuery query);
}
