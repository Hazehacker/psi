package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;
import com.zeroone.star.reportmanagement.mapper.procurementreport.PurchaseOrderTrackingFormMapper;
import com.zeroone.star.reportmanagement.service.PurchaseOrderTrackingFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu
 * @date 2025/10/23
 */
@Service
@Transactional
public class PurchaseOrderTrackingFormServiceImpl implements PurchaseOrderTrackingFormService {

    @Autowired
    PurchaseOrderTrackingFormMapper mapper;

    @Override
    public PageDTO<PurchaseOrderTrackingFormDTO> listPurchaseOrderTrackingForm(PurchaseOrderTrackingFormQuery query) {

        List<PurchaseOrderTrackingFormDTO> allRows;

        //    根据type调用正确的mapper方法
        if (query.getType() != null && query.getType() == 1) {
            allRows = mapper.selectPurchaseOrderTrackingFormByGoods(query);
        } else {
            allRows = mapper.selectPurchaseOrderTrackingForm(query);
        }

        //  手动进行分页。
        int total = allRows.size();
        int pageIndex = (int) query.getPageIndex();
        int pageSize = (int) query.getPageSize();

        int start = (pageIndex - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<PurchaseOrderTrackingFormDTO> pagedList;

        if (start >= total) {
            pagedList = new ArrayList<>();
        } else {
            pagedList = allRows.subList(start, end);
        }


        Page<PurchaseOrderTrackingFormDTO> page = new Page<>(pageIndex, pageSize, total);
        page.setRecords(pagedList);

        return PageDTO.create(page);
    }

    @Override
    public List<PurchaseOrderTrackingFormDTO> listAllForExport(PurchaseOrderTrackingFormQuery query) {
        if (query.getType() != null && query.getType() == 1) {
            return mapper.selectAllForExportByGoods(query);
        } else {
            return mapper.selectAllForExport(query);
        }
    }
}
