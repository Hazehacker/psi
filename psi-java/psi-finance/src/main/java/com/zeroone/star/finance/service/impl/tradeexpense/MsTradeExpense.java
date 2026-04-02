package com.zeroone.star.finance.service.impl.tradeexpense;

import com.zeroone.star.finance.entity.TradeExpenseDO;
import com.zeroone.star.project.dto.j8.finance.tradeexpense.TradeExpenseResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsTradeExpense {
    /**
     * DO转DTO
     * @param tradeExpenseDO
     * @return
     */
    TradeExpenseResponseDTO TradeExpenseDO2DTO(TradeExpenseDO tradeExpenseDO);
}
