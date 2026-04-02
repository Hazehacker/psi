package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Bre;
import com.zeroone.star.homepage.mapper.BreMapper;
import com.zeroone.star.homepage.service.IBreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j1.homepage.PurchaseReturnDailyTotalDTO;
import com.zeroone.star.project.dto.j1.homepage.PurchaseReturnDailyTotalResponseDTO;
import com.zeroone.star.project.query.j1.homepage.PurchaseReturnDailyTotalQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 采购退货单 服务实现类
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Service
public class BreServiceImpl extends ServiceImpl<BreMapper, Bre> implements IBreService {

    @Resource
    BreMapper breMapper;
    /**
     * 查询每日采购退货单汇总
     * @param query
     * @return
     */
    @Override
    public PurchaseReturnDailyTotalResponseDTO queryPurchaseReturnDailyTotal(PurchaseReturnDailyTotalQuery query) {
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();

        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(30);
        } else if (startDate == null) {
            startDate = endDate.minusDays(30);
        } else if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        String startDateString = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateString = endDate.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<PurchaseReturnDailyTotalDTO> purchaseReturnDailyTotalDTOS = breMapper.selectPurchaseReturnDailyTotal(startDateString, endDateString);
        PurchaseReturnDailyTotalResponseDTO purchaseReturnDailyTotalResponseDTO = new PurchaseReturnDailyTotalResponseDTO(purchaseReturnDailyTotalDTOS);
        return purchaseReturnDailyTotalResponseDTO;
    }
}
