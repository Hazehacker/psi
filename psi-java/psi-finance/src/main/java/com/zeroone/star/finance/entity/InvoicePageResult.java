package com.zeroone.star.finance.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 发票详情
 * </p>
 *
 * @author 多令
 * @since 2025-10-26
 */
@Getter
@Setter
public class InvoicePageResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数（满足查询条件的所有数据条数）
     */
    private Long totalCount;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    private Integer pageSize;

    /**
     * 当前页的数据列表
     */
    private List<InvoiceOneResult> list;
}
