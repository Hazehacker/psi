package com.zeroone.star.reportmanagement.controller.fundreport;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderSummaryFormDTO;
import com.zeroone.star.project.j5.fundreport.UnitArrearsReportApi;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.UnitArrearsReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

/**
 * 往来单位欠款表控制器类
 * @author 言语
 * @date 2025/10/20
 */
@RestController
@RequestMapping("/j5-reportmanagement/unit-arrears-report-form")
@Api(tags = "往来单位欠款表")
public class UnitArrearsReportController implements UnitArrearsReportApi {

    @Autowired
    private UnitArrearsReportService unitArrearsReportService;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<UnitArrearsReportDTO>> listUnitArrearsReportForm(@Validated UnitReceiptQuery query) {
        return JsonVO.success(unitArrearsReportService.query(query));
    }

    @Resource
    EasyExcelComponent excel;

    @SneakyThrows
    @GetMapping(value = "/export")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportUnitArrearsReportFormToExcel(@Validated UnitReceiptQuery query) {
        PageDTO<UnitArrearsReportDTO> pageDTO = unitArrearsReportService.query(query);
        List<UnitArrearsReportDTO> rows = pageDTO.getRows();
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        excel.export("采购订单汇总表", out, UnitArrearsReportDTO.class, rows);
        // 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "rep-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return res;
    }
}
