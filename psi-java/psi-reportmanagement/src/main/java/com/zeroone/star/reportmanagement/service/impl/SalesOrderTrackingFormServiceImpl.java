package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesOrderTrackingFormQuery;
import com.zeroone.star.reportmanagement.entity.SalesOrderTrackingByGoodsExcel;
import com.zeroone.star.reportmanagement.entity.SalesOrderTrackingByOrderExcel;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesOrderTrackingFormMapper;
import com.zeroone.star.reportmanagement.service.SalesOrderTrackingFormService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单跟踪表服务实现
 *
 * @author leyu
 * @date 2025-10-23
 */

@Service
public class SalesOrderTrackingFormServiceImpl implements SalesOrderTrackingFormService {

    @Autowired
    private SalesOrderTrackingFormMapper mapper;

    @Autowired
    EasyExcelComponent excel;

    /**
     * 带条件的分页查询
     *
     * @param query 查询条件
     * @return PageDTO
     */
    @Override
    public PageDTO<SalesOrderTrackingFormDTO> listAllPage(SalesOrderTrackingFormQuery query) {

        List<SalesOrderTrackingFormDTO> allRows;

        //    根据type调用正确的mapper方法
        if (query.getType() != null && query.getType() == 1) {
            allRows = mapper.selectSalesOrderTrackingFormByGoods(query);
        } else {
            allRows = mapper.selectSalesOrderTrackingForm(query);
        }

        //  手动进行分页。
        int total = allRows.size();
        int pageIndex = (int) query.getPageIndex();
        int pageSize = (int) query.getPageSize();

        int start = (pageIndex - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<SalesOrderTrackingFormDTO> pagedList;

        if (start >= total) {
            pagedList = new ArrayList<>();
        } else {
            pagedList = allRows.subList(start, end);
        }


        Page<SalesOrderTrackingFormDTO> page = new Page<>(pageIndex, pageSize, total);
        page.setRecords(pagedList);

        return PageDTO.create(page);
    }

    @Override
    @SneakyThrows
    public byte[] exportExcel(SalesOrderTrackingFormQuery query) {

        List<SalesOrderTrackingFormDTO> nestedList;

        if (query.getType() != null && query.getType() == 1) {
            nestedList = mapper.selectAllForExportByGoods(query);
        } else {
            nestedList = mapper.selectAllForExport(query);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        /* ------------------------------------------------------------------
         *  Type 1：按商品导出（父级商品 → 空行 → 子级单据信息）
         * ------------------------------------------------------------------ */
        if (query.getType() != null && query.getType() == 1) {

            List<SalesOrderTrackingByGoodsExcel> flatList = new ArrayList<>();

            for (SalesOrderTrackingFormDTO parentProduct : nestedList) {

                /* ① 创建父级“商品信息”行（只出现一次） */
                SalesOrderTrackingByGoodsExcel parentRow = new SalesOrderTrackingByGoodsExcel();
                parentRow.setGoodsName(parentProduct.getGoodsName());
                parentRow.setAttr(parentProduct.getAttr());
                parentRow.setWarehouseName(parentProduct.getWarehouseName());
                flatList.add(parentRow);

                /* ② 空行（可选，保持你想要的格式） */
                //flatList.add(new SalesOrderTrackingByGoodsExcel());

                /* ③ 子级单据明细行 */
                for (SalesOrderTrackingFormDTO childOrder : parentProduct.getItems()) {

                    SalesOrderTrackingByGoodsExcel childRow = new SalesOrderTrackingByGoodsExcel();

                    // 子级行：只填子级信息
                    childRow.setFrameName(childOrder.getFrameName());
                    childRow.setCustomerName(childOrder.getCustomerName());
                    childRow.setTime(childOrder.getTime());
                    childRow.setNumber(childOrder.getNumber());
                    childRow.setArrival(childOrder.getArrival());
                    childRow.setUnit(childOrder.getUnit());
                    childRow.setPrice(childOrder.getPrice());
                    childRow.setNums(childOrder.getNums());
                    childRow.setTotal(childOrder.getTotal());
                    childRow.setItemRemark(childOrder.getItemRemark());
                    childRow.setUnstockedQuantity(childOrder.getUnstockedQuantity());
                    childRow.setUnstockedAmount(childOrder.getUnstockedAmount());
                    childRow.setState(childOrder.getItemState());
                    childRow.setWarehouseName(childOrder.getWarehouseName());

                    flatList.add(childRow);
                }

                // 如果想每个商品块后再多空一行，就加上：
                // flatList.add(new SalesOrderTrackingByGoodsExcel());
            }

            excel.export("销售订单跟踪表(按商品)", byteArrayOutputStream,
                    SalesOrderTrackingByGoodsExcel.class, flatList);
        }


        /* ------------------------------------------------------------------
         *  Type 0：按单据导出（父级订单 → 空行 → 子级商品行）
         * ------------------------------------------------------------------ */
        else {

            List<SalesOrderTrackingByOrderExcel> flatExcelList = new ArrayList<>();

            for (SalesOrderTrackingFormDTO parentOrder : nestedList) {

                /* ① 父级订单行（只出现一次） */
                SalesOrderTrackingByOrderExcel parentRow = new SalesOrderTrackingByOrderExcel();
                parentRow.setFrameName(parentOrder.getFrameName());
                parentRow.setCustomerName(parentOrder.getCustomerName());
                parentRow.setTime(parentOrder.getTime());
                parentRow.setNumber(parentOrder.getNumber());
                parentRow.setArrival(parentOrder.getArrival());
                flatExcelList.add(parentRow);

                /* ② 空行 */
                //flatExcelList.add(new SalesOrderTrackingByOrderExcel());

                /* ③ 子级商品行 */
                for (SalesOrderTrackingFormDTO childProduct : parentOrder.getItems()) {

                    SalesOrderTrackingByOrderExcel childRow = new SalesOrderTrackingByOrderExcel();

                    childRow.setGoodsName(childProduct.getGoodsName());
                    childRow.setAttr(childProduct.getAttr());
                    childRow.setWarehouseName(childProduct.getWarehouseName());
                    childRow.setUnit(childProduct.getUnit());
                    childRow.setPrice(childProduct.getPrice());
                    childRow.setNums(childProduct.getNums());
                    childRow.setTotal(childProduct.getTotal());
                    childRow.setItemRemark(childProduct.getItemRemark());
                    childRow.setUnstockedQuantity(childProduct.getUnstockedQuantity());
                    childRow.setUnstockedAmount(childProduct.getUnstockedAmount());
                    childRow.setState(childProduct.getItemState());
                    childRow.setWarehouseName(childProduct.getWarehouseName());

                    flatExcelList.add(childRow);
                }
            }

            excel.export("采购订单跟踪表(按单据)", byteArrayOutputStream,
                    SalesOrderTrackingByOrderExcel.class, flatExcelList);
        }


        byte[] byteArray = byteArrayOutputStream.toByteArray();


        byteArrayOutputStream.close();

        return byteArray;
    }



}
