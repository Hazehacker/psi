package com.zeroone.star.sale.controller;

import com.zeroone.star.project.dto.j4.sale.SaleDTO;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnGenerateDTO;
import com.zeroone.star.project.dto.j4.sale.SaleInfoDTO;
import com.zeroone.star.project.j4.SaleFormApis;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.query.j4.sale.SaleQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.service.SaleFormService;
import com.zeroone.star.sale.service.SaleFormService;
import com.zeroone.star.sale.service.SaleFormPortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.zeroone.star.project.j4.SaleFormApis;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 描述：销售管理-销售单控制器类
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
@RestController
@RequestMapping("/sale/form")
@Api(tags = "销售单")
@Validated
public class SaleFormController implements SaleFormApis {

    @Resource
    private SaleFormService saleFormService;

    @Resource
    private SaleFormPortService SaleFormPortService;

    /**
     * 获取销售单列表（条件+分页）
     */
    @GetMapping("/getSaleList")
    @ApiOperation(value = "获取销售单列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<SaleListDTO>> getSaleList(
            @ApiParam(value = "查询参数") SaleQuery query) { // 移除 @Valid
        return saleFormService.getSaleList(query);
    }

    /**
     * 新增销售单
     * */
    @PostMapping("/addSale")
    @ApiOperation(value = "新增销售单")
    @Override
    public JsonVO<SaleDTO> addSale(
            @ApiParam(value = "销售单数据 包含主表和详情表")
            @RequestBody SaleDTO saleDTO) {
        validateSaleDTO(saleDTO,false);
        saleFormService.addSale(saleDTO);
        return JsonVO.success(saleDTO);
    }

    /**
     * 校验销售单数据
     * */

    private void validateSaleDTO(SaleDTO saleDTO, boolean isUpdate){
        if(saleDTO == null){
            throw new IllegalArgumentException("销售单数据不能为空");
        }
        /// 修改销售单时,先对id进行判空
        if(saleDTO.getId() == null && isUpdate){
            throw new IllegalArgumentException("修改销售单时,销售单id不能为空");
        }
        if(saleDTO.getFrame()==null){
            throw new IllegalArgumentException("所属组织不能为空");
        }
        if(saleDTO.getCustomer()==null){
            throw new IllegalArgumentException("客户不能为空");
        }
        if(saleDTO.getTime()==null){
            throw new IllegalArgumentException("单据时间不能为空");
        }
        if(saleDTO.getNumber()==null){
            throw new IllegalArgumentException("单据编号不能为空");
        }
        if(saleDTO.getCost()==null){
            throw new IllegalArgumentException("单据费用不能为空");
        }
        if(saleDTO.getExamine()==null){
            throw new IllegalArgumentException("审核状态不能为空");
        }
        if(saleDTO.getNucleus()==null){
            throw new IllegalArgumentException("核销状态不能为空");
        }
        if(saleDTO.getCse()==null){
            throw new IllegalArgumentException("费用状态不能为空");
        }
        if(saleDTO.getInvoice()==null){
            throw new IllegalArgumentException("发票状态不能为空");
        }
        if(saleDTO.getCheck()==null){
            throw new IllegalArgumentException("核对状态不能为空");
        }
        if(saleDTO.getUser()==null){
            throw new IllegalArgumentException("制单人不能为空");
        }
        if(saleDTO.getItems()==null || saleDTO.getItems().isEmpty()){
            throw new IllegalArgumentException("销售单详情不能为空");
        }
        for (SaleInfoDTO item : saleDTO.getItems()) {
            if(item.getGoods()==null){
                throw new IllegalArgumentException("销售单详情所属商品不能为空");
            }
            if(item.getUnit()==null){
                throw new IllegalArgumentException("销售单详情单位不能为空");
            }
            if(item.getPrice()==null){
                throw new IllegalArgumentException("销售单详情单价不能为空");
            }
            if(item.getNums()==null){
                throw new IllegalArgumentException("销售单详情数量不能为空");
            }
        }
    }
    @GetMapping(value = "/export-simple")
    @ApiOperation(value = "导出销售单简单报表")
    @Override
    public ResponseEntity<byte[]> exportSimpleSaleForm(
            @ApiParam(value = "销售单ID列表",required = true,example = "1,2,3")  @RequestParam("ids") List<String> ids) {

        return  SaleFormPortService.exportSimpleSaleForm(ids);
    }

    @GetMapping(value = "/export-detail", produces = "application/octet-stream")
    @ApiOperation(value = "导出销售单详情报表")
    @Override
    public ResponseEntity<byte[]> exportDetailSaleForm(
            @ApiParam(value = "销售单ID列表",required = true)  @RequestParam("ids") List<String> ids) {

        return SaleFormPortService.exportDetailSaleForm(ids);
    }

    @Override
    @PostMapping(value = "/import" , consumes = "multipart/form-data")
    @ApiOperation(value = "导入销售单详细报表")
    public JsonVO<String> exportDetailSaleForm(
            @ApiParam(value = "导入文件excel",required = true) MultipartFile file) {
        return SaleFormPortService.importDetailSaleForm(file);
    }


    /**
     * 修改销售单
     * */
    @PutMapping("/updateSale")
    @ApiOperation(value = "修改销售单")
    @Override
    public JsonVO<SaleDTO> updateSale(
            @ApiParam(value = "销售单数据 必须包含id")
            @RequestBody SaleDTO saleDTO) {
        /// 校验销售单数据
        validateSaleDTO(saleDTO,true);
        saleFormService.updateSale(saleDTO);
        return JsonVO.success(saleDTO);
    }

    /**
     * 删除销售单(支持批量)
     * */

    @DeleteMapping("/deleteSales")
    @ApiOperation(value = "删除销售单(支持批量)")
    @Override
    public JsonVO<Integer> batchDeleteSales(
            @ApiParam(value = "要删除的销售单id列表",required = true,example = "[1,2,3]")
            @RequestBody List<String> ids) {
        if(ids == null || ids.isEmpty()){
            throw new IllegalArgumentException("要删除的销售单id列表不能为空");
        }
        int count = saleFormService.batchDeleteSales(ids);
        return JsonVO.success(count);
    }

    /**
     * 核对/反核对(支持批量)
     * */
    @PostMapping("/check")
    @ApiOperation(value = "核对/反核对(支持批量)")
    @Override
    public JsonVO<Integer> checkSales(
            @RequestBody List<String> ids,
            @RequestParam boolean check) {
        if(ids == null || ids.isEmpty()){
            throw new IllegalArgumentException("要核对或反核对的销售单id列表不能为空");
        }
        Integer count = saleFormService.checkSales(ids,check);
        return JsonVO.success(count);
    }

    /**
     * 审核/反审核(支持批量)
     * */
    @PostMapping("/examine")
    @ApiOperation(value = "审核/反审核(支持批量)")
    @Override
    public JsonVO<Integer> examineSales(
            @RequestBody List<String> ids,
            @RequestParam boolean examine) {
        if(ids == null || ids.isEmpty()){
            throw new IllegalArgumentException("要审核或反审核的销售单id列表不能为空");
        }
        Integer count = saleFormService.examineSales(ids,examine);
        return JsonVO.success(count);
    }

    /**
     * 获取销售单详情
     */
    @GetMapping("/getSaleDetail")
    @ApiOperation(value = "获取销售单详情")
    @Override
    public JsonVO<List<SaleDetailDTO>> getSaleDetail(
            @ApiParam(value = "客户") @RequestParam(required = false) String customer,
            @ApiParam(value = "单据时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time,
            @ApiParam(value = "单据编号") @RequestParam(required = false) String number) {
        return saleFormService.getSaleDetail(customer, time, number);
    }

    /**
     * 获取生成销售退货单数据
     */
    @GetMapping("/getGenerateReturnData")
    @ApiOperation(value = "获取生成销售退货单数据")
    @Override
    public JsonVO<SaleReturnGenerateDTO> getGenerateReturnData(
            @ApiParam(value = "销售单ID", required = true) @RequestParam String saleId) {
        return saleFormService.getGenerateReturnData(saleId);
    }
}