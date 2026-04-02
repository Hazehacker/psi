package com.zeroone.star.sale.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnGenerateDTO;
import com.zeroone.star.project.query.j4.sale.SaleQuery;
import com.zeroone.star.project.vo.JsonVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleFormService {

    /**
     * 获取销售单列表（条件+分页）
     */
    JsonVO<PageDTO<SaleListDTO>> getSaleList(SaleQuery query);

    /**
     * 获取销售单详情
     */
    JsonVO<List<SaleDetailDTO>> getSaleDetail(String customer, LocalDateTime time, String number); // 返回 List

    /**
     * 获取生成销售退货单数据
     */
    JsonVO<SaleReturnGenerateDTO> getGenerateReturnData(String saleId);

    void addSale(SaleDTO saleDTO);

    void updateSale(SaleDTO saleDTO);

    int batchDeleteSales(List<String> ids);

    Integer checkSales(List<String> ids, boolean check);

    Integer examineSales(List<String> ids, boolean examine);
}
