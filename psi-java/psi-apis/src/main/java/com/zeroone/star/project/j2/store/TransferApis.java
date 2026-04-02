package com.zeroone.star.project.j2.store;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.query.j2.store.TransferQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 18:46
 * @Description: 调拨单接口
 * @Version: 1.0
 */
public interface TransferApis {
    /**
     * 查看调拨单详细信息
     * @param id  调拨单详细id，然后根据此信息去查并返回调拨单详细信息
     * @return 包含调拨单详细信息的JsonVO对象
     */
    JsonVO<TransferDetailListDTO> detailTransferList(String id);

    /**
     * 获取调拨单列表
     * @param transferQuery 调拨单列表查询参数
     * @return 包含调拨单列表信息的JsonVO对象
     */
    JsonVO<PageDTO<TransferListDTO>> queryTransferList(TransferQuery transferQuery);

    /**
     * 新增调拨单
     * @param transferDetailListDTO 调拨单详细信息
     * @return 添加结果
     */
    JsonVO<String> addTransferList(TransferDetailListDTO transferDetailListDTO);

    /**
     * 修改调拨单
     * @param dto 调拨单数据
     * @return 修改结果
     */
    JsonVO<String> modifyTransfer(TransferDetailDTO dto);

    /**
     * 批量审核/反审核调拨单
     * @param dto 批量审核/反审核参数
     * @return 审核/反审核结果
     */
    JsonVO<String> batchAuditTransfer(BatchAuditTransferDTO dto);

    /**
     * 批量删除调拨单
     * @param dto 批量删除参数
     * @return 删除结果
     */
    JsonVO<String> removeTransfer(RemoveTransferDTO dto);

    /**
     * 导入数据
     * @param file 导入文件
     * @return 是否导入成功
     */
    public JsonVO<String> importTransferList(MultipartFile file);


    /**
     * 导出调拨单
     * @param idList 调拨单id列表
     * @return 调拨单excel
     */
    public ResponseEntity<byte[]> exportTransferList(List<String> idList);

    /**
     * 导出调拨单详情
     * @param idList 调拨单id列表
     * @return 调拨单详情excel
     */
    public ResponseEntity<byte[]> exportTransferDetailList(List<String> idList);
}
