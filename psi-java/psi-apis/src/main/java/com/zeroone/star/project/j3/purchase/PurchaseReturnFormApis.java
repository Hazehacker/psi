package com.zeroone.star.project.j3.purchase;

import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.project.dto.PageDTO;


import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnAddDTO;

import com.zeroone.star.project.query.j3.purchase.PurchaseReturnQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.query.j3.purchase.BreAuditQuery;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description: 采购退货单相关接口
 * @Version: 1.0
 */
public interface PurchaseReturnFormApis {

    /**
     * 删除采购退货单接口
     * @param pid 需要删除的订单 id
     * @return 删除状态 若删除失败 会返回第一个pid
     * */
    JsonVO<String> deletePurchaseReturnForm(@NotEmpty(message = "采购退货单ID列表不能为空") List<String> pid);

    /**
     * 更新采购退货单接口
     * @param purchaseUpdateDTO 前端更新之后的 DTO 对象
     * @return 在数据库的更新状态 0表示失败 1表示成功
     * */
    JsonVO<Integer> updatePurchaseReturnForm(@NotNull(message = "更新数据不能为空") PurchaseUpdateDTO purchaseUpdateDTO);

    /**
     * @param checkOutReturnGoodsDTO 需要核对或者反核对的订单
     * @return 核对或反核对之后的状态 0表示失败 1表示成功
     * */
    JsonVO<Integer> checkOut(@NotEmpty(message = "核对ID列表不能为空")CheckOutReturnGoodsDTO checkOutReturnGoodsDTO);

    /**
     * @Author: xiaoliu
     * @CreateTime: 2025-10-20
     * 获取采购退货单列表（条件+分页）
     * @param purchaseReturnQuery
     * @return 采购退货单列表
     */
    JsonVO<PageDTO<PurchaseReturnListVO>> listPurchaseReturns(@NotNull(message = "查询参数不能为空") PurchaseReturnQuery purchaseReturnQuery);

    /**
     * @Author: xiaoliu
     * @CreateTime: 2025-10-20
     * 获取采购退货单详情
     * @param id 采购退货单id
     * @return 采购退货单详情
     */
    JsonVO<PurchaseReturnDetailVO> getPurchaseReturnDetail(@NotEmpty(message = "采购退货单id不能为空") String id);

    /**
     * @Author: xiaoliu
     * @CreateTime: 2025-10-20
     * 新增采购退货单
     * @param purchaseReturnAddDTO 采购退货单详情
     * @return 采购退货单id
     */
    JsonVO<String> savePurchaseReturn(@NotNull(message = "采购退货单数据不能为空") @Validated PurchaseReturnAddDTO purchaseReturnAddDTO);

    /**
     * @author 斗气化码
     * 批量审核与反审核采购退货单
     * @param purchaseReturnBreAuditDTO 采购退货单审核DTO
     * @return 操作结果
     */
    JsonVO<String> approve(@NotNull(message = "采购退货单审核DTO不能为空") PurchaseReturnBreAuditDTO purchaseReturnBreAuditDTO);

    /**
     * @author 斗气化码
     * 批量导入采购退货单
     * @param file 导入文件
     * @return 导入结果
     */
    JsonVO<String> importData(@NotNull(message = "导入文件不能为空") MultipartFile file);

    /**
     * @author 斗气化码
     * 批量导出采购退货单简单报表
     * @param ids 导出采购退货单简单报表id
     * @return 采购退货单简单信息
     */
    ResponseEntity<byte[]> exportBre(@NotEmpty(message = "导出数据id不能为空") List<String> ids);

    /**
     * @author 斗气化码
     * 批量导出采购退货单详细报表
     * @Param ids 采购退货单id
     * @return 采购退货单详细信息
     */
    ResponseEntity<byte[]> exportBreInfo(@NotEmpty(message = "导出数据id不能为空") List<String> ids);
}
