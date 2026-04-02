package com.zeroone.star.reportmanagement.controller.fundreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.ProfitReportDTO;
import com.zeroone.star.project.j5.fundreport.ProfitApi;
import com.zeroone.star.project.query.j5.fundreport.ProfitQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.ProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

@RestController
@RequestMapping("/j5-reportmanagement/profit")
@Api(tags = "利润表")
@RequiredArgsConstructor
@Slf4j
public class ProfitController implements ProfitApi {
    private final ProfitService profitService;

    @GetMapping
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<ProfitReportDTO>> listProfitReportForm(ProfitQuery query) {
        return JsonVO.success(profitService.listProfitReportForm(query));
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportProfitReportToExcel(ProfitQuery query) {
        try {
            byte[] excelBytes = profitService.exportProfitReportToExcel(query);

            String originalFilename = "利润表" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
            // URL 编码，解决中文乱码
            String encodedFilename = URLEncoder.encode(originalFilename, "UTF-8")
                    .replaceAll("\\+", "%20");
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 构建 content-disposition
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(encodedFilename)  // Spring 会自动处理成 filename* 格式
                            .build()
            );
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出Excel失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
