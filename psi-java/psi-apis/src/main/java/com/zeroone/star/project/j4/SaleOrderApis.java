package com.zeroone.star.project.j4;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderInfoDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderImportDTO;
import com.zeroone.star.project.dto.j4.sale.SaleOrderVerifyDTO;
import com.zeroone.star.project.dto.j4.sale.*;
import com.zeroone.star.project.query.j4.sale.SaleOrderQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 描述：销售管理-销售订单接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
public interface SaleOrderApis {

    /**
     * 获取销售订单列表（条件+分页）
     */
    JsonVO<PageDTO<SaleOrderListDTO>> saleOrdersList(SaleOrderQuery query);

    /**
     * 获取指定销售订单详情
     */
    JsonVO<List<List<SaleOrderInfoDTO>>>  saleOrderDetail(String customer, LocalDateTime time, String number);

    /**
     * 获取生成销售单数据
     */
    JsonVO<SaleOrderGenerateDTO> getGenerateSaleOrderData(String saleId);

    /**
     * 获取生成采购订单数据
     */
    JsonVO<PurchaseOrderGenerateDTO> getGeneratePurchaseOrderData(String purchaseId);
    /**
     * 删除销售订单
     */
    JsonVO<Boolean> saleOrderDelete(List<String> ids);

    /**
     * 新增销售订单
     */
    JsonVO<Boolean> saleOrderAdd(SaleOrderListDTO dto);

    /**
     * 修改销售订单
     */
    JsonVO<Boolean> saleOrderChange(SaleOrderListDTO dto);

    /**
     * 审核/反审核销售订单
     */
    JsonVO<Boolean> verifyOrder(SaleOrderVerifyDTO request);




    /**
     * 上传销售订单CSV文件并导入数据
     */
    JsonVO<Boolean> saleOrderImport(MultipartFile file);

    /**
     * 导出简单报表（通过单据编号筛选）
     */
    ResponseEntity<byte[]> saleOrderExportSimple(List<String> billno);

    /**
     * 导出详细报表（通过单据编号筛选）
     */
    ResponseEntity<byte[]>  saleOrderExportDetail(List<String> billno);

}
