package com.zeroone.star.reportmanagement.controller.procurementreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PaymentFormFormDTO;
import com.zeroone.star.project.j5.procurementreport.PaymentFormApi;
import com.zeroone.star.project.query.j5.procurementreport.PaymentFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.PaymentFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;


/**
 * 采购付款表单控制器
 */
@RestController
@RequestMapping("/j5-reportmanagement/procurement-payment-form")
@Api(tags = "采购付款表")
@RequiredArgsConstructor
@Slf4j
public class PaymentFormController implements PaymentFormApi {
    private final PaymentFormService paymentFormService;

    @GetMapping
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<PaymentFormFormDTO>> listPaymentForm(@Validated PaymentFormQuery query) {
        PageDTO<PaymentFormFormDTO> pageDTO = paymentFormService.listPaymentForm(query);
        return JsonVO.success(pageDTO);
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportPaymentFormToExcel(PaymentFormQuery query) {
        try {
            byte[] excelBytes = paymentFormService.exportPaymentFormToExcel(query);

            String originalFilename = "采购付款表" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
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
