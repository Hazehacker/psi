package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesItemDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryExportDTO;
import com.zeroone.star.project.dto.j5.salesreport.SummaryDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesSummaryQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.SalesSummaryDO;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesSummaryMapper;
import com.zeroone.star.reportmanagement.service.SalesSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;



/*
* 销售汇总表实现类
* */
@Service
@Slf4j
public class SalesSummaryServiceImpl implements SalesSummaryService {

    @Autowired
    private SalesSummaryMapper salesSummaryMapper;

    /*
     * 销售汇总表分页查询
     * */
    public PageDTO<SalesSummaryDTO> query(SalesSummaryQuery query) {
//
//        // 构建分页查询对象
//        Page<SalesSummaryDTO> page = new Page<>(query.getPageIndex(),query.getPageSize());
//
//        // 直接查询DO列表
//        List<SalesSummaryDO> doList = salesSummaryMapper.selectSalesSummaryPage(query);
        Page<SalesSummaryDO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        IPage<SalesSummaryDO> result = salesSummaryMapper.selectSalesSummaryPage(page, query);

        List<SalesSummaryDTO> list = convertToDTO(result);

        PageDTO<SalesSummaryDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageIndex( result.getCurrent());
        pageDTO.setPageSize(result.getSize());
        pageDTO.setTotal(result.getTotal());
        pageDTO.setRows(list);

        return pageDTO;
    }

    /*
    * 导出报表为excel，不分页
    * */
    public List<SalesSummaryExportDTO> queryForExport(SalesSummaryQuery query) {
        Page<SalesSummaryDO> page = new Page<>(); // 不设置 page 和 size，MyBatis-Plus 会查所有数据
        IPage<SalesSummaryDO> result = salesSummaryMapper.selectSalesSummaryPage(page, query);
        List<SalesSummaryExportDTO> list = new ArrayList<>();
        // 检查数据是否为空
        if (result == null || result.getRecords() == null || result.getRecords().isEmpty()) {
            SalesSummaryExportDTO emptyRow = new SalesSummaryExportDTO();
            emptyRow.setProductName("无数据");
            // 其他字段设为null（Excel会显示为空）
            list.add(emptyRow);
            return list;
        }
        list = convertToSalesSummaryExportDTO(query,result);
        return list;
    }

    /*
    * 将SalesSummaryDO转为SalesSummaryDTO
    * */
    private List<SalesSummaryDTO> convertToDTO(IPage<SalesSummaryDO> result){
        List<SalesSummaryDTO>  list = new ArrayList<>();

        for (SalesSummaryDO salesSummaryDO : result.getRecords()) {
            SalesSummaryDTO salesSummaryDTO = new SalesSummaryDTO();
            salesSummaryDTO.setGroupField(salesSummaryDO.getGroupField());
            salesSummaryDTO.setProductName(salesSummaryDO.getProductName());
            salesSummaryDTO.setAttribute(salesSummaryDO.getAttribute());
            salesSummaryDTO.setWarehouse(salesSummaryDO.getWarehouse());
            salesSummaryDTO.setUnit(salesSummaryDO.getUnit());

            // 销售单
            SalesItemDTO salesItemDTO = new SalesItemDTO();
            salesItemDTO.setQuantity(salesSummaryDO.getSalesCount() != null ? salesSummaryDO.getSalesCount() : 0);
            salesItemDTO.setPrice(salesSummaryDO.getSalesPrice() != null ? salesSummaryDO.getSalesPrice() : BigDecimal.ZERO);
            salesItemDTO.setAmount(salesSummaryDO.getSalesTotalPrice() != null ? salesSummaryDO.getSalesTotalPrice() : BigDecimal.ZERO);
            salesSummaryDTO.setSales(salesItemDTO);

            // 销售退货单
            SalesItemDTO returnsSalesItemDTO = new SalesItemDTO();
            returnsSalesItemDTO.setQuantity(salesSummaryDO.getReturnCount() != null ? salesSummaryDO.getReturnCount() : 0);
            returnsSalesItemDTO.setPrice(salesSummaryDO.getReturnPrice() != null ? salesSummaryDO.getReturnPrice() : BigDecimal.ZERO);
            returnsSalesItemDTO.setAmount(salesSummaryDO.getReturnTotalPrice() != null ? salesSummaryDO.getReturnTotalPrice() : BigDecimal.ZERO);
            salesSummaryDTO.setReturnSales(returnsSalesItemDTO);// 将销售退货单封装数据传参

            // 计算汇总
            SummaryDTO summaryDTO = new SummaryDTO();
            summaryDTO.setTotalQuantity(
                    salesItemDTO.getQuantity() - returnsSalesItemDTO.getQuantity()
            );
            summaryDTO.setTotalAmount(
                    salesItemDTO.getAmount().subtract(returnsSalesItemDTO.getAmount())
            );


            salesSummaryDTO.setSummary(summaryDTO);// 将汇总数据封装传参
            list.add(salesSummaryDTO);
        }
        return list;
    }

    /*
     * 将SalesSummaryDO转为SalesSummaryExportDTO
     * */
    private List<SalesSummaryExportDTO> convertToSalesSummaryExportDTO(SalesSummaryQuery query,IPage<SalesSummaryDO> result){
        List<SalesSummaryExportDTO>  list = new ArrayList<>();
        for (SalesSummaryDO salesSummaryDO : result.getRecords()) {
            SalesSummaryExportDTO salesSummaryExportDTO = new SalesSummaryExportDTO();
            // 根据分组条件设置groupField的值
            String groupFieldValue = getGroupFieldValue(salesSummaryDO, query.getGroupBy());
            salesSummaryExportDTO.setGroupField(groupFieldValue);

            salesSummaryExportDTO.setProductName(salesSummaryDO.getProductName());
            salesSummaryExportDTO.setAttribute(salesSummaryDO.getAttribute());
            salesSummaryExportDTO.setWarehouse(salesSummaryDO.getWarehouse());
            salesSummaryExportDTO.setUnit(salesSummaryDO.getUnit());

            // 销售单单价
            BigDecimal salesPrice = salesSummaryDO.getSalesPrice() != null ? salesSummaryDO.getSalesPrice() : BigDecimal.ZERO;
            // 销售单金额
            BigDecimal salesTotalPrice = salesSummaryDO.getSalesTotalPrice() != null ? salesSummaryDO.getSalesTotalPrice() : BigDecimal.ZERO;
            // 销售单数量
            Integer salesCount = salesSummaryDO.getSalesCount() != null ? salesSummaryDO.getSalesCount() : 0;

            // 销售退货单金额
            BigDecimal returnTotalPrice = salesSummaryDO.getReturnTotalPrice() != null ? salesSummaryDO.getReturnTotalPrice() : BigDecimal.ZERO;
            // 销售退货单单价
            BigDecimal returnPrice = salesSummaryDO.getReturnPrice() != null ? salesSummaryDO.getReturnPrice() : BigDecimal.ZERO;
            // 销售退货单数量
            Integer returnCount = salesSummaryDO.getReturnCount() != null ? salesSummaryDO.getReturnCount() : 0;

            // 销售单
            salesSummaryExportDTO.setSalesPrice(salesPrice);
            salesSummaryExportDTO.setSalesQuantity(salesCount);
            salesSummaryExportDTO.setSalesPrice(salesTotalPrice);

            // 销售退货单
            salesSummaryExportDTO.setReturnPrice(returnPrice);
            salesSummaryExportDTO.setReturnQuantity(returnCount);
            salesSummaryExportDTO.setReturnAmount(returnTotalPrice);

            // 计算汇总
            salesSummaryExportDTO.setTotalQuantity(salesCount - returnCount); // 汇总数量
           salesSummaryExportDTO.setTotalAmount(salesTotalPrice.subtract(returnTotalPrice)); // 汇总金额

            list.add(salesSummaryExportDTO);
        }
        return list;
    }

    /**
     * 根据分组类型获取groupField的显示值
     */
    private String getGroupFieldValue(SalesSummaryDO salesSummaryDO, String groupBy) {
        if (groupBy == null || "product".equals(groupBy)) {
            // 按商品分组时，groupField为空
            return null;
        }

        return salesSummaryDO.getGroupField();
    }
}
