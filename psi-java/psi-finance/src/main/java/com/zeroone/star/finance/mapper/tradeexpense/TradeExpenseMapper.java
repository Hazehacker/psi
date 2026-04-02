package com.zeroone.star.finance.mapper.tradeexpense;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.TradeExpenseDO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.CostDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.ReportInfoDTO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseReportDTO;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseQuery;
import com.zeroone.star.project.query.j8.finance.tradeexpense.TradeExpenseReportQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeExpenseMapper{
    Page<TradeExpenseDO> queryTradeExpensePage(@Param("page") Page<TradeExpenseDO> page, @Param("query") TradeExpenseQuery query);

    IPage<TradeExpenseReportDTO> queryTradeExpenseReportPage(@Param("page") Page<TradeExpenseReportDTO> page, @Param("query") TradeExpenseReportQuery query);

    List<ReportInfoDTO> selectInfosById(String id);

    List<CostDTO> getSettles(@Param("ids") List<String> ids);

    List<TradeExpenseDO> queryTradeExpense(List<String> ids);
}
