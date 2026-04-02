package com.zeroone.star.reportmanagement.controller.salesreport;


import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesSummaryExportDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesSummaryQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.config.DynamicExcelExporter;
import com.zeroone.star.reportmanagement.service.SalesSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.List;

/*
* 销售报表 - 销售汇总
* @author taohu5564
* @date 2025/10/19
* */
@RestController
@RequestMapping("/j5-reportmanagement/sales-summary")
@Api(tags = "销售汇总表")
@Slf4j
public class SalesSummaryController {

    @Autowired
    private SalesSummaryService salesSummaryService;

    @Resource
    private EasyExcelComponent excel;


    @Autowired
    private DynamicExcelExporter dynamicExcelExporter;

    @GetMapping
    @ApiOperation("获取报表（条件+分页）")
    public JsonVO<PageDTO<SalesSummaryDTO>> listSalesSummary(@Valid SalesSummaryQuery query) {
        log.info("开始销售汇总表的分页查询：{}",query);
        PageDTO<SalesSummaryDTO> pageDTO = salesSummaryService.query(query);
        return JsonVO.success(pageDTO);
    }

    @GetMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation("导出数据")
    public ResponseEntity<byte[]> exportSalesSummaryToExcel(@Valid SalesSummaryQuery query) throws Exception {
        // 1、查询数据(不使用分页查询)
        List<SalesSummaryExportDTO> data = salesSummaryService.queryForExport(query);

        // 2. 生成 Excel 流（直接输出到内存流）
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        dynamicExcelExporter.export("销售汇总报表", out, data, query.getGroupBy());
        byte[] excelBytes = out.toByteArray(); // 获取字节数组

        // 3. 设置响应头（关键修改：直接返回字节数组）
        HttpHeaders headers = new HttpHeaders();
        String fileName = URLEncoder.encode("销售汇总表-" +
                DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx","UTF-8");
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 4. 直接返回字节数组（无需 FastDFS 上传）
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
