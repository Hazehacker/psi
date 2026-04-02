package com.zeroone.star.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.dto.j4.sale.SaleReturnIdsDTO;
import com.zeroone.star.project.query.j4.sale.SaleReturnCheckQuery;
import com.zeroone.star.project.vo.JsonVO;

public interface SaleReturnFormplusService extends IService<SaleReturnCheckQuery> {
    JsonVO<Integer> check(SaleReturnIdsDTO saleReturnIdsDTO);

    JsonVO<Integer> uncheck(SaleReturnIdsDTO saleReturnIdsDTO);

    JsonVO<Integer> examine(SaleReturnIdsDTO saleReturnIdsDTO);

    JsonVO<Integer> unexamine(SaleReturnIdsDTO saleReturnIdsDTO);
}
