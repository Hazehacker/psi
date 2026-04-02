package com.zeroone.star.project.j4;

import com.zeroone.star.project.dto.j4.sale.SaleDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnGenerateDTO;
import com.zeroone.star.project.query.j4.sale.SaleQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 描述：销售管理-销售单接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
public interface SaleFormApis {
    /**
     * 导出销售表简单报表
     */
    ResponseEntity<byte[]> exportSimpleSaleForm(List<String> ids);
    /**
     * 导出销售表详情报表
     */
    ResponseEntity<byte[]> exportDetailSaleForm(List<String> ids);

    /**
     * 导入销售表
     */
    JsonVO<String> exportDetailSaleForm(MultipartFile file);
    /**
     * 新增销售单
     * */
    JsonVO<SaleDTO> addSale(SaleDTO saleDTO);

    /**
     * 修改销售单
     * */
    JsonVO<SaleDTO> updateSale(SaleDTO saleDTO);

    /**
     * 删除销售单(支持批量)
     * */
    JsonVO<Integer> batchDeleteSales(List<String> ids);

    /**
     * 核对/反核对(支持批量)
     * */
    JsonVO<Integer> checkSales(List<String> ids, boolean check);

    /**
     * 审核/反审核(支持批量)
     * */
    JsonVO<Integer> examineSales(List<String> ids, boolean examine);


    @ApiOperation(value = "获取销售单列表（条件+分页）")
    JsonVO<PageDTO<SaleListDTO>> getSaleList(
            @ApiParam(value = "查询参数") SaleQuery query);


    @ApiOperation(value = "获取销售单详情")
    JsonVO<List<SaleDetailDTO>> getSaleDetail(
            @ApiParam(value = "客户") String customer,
            @ApiParam(value = "单据时间") LocalDateTime time,
            @ApiParam(value = "单据编号") String number);


    @ApiOperation(value = "获取生成销售退货单数据")
    JsonVO<SaleReturnGenerateDTO> getGenerateReturnData(
            @ApiParam(value = "销售单ID", required = true) String saleId);
}
