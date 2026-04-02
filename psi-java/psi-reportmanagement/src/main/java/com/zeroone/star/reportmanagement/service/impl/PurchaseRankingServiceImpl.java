package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseRankingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseRankingFormQuery;
import com.zeroone.star.reportmanagement.mapper.procurementreport.PurchaseRankingFormMapper;
import com.zeroone.star.reportmanagement.service.PurchaseRankingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PurchaseRankingServiceImpl implements PurchaseRankingService {

    @Resource
    PurchaseRankingFormMapper purchaseRankingFormMapper;

    @Override
    public PageDTO<PurchaseRankingFormDTO> listPurchaseRankingForm(PurchaseRankingFormQuery query) {
        Page<PurchaseRankingFormDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        purchaseRankingFormMapper.listPurchaseRankingForm(page, query);
        return PageDTO.create(page);
    }
}
