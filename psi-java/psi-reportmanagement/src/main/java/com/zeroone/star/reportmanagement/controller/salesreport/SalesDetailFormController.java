package com.zeroone.star.reportmanagement.controller.salesreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesDetailFormDTO;
import com.zeroone.star.project.j5.salesreport.SalesDetailReportApi;
import com.zeroone.star.project.query.j5.salesreport.SalesDetailFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.SalesDetailFormExcel;
import com.zeroone.star.reportmanagement.service.SalesDetailFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售明细表控制器
 * @author rainsilent
 * @date 2025/10/19
 */
@Api(tags = "销售明细表")
@RequestMapping("/j5-reportmanagement/sales-detail-form")
@RestController
public class SalesDetailFormController implements SalesDetailReportApi {
    @Resource
    SalesDetailFormService salesDetailFormService;
    @Resource
    EasyExcelComponent excel;

    @Override
    @ApiOperation(value = "获取报表(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<SalesDetailFormDTO>> listSalesDetailForm(SalesDetailFormQuery query) {
        if (query.getStartTime() != null && query.getEndTime() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(query.getStartTime(), formatter);
            LocalDate endDate = LocalDate.parse(query.getEndTime(), formatter);
            if (startDate.isAfter(endDate)){
                return JsonVO.fail(null);
            }
        }
        PageDTO<SalesDetailFormDTO> pageDTO = salesDetailFormService.listSalesDetailForm(query);
        return JsonVO.success(pageDTO);
    }

    @Override
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    public ResponseEntity<byte[]> exportSalesDetailFormToExcel(SalesDetailFormQuery query) {
        List<SalesDetailFormDTO> list = salesDetailFormService.listAllForExcel(query);
        List<SalesDetailFormExcel> excelList = list.stream().map(dto -> {
            SalesDetailFormExcel excel = new SalesDetailFormExcel();
            BeanUtils.copyProperties(dto, excel);
            return excel;
        }).collect(Collectors.toList());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            excel.export("销售明细表", out, SalesDetailFormExcel.class, excelList);
            String filename = URLEncoder.encode("销售明细表-" +
                    DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx","UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("导出销售明细表失败", e);
        }
    }
}
