package com.zeroone.star.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.sale.entity.Sale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 销售单 服务类
 * </p>
 *
 * @author renjian
 * @since 2025-10-26
 */
public interface SaleFormPortService extends IService<Sale> {

    ResponseEntity<byte[]> exportDetailSaleForm(List<String> ids);
    /**
     * 导出销售单简单报表
     * @param ids
     *
     * @return
     */
    ResponseEntity<byte[]> exportSimpleSaleForm(List<String> ids);

    JsonVO<String> importDetailSaleForm(MultipartFile file);
}
