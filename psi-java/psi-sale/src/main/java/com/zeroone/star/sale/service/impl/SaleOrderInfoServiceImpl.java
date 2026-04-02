package com.zeroone.star.sale.service.impl;

import com.zeroone.star.project.dto.j4.sale.SaleOrderInfoDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.SaleOrder;
import com.zeroone.star.sale.mapper.SaleOrderInfoMapper;
import com.zeroone.star.sale.mapper.SaleOrderMapper;
import com.zeroone.star.sale.service.SaleOrderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderInfoServiceImpl implements SaleOrderInfoService {
    @Resource
    private SaleOrderMapper saleOrderMapper;
    @Resource
    private SaleOrderInfoMapper saleOrderInfoMapper;

    @Override
    public JsonVO<List<List<SaleOrderInfoDTO>>> getByQuery(String customer, LocalDateTime time, String number) {
        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customer);
        saleOrder.setTime(time);
        saleOrder.setNumber(number);
        List<SaleOrder> saleOrders = saleOrderMapper.getByQuery(saleOrder);
        List<List<SaleOrderInfoDTO>> saleOrderInfos = new ArrayList<>();
        for (SaleOrder order : saleOrders) {
            List<SaleOrderInfoDTO> saleOrderInfoDTO = saleOrderInfoMapper.getById(order.getId());
            saleOrderInfos.add(saleOrderInfoDTO);
        }

        return JsonVO.success(saleOrderInfos);
    }
}
