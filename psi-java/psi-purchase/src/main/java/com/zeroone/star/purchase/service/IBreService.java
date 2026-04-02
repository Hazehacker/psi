package com.zeroone.star.purchase.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnAddDTO;
import com.zeroone.star.project.query.j3.purchase.PurchaseReturnQuery;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;

import com.zeroone.star.project.dto.j3.purchase.CheckOutReturnGoodsDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.DO.BreDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnBreAuditDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 采购退货单 服务类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
public interface IBreService extends IService<BreDO> {



    /**
     * 导出采购退货单详细报表
     * @param ids 采购退货单id
     * @return 带有导出报表信息的响应实体
     * @author Hazenix 
     * @date 2025/10/27
     */
    ResponseEntity<byte[]> exportBreInfo(List<String> ids);

    /**
     * @author Hazenix 
     * 获取获取采购退货单列表VO并封装为PageDTO
     * @param purchaseReturnQuery
     * @return
     */
    PageDTO<PurchaseReturnListVO> getPurchaseReturnPageDATO(PurchaseReturnQuery purchaseReturnQuery);


    /**
     * @author Hazenix 
     * 新增采购退货单
     * @param purchaseReturnAddDTO
     * @return
     */
    String addPurchaseReturn(PurchaseReturnAddDTO purchaseReturnAddDTO);

    /**
     * 核对反核对接口
     */
    JsonVO<Integer> checkOut(CheckOutReturnGoodsDTO checkOutReturnGoodsDTO);

    /**
     * @param purchaseReturnBreAuditDTO 采购退货单审核DTO
     * @author Hazenix 
     * 批量审核/反审核
     */
    JsonVO<String> approve(PurchaseReturnBreAuditDTO purchaseReturnBreAuditDTO);

    /**
     * @param file 导入文件
     * @author Hazenix 
     * 导入数据
     */
    JsonVO<String> importData(MultipartFile file);

    /**
     * @param ids 导出采购退货单简单报表id
     * @author Hazenix 
     * 导出简单报表
     */
    ResponseEntity<byte[]> exportBre(List<String> ids);

}

