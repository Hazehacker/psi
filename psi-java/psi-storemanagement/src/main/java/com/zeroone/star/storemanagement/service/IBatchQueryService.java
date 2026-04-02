package com.zeroone.star.storemanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.BatchDetailDTO;
import com.zeroone.star.project.dto.j2.store.BatchListDTO;
import com.zeroone.star.project.query.j2.store.BatchDetailQuery;
import com.zeroone.star.project.query.j2.store.BatchQuery;
import com.zeroone.star.project.vo.JsonVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.service
 * @Author: 高
 * @CreateTime: 2025-10-24 20:16
 * @Description: TODO
 * @Version: 1.0
 */
public interface IBatchQueryService {

    /**
     * 查询批次列表
     */
    PageDTO<BatchListDTO> listBatch(BatchQuery query);

    /**
     * 获取指定批次详情数据
     * @param batchDetailQuery
     * @return
     */
    PageDTO<BatchDetailDTO> getBatchDetail(BatchDetailQuery batchDetailQuery);

    /**
     * 导出批次列表到Excel
     * @param query 批次查询条件
     * @return Excel文件字节数组
     * @throws IOException IO异常
     */
    ByteArrayOutputStream exportBatchListExcel(BatchQuery query) throws IOException;

    /**
     * 导出批次详情到Excel
     * @param query 批次详情查询条件
     * @return Excel文件字节数组
     * @throws IOException IO异常
     */
    ByteArrayOutputStream exportBatchDetailExcel(BatchDetailQuery query) throws IOException;
}
