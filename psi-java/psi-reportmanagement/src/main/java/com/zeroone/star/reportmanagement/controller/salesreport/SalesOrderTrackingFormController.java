package com.zeroone.star.reportmanagement.controller.salesreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesOrderTrackingFormDTO;
import com.zeroone.star.project.j5.salesreport.SalesOrderTrackingFormApi;
import com.zeroone.star.project.query.j5.salesreport.SalesOrderTrackingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.SalesOrderTrackingFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 销售订单跟踪表控制器类
 *
 * @author leyu
 * @date 2025-10-19
 */
@RestController
@RequestMapping("/j5-reportmanagement/sales-order-tracking-form")
@Api(tags = "销售订单跟踪表")
public class SalesOrderTrackingFormController implements SalesOrderTrackingFormApi {

    @Autowired
    private SalesOrderTrackingFormService service;

    @GetMapping
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<SalesOrderTrackingFormDTO>> listSalesOrderTrackingForm(@Validated SalesOrderTrackingFormQuery query) {
        PageDTO dtoPageDTO = service.listAllPage(query);

        if (query.getBegintime() != null && query.getEndtime() != null &&
                query.getBegintime().isAfter(query.getEndtime())){
            return JsonVO.fail(null);
        }
        return JsonVO.success(dtoPageDTO);
    }



    @SneakyThrows
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation("导出数据")
    @Override
    public ResponseEntity<byte[]> exportSalesOrderTrackingFormToExcel(SalesOrderTrackingFormQuery query) {
        // 获取数据，输出为byte array
        byte[] excel = service.exportExcel(query);

        // 设置响应头，响应文件输出给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "SalesOrderTrackingFormReport-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(excel);
    }
}
