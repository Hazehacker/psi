package com.zeroone.star.storemanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.BatchAuditTransferDTO;
import com.zeroone.star.project.dto.j2.store.RemoveTransferDTO;
import com.zeroone.star.project.dto.j2.store.TransferDetailDTO;
import com.zeroone.star.project.dto.j2.store.TransferDetailListDTO;
import com.zeroone.star.project.dto.j2.store.TransferListDTO;
import com.zeroone.star.project.query.j2.store.TransferQuery;
import com.zeroone.star.project.vo.JsonVO;

public interface ITransferService {
    /**
     * 查看调拨单详细信息
     * @param id  调拨单id，然后根据此信息去查并返回调拨单详细信息
     * @return 包含调拨单详细信息的JsonVO对象
     */
    JsonVO<TransferDetailListDTO> detailTransferListAchieve(String id);

    /**
     * 获取调拨单列表
     * @param transferQuery 调拨单列表查询参数
     * @return 包含调拨单列表信息的JsonVO对象
     */
    JsonVO<PageDTO<TransferListDTO>> queryTransferListAchieve(TransferQuery transferQuery);

    /**
     * 新增调拨单
     * @param transferDetailListDTO 调拨单详细信息
     * @return 添加结果
     */
    JsonVO<String> addTransferListAchieve(TransferDetailListDTO transferDetailListDTO);

    /**
     * 修改调拨单
     */
    JsonVO<String> modifyTransfer(TransferDetailDTO dto);

    /**
     * 审核/反审核调拨单（支持批量）
     */
    JsonVO<String> batchAuditTransfer(BatchAuditTransferDTO dto);

    /**
     * 删除调拨单（支持批量）
     */
    JsonVO<String> deleteTransfer(RemoveTransferDTO dto);
}
