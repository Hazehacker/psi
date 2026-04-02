package com.zeroone.star.purchase.controller;
import com.zeroone.star.project.j3.purchase.PurchaseReturnFormApis;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;
import com.zeroone.star.purchase.DO.BreDO;
import com.zeroone.star.purchase.service.IBreBillService;
import com.zeroone.star.purchase.service.IBreInfoService;

import com.zeroone.star.purchase.service.IBreService;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.*;
import com.zeroone.star.purchase.service.impl.BreInfoServiceImpl;
import com.zeroone.star.purchase.service.impl.BreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import com.zeroone.star.project.query.j3.purchase.PurchaseReturnQuery;

import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: 小夏
 * @CreateTime: 2025-10-18
 * @Description: 采购退货单接口功能
 * @Version: 1.0
 */
@RestController
@RequestMapping("purchase-return-form")
@Api(tags = "采购管理-采购退货单")
@RequiredArgsConstructor
public class PurchaseReturnFormController implements PurchaseReturnFormApis {


    @Resource
    private IBreService breService;

    /**
     * 注入采购退货单服务：IBreService
     */
    @Resource
    private IBreService iBreService;

    /**
     * 注入采购退货单核销详情服务：IBreBillService
     *
     */
    @Resource
    private IBreBillService iBreBillService;

    /**
     * 注入采购退货单详情服务：IBreInfoService
     *
     */
    @Resource
    private IBreInfoService iBreInfoService;
    private final BreInfoServiceImpl breInfoService;
    /**
     * 删除采购退货单接口
     *
     * @param pid 需要删除的订单 id
     * @return 删除状态 0表示失败 1表示成功
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除采购退货单 (支持批量)")
    public JsonVO<String> deletePurchaseReturnForm( @RequestBody List<String> pid) {
        return breInfoService.deletePurchaseReturnForm(pid);
    }

    /**
     * 更新采购退货单接口
     *
     * @param purchaseUpdateDTO 前端更新之后的 DTO 对象
     * @return 在数据库的更新状态 0表示失败 1表示成功
     *
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改采购退货单")
    public JsonVO<Integer> updatePurchaseReturnForm(@Validated @RequestBody PurchaseUpdateDTO purchaseUpdateDTO) {
        return breInfoService.updateBreInfoDo(purchaseUpdateDTO);
    }

    /**
     * @param checkOutReturnGoodsDTO 需要核对或者反核对的订单
     * @return 核对或反核对之后的状态 0表示失败 1表示成功
     */
    @ApiOperation(value = "核对/反核对 (支持批量)")
    @PostMapping("/checkout")
    public JsonVO<Integer> checkOut(@RequestBody CheckOutReturnGoodsDTO checkOutReturnGoodsDTO) {
        return breService.checkOut(checkOutReturnGoodsDTO);
    }

    /**

     * @author Hazenix 


    public JsonVO<Integer> checkOut(@RequestBody List<String> pid) {
        return null;
    }

    /**
     * @param purchaseReturnQuery
     * @return
     * @Author: xiaoliu
     * @CreateTime: 2025-10-20
     * 获取采购退货单列表（条件+分页）
     */
    @Override
    @GetMapping("/list")

    @ApiOperation(value= "获取采购退货单列表（条件+分页）")
    public JsonVO<PageDTO<PurchaseReturnListVO>> listPurchaseReturns(PurchaseReturnQuery purchaseReturnQuery) {
        // 1.获取获取采购退货单列表VO并封装为PageDTO
        PageDTO<PurchaseReturnListVO> breVOPageDTO = iBreService.getPurchaseReturnPageDATO(purchaseReturnQuery);

        // 2.返回查询结果
        return JsonVO.success(breVOPageDTO);
    }


    /**
     * @author Hazenix 
     * @CreateTime: 2025-10-20
     * 获取指定采购退货单详情
     * @param id 采购退货单id
     * @return 采购退货单详情
     */
    @Override
    @GetMapping("/detail")
    @ApiOperation(value = "获取指定采购退货单详情")
    @ApiImplicitParam(name= "id",value = "采购退货单id",required = true,example = "b005")
    public JsonVO<PurchaseReturnDetailVO> getPurchaseReturnDetail(@RequestParam String id) {

        // 1.获取指定采购退货单详情VO
        PurchaseReturnDetailVO purchaseReturnDetailVO = iBreInfoService.getPurchaseReturnDetailVO(id);

        // 2.返回查询结果
        return JsonVO.success(purchaseReturnDetailVO);
    }


    /**
     * @param purchaseReturnAddDTO 采购退货单详情
     * @return 采购退货单id
     * @Author:
     * @Author: xiaoliu
     * @CreateTime: 2025-10-20
     * 新增采购退货单
     */
    @Override
    @PostMapping("/add")
    @ApiOperation(value = "新增采购退货单")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "请求头示例值",
                    required = true,
                    paramType = "header",
                    dataType = "string",
                    example = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmFtZU5hbWUiOiLpu5jorqTnu4Tnu4ciLCJ1c2VyX25hbWUiOiJhZG1pbiIsImZyYW1lSWQiOiIwIiwic2NvcGUiOlsiYWxsIl0sImlkIjoyLCJhdmF0YXIiOiIiLCJleHAiOjE3NjIxNTM2NjgsImF1dGhvcml0aWVzIjpbIua1i-ivleinkuiJsiJdLCJqdGkiOiJmNmFhNTdjYy03MjEzLTQ5NmEtOGYwNi0wMmZhYzk5OGJlMzkiLCJjbGllbnRfaWQiOiJwc2ktbWFuYWdlciJ9.JUSUzivCRiXk4eX6tyEMZ47TJYtdaBcRGxpTDffWvlSqVWjIkDBKYLDUo4d4aZjSIhIVjh-CLhYm7G72tSPO_R2RtgkvmfOHXfYqpEdHikic1FhFC6GWapn7FTN3-2n3PsAoU88r8w5MP7SiaWbG_BF6ZTuOwSE-dV3w_9I8E_S8ghKr977M2PGk38V9ekRuw6msP-6M0hDVqu8Z0fwSCPupy8KsQ4j_xA_ktZz88Z0W5jLB8AfkBA9X5WWVwoAgvdvx44p9Z2NvvQPDOH2SC-xZzaOXosqh9ehmEENbKCA-_i0CWXLqCk-xeJy7Zw9-PEHgRvum21iKpjSPitPUFw"
            )
    })
    @Transactional
    public JsonVO<String> savePurchaseReturn(@RequestBody PurchaseReturnAddDTO purchaseReturnAddDTO) {
        // TODO 新增采购退货单
        String breId = iBreService.addPurchaseReturn(purchaseReturnAddDTO);
        return JsonVO.success(breId);
    }

    /**
     * @param purchaseReturnBreAuditDTO 采购退货单审核DTO
     * @return 审核结果
     * @author Hazenix 
     * 批量审核/反审核
     */
    @PutMapping("/approve")
    @ApiOperation(value = "审核/反审核（支持批量）")
    @Override
    public JsonVO<String> approve(@RequestBody PurchaseReturnBreAuditDTO purchaseReturnBreAuditDTO) {
        return breService.approve(purchaseReturnBreAuditDTO);
    }

    @Autowired
    FastDfsClientComponent dfs;

    /**
     * @param file  导入文件
     * @param file 导入文件
     * @return 导入结果
     * @author Hazenix 
     * 导入数据
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入数据")
    @Override
    public JsonVO<String> importData(@RequestParam("file") MultipartFile file) {
        return breService.importData(file);
    }

    /**
     * @param ids 导出采购退货单简单报表id
     * @return 带有导出报表信息的响应实体
     * @author Hazenix 
     * 导出简单报表
     */
    @GetMapping(value = "/bre-export", produces = "application/octet-stream")
    @ApiOperation(value = "导出简单报表")
    @Override
    public ResponseEntity<byte[]> exportBre(@RequestParam("ids") List<String> ids) {
        return breService.exportBre(ids);
    }

    /**

     * 导出详细报表
     * @param ids 导出采购退货单详细报表列表
     * @return 带有导出报表信息的响应实体
     * @author Hazenix 
     * @since 2025-10-20
     * 导出详细报表
     */
    @GetMapping(value = "/breInfo-export", produces = "application/octet-zip")
    @ApiOperation(value = "导出详细报表")
    @Override
    public ResponseEntity<byte[]> exportBreInfo(@RequestParam("ids") List<String> ids) {
        return breService.exportBreInfo(ids);
    }
}
