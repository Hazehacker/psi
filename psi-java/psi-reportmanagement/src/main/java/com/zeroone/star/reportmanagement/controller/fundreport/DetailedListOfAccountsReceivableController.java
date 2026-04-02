package com.zeroone.star.reportmanagement.controller.fundreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.j5.fundreport.DetailedAccountReceivableStatementApi;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.IDetailedAccountReceivableStatementService;
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
import java.util.List;

/**
 * 应收款明细表控制器类
 */
@RestController
@RequestMapping("/j5-reportmanagement/detailed-account-receivable-statement")
@Api(tags = "应收款明细表")
public class DetailedListOfAccountsReceivableController implements DetailedAccountReceivableStatementApi {

    @Resource
    IDetailedAccountReceivableStatementService service;

    @GetMapping()
    @ApiOperation(value = "获取报表(条件+分页)")
    @Override
    public JsonVO<PageDTO<DetailedAccountReceivableStatementDTO>> listDetailedAccountReceivableStatement(@Validated DetailedAccountReceivableStatementQuery query) {
        return JsonVO.success(service.listDetailedAccountReceivableStatement(query));
    }

    @Resource
    EasyExcelComponent excel;

    @SneakyThrows
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportDetailedAccountReceivableStatementToExcel(DetailedAccountReceivableStatementQuery query) {
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<DetailedAccountReceivableStatementDTO> list = service.listAllDetailedAccountReceivableStatement(query);
        //DetailedAccountReceivableStatementDTO test = new DetailedAccountReceivableStatementDTO();
        //test.setData("测试信息");
        //list.add(test);
        // 生成Excel
        excel.export("现金银行报表", out, DetailedAccountReceivableStatementDTO.class, list);
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
