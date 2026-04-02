package com.zeroone.star.fund.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptDetailDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptListDTO;
import com.zeroone.star.project.query.j4.fund.FundReceiptListQuery;
import com.zeroone.star.project.vo.JsonVO;

import javax.validation.constraints.NotNull;

public interface FundReceiptFormService {
    /**
     * 查询收款单详情
     * @param id
     * @return
     */
    JsonVO<FundReceiptDetailDTO> selectFundReceiptDetail(@NotNull String id);

    /**
     * 查询收款单列表（条件 + 分页）
     * @return
     */
    JsonVO<PageDTO<FundReceiptListDTO>> pageList(FundReceiptListQuery query);
}
