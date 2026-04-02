package com.zeroone.star.reportmanagement.controller.fundreport;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.j5.fundreport.DetailedAccountPayableStatementApi;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.DetailedAccountPayableStatementExcel;
import com.zeroone.star.reportmanagement.service.DetailedAccountPayableStatementService;
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
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 应付款明细表控制器类
 */
@RestController
@RequestMapping("/j5-reportmanagement/detailed-account-payable-statement")
@Api(tags = "应付款明细表")
public class DetailedListOfAccountsPayableController implements DetailedAccountPayableStatementApi {

    @Resource
    private DetailedAccountPayableStatementService service;

    @Resource
    private EasyExcelComponent excel;

    @GetMapping("/query")
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<DetailedAccountPayableStatementDTO>> listDetailedAccountPayableStatement(DetailedAccountPayableStatementQuery query) {
        PageDTO<DetailedAccountPayableStatementDTO> dtoPageDTO = service.listDetailedAccountPayableStatement(query);
        return JsonVO.success(dtoPageDTO);
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportDetailedAccountPayableStatementToExcel(DetailedAccountPayableStatementQuery query) {
        List<DetailedAccountPayableStatementDTO> listAllForExport = service.listAllForExport(query);
        List<DetailedAccountPayableStatementExcel> excelDtos = new ArrayList<>(listAllForExport.size());

        for (DetailedAccountPayableStatementDTO dto : listAllForExport) {
            DetailedAccountPayableStatementExcel formExcel = new DetailedAccountPayableStatementExcel();
            BeanUtils.copyProperties(dto, formExcel);

            // 处理日期转换
            if (dto.getDateTime() != null && !dto.getDateTime().isEmpty()) {
                try {
                    formExcel.setDateTime(LocalDate.parse(dto.getDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception e) {
                    formExcel.setDateTime(null);
                }
            }

            excelDtos.add(formExcel);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            excel.export("应付款明细表", byteArrayOutputStream, DetailedAccountPayableStatementExcel.class, excelDtos);
            HttpHeaders headers = new HttpHeaders();
            String filename = "应付款明细表.xlsx";
            // todo 这里方便apifox 测试，实际应该使用中文文件名
            String encodedFilename = URLEncoder.encode(filename, "UTF-8");
            headers.setContentDispositionFormData("attachment", encodedFilename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ResponseEntity<byte[]> res = new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
            byteArrayOutputStream.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
