package com.zeroone.star.reportmanagement.controller.procurementreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.ProcurementDetailFromDTO;
import com.zeroone.star.project.j5.procurementreport.ProcurementDetailReportApi;
import com.zeroone.star.project.query.j5.procurementreport.ProcurementDetailFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.ProcurementDetailFormExcel;
import com.zeroone.star.reportmanagement.service.ProcurementDetailFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

/**
 * 采购明细表
 * @author chuming_7
 * @since 2025-10-24
 */
@Api(tags = "采购明细表")
@RequestMapping("/j5-reportmanagement/procurement-detail-form")
@RestController
public class ProcurementDetailFormController implements ProcurementDetailReportApi{
    @Resource
    ProcurementDetailFormService service;
    @Resource
    EasyExcelComponent excel;
    @Autowired
    private ProcurementDetailFormService procurementDetailFormService;

    @GetMapping("/query")
    @ApiOperation("获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<ProcurementDetailFromDTO>> listProcurementDetailForm(ProcurementDetailFormQuery query){
        if (query.getStartTime() != null && query.getEndTime() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(query.getStartTime(), formatter);
            LocalDate endDate = LocalDate.parse(query.getEndTime(), formatter);
            if (startDate.isAfter(endDate)){
                return JsonVO.fail(null);
            }
        }
        PageDTO<ProcurementDetailFromDTO> pageDTO = procurementDetailFormService.listProcurementDetailForm(query);
        return JsonVO.success(pageDTO);
    }


    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation("导出报表")
    @Override
    public ResponseEntity<byte[]> exportProcurementDetailFormToExcel(ProcurementDetailFormQuery query){
        List<ProcurementDetailFromDTO> list = service.listAllForExport(query);
        List<ProcurementDetailFormExcel> excelList = list.stream().map(dto -> {
            ProcurementDetailFormExcel excel = new ProcurementDetailFormExcel();
            BeanUtils.copyProperties(dto, excel);
            return excel;
        }).collect(Collectors.toList());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            excel.export("采购明细表", out, ProcurementDetailFormExcel.class, excelList);
            String filename = URLEncoder.encode("采购明细表-" +
                    DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx","UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("导出采购明细表失败", e);
        }
    }
}
