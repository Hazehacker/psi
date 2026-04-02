package com.zeroone.star.sale.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnInfoDTO;
import com.zeroone.star.project.dto.j4.sale.SalesReturnOrderDTO;
import com.zeroone.star.project.query.j4.sale.SaleReturnQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface SaleReturnFormService {
    PageDTO<SaleReturnDTO> pageSaleReturn(SaleReturnQuery saleReturnQuery);

    List<SaleReturnInfoDTO> listSaleReturnInfo(String id);

    /**
     * 新增销售退货单
     */
    boolean addSalesReturnOrder(SalesReturnOrderDTO dto);

    /**
     * 修改销售退货单
     */
    boolean updateSalesReturnOrder(SalesReturnOrderDTO dto);

    /**
     * 删除销售退货单
     */
    boolean deleteSalesReturnOrder(String id);

    String importFund(MultipartFile file);

    String exportDetailFund();
}
