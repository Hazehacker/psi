package com.zeroone.star.reportmanagement.controller.procurementreport;

import cn.hutool.core.date.DateTime;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.PurchaseOrderTrackingFormDTO;
import com.zeroone.star.project.j5.procurementreport.PurchaseOrderTrackingFormApi;
import com.zeroone.star.project.query.j5.procurementreport.PurchaseOrderTrackingFormQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.entity.PurchaseByGoodsExcel;
import com.zeroone.star.reportmanagement.entity.PurchaseOrderTrackingFormExcel;
import com.zeroone.star.reportmanagement.service.PurchaseOrderTrackingFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购订单跟踪表控制器类
 * @author yu
 * @date 2025/10/18
 */
@RestController
@RequestMapping("/j5-reportmanagement/purchase-order-tracking-form")
@Api(tags = "采购订单跟踪表")
public class PurchaseOrderTrackingFormController implements PurchaseOrderTrackingFormApi {

    @Autowired
    PurchaseOrderTrackingFormService service;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<PurchaseOrderTrackingFormDTO>> listPurchaseOrderTrackingForm(
            @Validated PurchaseOrderTrackingFormQuery query
    ){
        PageDTO dtoPageDTO = service.listPurchaseOrderTrackingForm(query);

        if (query.getBegintime() != null && query.getEndtime() != null &&
                query.getBegintime().isAfter(query.getEndtime())){
            return JsonVO.fail(null);
        }
        return JsonVO.success(dtoPageDTO);
    }

    @Resource
    EasyExcelComponent excel;

    @GetMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportPurchaseOrderTrackingFormToExcel(
            PurchaseOrderTrackingFormQuery query
    ) {
        if (query.getType() == null){
            return ResponseEntity.badRequest().build();
        }

        List<PurchaseOrderTrackingFormDTO> nestedList = service.listAllForExport(query);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            // 根据type执行不同的导出
            if (query.getType() != null && query.getType() == 1) {

                // Type 1:按商品聚合
                List<PurchaseByGoodsExcel> flatList = new ArrayList<>();

                for (PurchaseOrderTrackingFormDTO parentProduct : nestedList) {
                    for (PurchaseOrderTrackingFormDTO childOrder : parentProduct.getItems()) {

                        PurchaseByGoodsExcel excelRow = new PurchaseByGoodsExcel();

                        // 填充父级商品信息
                        excelRow.setGoodsName(parentProduct.getGoodsName());
                        excelRow.setAttr(parentProduct.getAttr());
                        excelRow.setWarehouseName(parentProduct.getWarehouseName());

                        // 填充子级单据行信息
                        excelRow.setFrameName(childOrder.getFrameName());
                        excelRow.setSupplierName(childOrder.getSupplierName());
                        excelRow.setTime(childOrder.getTime());
                        excelRow.setNumber(childOrder.getNumber());
                        excelRow.setArrival(childOrder.getArrival());
                        excelRow.setUnit(childOrder.getUnit());
                        excelRow.setPrice(childOrder.getPrice());
                        excelRow.setNums(childOrder.getNums());
                        excelRow.setTotal(childOrder.getTotal());
                        excelRow.setItemRemark(childOrder.getItemRemark());
                        excelRow.setUnstockedQuantity(childOrder.getUnstockedQuantity());
                        excelRow.setUnstockedAmount(childOrder.getUnstockedAmount());
                        excelRow.setState(childOrder.getItemState());
                        flatList.add(excelRow);
                    }
                }

                // 使用Type=1的Excel模板导出
                excel.export("采购订单跟踪表(按商品)", byteArrayOutputStream, PurchaseByGoodsExcel.class, flatList);

            } else {

                // Type 0:按单据聚合
                List<PurchaseOrderTrackingFormExcel> flatExcelList = new ArrayList<>();

                // 遍历每一个订单
                for (PurchaseOrderTrackingFormDTO parentOrder : nestedList) {
                    // 遍历这个订单下的每一个商品Item
                    for (PurchaseOrderTrackingFormDTO childProduct : parentOrder.getItems()) { // <-- 统一DTO

                        // 为每一个商品创建一个Excel行
                        PurchaseOrderTrackingFormExcel excelRow = new PurchaseOrderTrackingFormExcel();

                        // 填充父级订单的信息
                        excelRow.setFrameName(parentOrder.getFrameName());
                        excelRow.setSupplierName(parentOrder.getSupplierName());
                        excelRow.setTime(parentOrder.getTime());
                        excelRow.setNumber(parentOrder.getNumber());
                        excelRow.setArrival(parentOrder.getArrival());

                        // 填充子级商品的信息
                        excelRow.setGoodsName(childProduct.getGoodsName());
                        excelRow.setAttr(childProduct.getAttr());
                        excelRow.setWarehouseName(childProduct.getWarehouseName());
                        excelRow.setUnit(childProduct.getUnit());
                        excelRow.setPrice(childProduct.getPrice());
                        excelRow.setNums(childProduct.getNums());
                        excelRow.setTotal(childProduct.getTotal());
                        excelRow.setItemRemark(childProduct.getItemRemark());
                        excelRow.setUnstockedQuantity(childProduct.getUnstockedQuantity());
                        excelRow.setUnstockedAmount(childProduct.getUnstockedAmount());
                        excelRow.setState(childProduct.getItemState()); // <-- [新的、正确的]
                        flatExcelList.add(excelRow);
                    }
                }

                // 使用Type=0的Excel模板导出
                excel.export("采购订单跟踪表(按单据)", byteArrayOutputStream, PurchaseOrderTrackingFormExcel.class, flatExcelList);
            }

            // 返回
            HttpHeaders headers = new HttpHeaders();
            String filename = "rep-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ResponseEntity<byte[]> res = new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
            byteArrayOutputStream.close();
            return res;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
