package com.zeroone.star.reportmanagement.controller.procurementreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseRankingFormDTO;
import com.zeroone.star.project.j5.procurementreport.PurchaseRankingFormApi;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseRankingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.PurchaseRankingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 采购订单排行表控制器类
 * @author impovo123
 * @date 2025/10/20
 */
@RestController
@RequestMapping("/j5-reportmanagement/purchase-ranking-form")
@Api(tags = "采购排行表")
public class PurchaseRankingFormController implements PurchaseRankingFormApi {
    @Resource
    PurchaseRankingService purchaseRankingService;
    @Resource
    EasyExcelComponent excel;


    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<PurchaseRankingFormDTO>> listPurchaseRankingForm(PurchaseRankingFormQuery query) {
        return JsonVO.success(purchaseRankingService.listPurchaseRankingForm(query));
    }

    @SneakyThrows
    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportPurchaseRankingFormToExcel(PurchaseRankingFormQuery query) {
        PageDTO<PurchaseRankingFormDTO> rankingFormDTOPageDTO = purchaseRankingService.listPurchaseRankingForm(query);
        List<PurchaseRankingFormDTO> rows = rankingFormDTOPageDTO.getRows();
        //定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //生成Excel
        excel.export("采购排行表", out, PurchaseRankingFormDTO.class, rows);

        //响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "purchase-ranking-form-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();

        return res;
    }
}
