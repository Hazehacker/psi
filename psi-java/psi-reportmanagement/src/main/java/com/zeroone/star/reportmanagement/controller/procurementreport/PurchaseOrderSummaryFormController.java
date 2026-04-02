package com.zeroone.star.reportmanagement.controller.procurementreport;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.j5.procurementreport.PurchaseOrderSummaryFormApi;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderSummaryFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.PurchaseOrderSummaryFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 采购汇总表控制器类
 * @author yu
 * @date 2025/10/18
 */
@RestController
@RequestMapping("/j5-reportmanagement/purchase-order-summary-form")
@Api(tags = "采购汇总表")
public class PurchaseOrderSummaryFormController implements PurchaseOrderSummaryFormApi {

    @Resource
    private PurchaseOrderSummaryFormService service;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<PurchaseOrderSummaryFormDTO>> listPurchaseOrderSummaryForm(@Validated PurchaseOrderSummaryFormQuery query) {
        return JsonVO.success(service.listPurchaseSummary(query));
    }

    @SneakyThrows
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportPurchaseOrderSummaryFormToExcel(PurchaseOrderSummaryFormQuery query) {
        return service.export(query);
    }
}
