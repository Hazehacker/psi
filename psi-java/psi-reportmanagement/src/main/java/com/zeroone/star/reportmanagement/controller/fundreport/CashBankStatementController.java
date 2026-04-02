package com.zeroone.star.reportmanagement.controller.fundreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.j5.fundreport.CashBankStatementApi;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.ICashBankStatementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
 * 现金银行报表控制器类
 */
@Slf4j
@RestController
@RequestMapping("/j5-reportmanagement/cash-bank-statement")
@Api(tags = "现金银行报表")
public class CashBankStatementController implements CashBankStatementApi {

    @Resource
    ICashBankStatementService service;

    @GetMapping()
    @ApiOperation(value = "获取报表(条件+分页)")
    @Override
    public JsonVO<PageDTO<CashBankStatementDTO>> listCashBankStatement(CashBankStatementQuery query) {
        return JsonVO.success(service.listCashBankStatement(query));
    }

    @Resource
    EasyExcelComponent excel;

    @SneakyThrows
    @GetMapping(value =  "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportCashBankStatementToExcel(@Validated CashBankStatementQuery query) {
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<CashBankStatementDTO> list = service.listAllCashBankStatement(query);
        //测试数据
        //CashBankStatementDTO test = new CashBankStatementDTO();
        //test.setName("张三");
        //list.add(test);
        // 生成Excel
        excel.export("现金银行报表", out, CashBankStatementDTO.class, list);
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
