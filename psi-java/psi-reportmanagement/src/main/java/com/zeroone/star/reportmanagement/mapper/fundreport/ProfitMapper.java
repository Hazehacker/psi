package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProfitMapper {
    /**
     * 查询 销售单 审核通过的 实际金额(actual) 和 单据费用(cost) 总和
     *
     * @param query 查询参数
     * @return
     */
    Map<String, BigDecimal> getSellSummary(@Param("query") ProfitQuery query);

    /**
     * 查询 销售退货单 审核通过的 实际金额(actual) 和 单据费用(cost) 总和
     *
     * @param query 查询参数
     * @return
     */
    Map<String, BigDecimal> getSreSummary(@Param("query") ProfitQuery query);

    /**
     * 查询销售单的所有审核通过的 id
     *
     * @param query 查询参数
     * @return
     */
    List<String> getSellIds(@Param("query") ProfitQuery query);

    /**
     * 查询销售退货单的所有审核通过的 id
     *
     * @param query 查询参数
     * @return
     */
    List<String> getSreIds(@Param("query") ProfitQuery query);

    /**
     * 查询销售单基础成本(bct)
     *
     * @param sellIds 销售单 id 列表
     * @return
     */
    BigDecimal getSellBctSum(@Param("ids") List<String> sellIds);

    /**
     * 查询销售退货单基础成本(bct)）
     *
     * @param sreIds 销售退货单 id 列表
     * @return
     */
    BigDecimal getSreBctSum(@Param("ids") List<String> sreIds);
}
