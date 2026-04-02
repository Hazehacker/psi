package com.zeroone.star.reportmanagement.controller.salesreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesReceiptDTO;
import com.zeroone.star.project.j5.salesreport.SalesReceiptApi;
import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;
import com.zeroone.star.project.query.j5.salesreport.SalesReceiptQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.SalesReceiptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 销售收款表控制类
 * @author leyu
 * @date 2025/10/21
 */
@Controller
@RequestMapping("/j5-reportmanagement/sales-receipt")
@Api(tags = "销售收款表")
public class SalesReceiptController implements SalesReceiptApi {

    @Autowired
    private SalesReceiptService service;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @ResponseBody
    @Override
    public JsonVO<PageDTO<SalesReceiptDTO>> listSalesReceipt(@Validated  SalesReceiptQuery query) {
        return JsonVO.success(service.listAllPage(query));
    }

    @SneakyThrows
    @GetMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportSalesReceiptToExcel(SalesReceiptQuery query) {
        // 获取数据，输出为byte array
        byte[] excel = service.exportExcel(query);

        // 设置响应头，响应文件输出给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "SalesReceiptReport-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(excel);
    }


}
