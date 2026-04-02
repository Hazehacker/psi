package com.zeroone.star.reportmanagement.controller.fundreport;

import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.j5.fundreport.DetailedAccountPayableStatementApi;
import com.zeroone.star.project.j5.fundreport.OtherIncomeExpenditureDetailApi;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.OtherIncomeExpenditureDetailExcel;
import com.zeroone.star.reportmanagement.service.OtherIncomeExpenditureDetailService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 其他收支明细表控制器类
 */
@RestController
@RequestMapping("/j5-reportmanagement/other-income-expenditure")
@Api(tags = "其他收支明细表")
public class OtherIncomeExpenditureDetailController implements OtherIncomeExpenditureDetailApi {

    @Resource
    private OtherIncomeExpenditureDetailService service;

    @Resource
    private EasyExcelComponent excel;

    @GetMapping("/query")
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<OtherIncomeExpenditureDetailDTO>> listOtherIncomeExpenditureDetail(OtherIncomeExpenditureQuery query) {
        PageDTO<OtherIncomeExpenditureDetailDTO> dtoPageDTO = service.listOtherIncomeExpenditureDetail(query);
        return JsonVO.success(dtoPageDTO);
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportOtherIncomeExpenditureDetailToExcel(OtherIncomeExpenditureQuery query) {
        List<OtherIncomeExpenditureDetailDTO> listAllForExport = service.listAllForExport(query);
        List<OtherIncomeExpenditureDetailExcel> excelDtos = new ArrayList<>(listAllForExport.size());

        for (OtherIncomeExpenditureDetailDTO dto : listAllForExport) {
            OtherIncomeExpenditureDetailExcel formExcel = new OtherIncomeExpenditureDetailExcel();
            BeanUtils.copyProperties(dto, formExcel);
            excelDtos.add(formExcel);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            excel.export("其他收支明细表", byteArrayOutputStream, OtherIncomeExpenditureDetailExcel.class, excelDtos);
            HttpHeaders headers = new HttpHeaders();
            String filename = "其他收支明细表.xlsx";
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
