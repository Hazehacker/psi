package com.zeroone.star.project.j2.store;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.BatchDetailDTO;
import com.zeroone.star.project.query.j2.store.BatchDetailQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.dto.j2.store.BatchListDTO;
import com.zeroone.star.project.query.j2.store.BatchQuery;
import org.springframework.http.ResponseEntity;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 18:44
 * @Description: 批次查询接口
 * @Version: 1.0
 */
public interface BatchQueryApis {
    /**
     * 获取批次列表
     * @param query
     * @return 批次列表数据
     */
    JsonVO<PageDTO<BatchListDTO>> listBatch(BatchQuery query);

    /**
     * 获取指定批次详情数据
     * @param batchDetailQuery
     * @return
     */
    JsonVO<PageDTO<BatchDetailDTO>> getBatchDetail(BatchDetailQuery batchDetailQuery);
    /**
     * 导出批次数据
     * @param query 查询参数
     */
    ResponseEntity<byte[]> exportBatchListExcel(BatchQuery query);

    /**
     * 导出批次详情数据
     * @param query 批次查询详情参数
     */
    ResponseEntity<byte[]> exportBatchDetailExcel(BatchDetailQuery query);
}
