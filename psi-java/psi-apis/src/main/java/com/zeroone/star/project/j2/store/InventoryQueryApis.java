package com.zeroone.star.project.j2.store;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.InventoryDetailDTO;
import com.zeroone.star.project.dto.j2.store.InventoryListDTO;
import com.zeroone.star.project.query.j2.store.InventoryDetailQuery;
import com.zeroone.star.project.query.j2.store.InventoryQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 18:44
 * @Description: 库存查询接口
 * @Version: 1.0
 */
public interface InventoryQueryApis {
    /**
     * 获取库存列表
     * @param query 查询参数
     * @return 商品列表数据
     */
    JsonVO<PageDTO<InventoryListDTO>> getInventoryList(InventoryQuery query);

    /**
     * 获取指定库存详情
     * @param query
     * @return 库存详情数据
     */
    JsonVO<PageDTO<InventoryDetailDTO>> getInventoryDetail(InventoryDetailQuery query);


    /**
     * 导出库存列表数据
     * @param query
     * @return
     */
    ResponseEntity<byte[]> exportInventoryListExcel(InventoryQuery query);

    /**
     * 导出库存详情数据
     * @param id
     * @return
     */
    ResponseEntity<byte[]> exportInventoryDetailExcel(String id, List<String> warehouseIds);
}
