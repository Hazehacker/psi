package com.zeroone.star.storemanagement.service;


import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.InventoryDetailDTO;
import com.zeroone.star.project.dto.j2.store.InventoryListDTO;
import com.zeroone.star.project.query.j2.store.InventoryDetailQuery;
import com.zeroone.star.project.query.j2.store.InventoryQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IInventoryQueryService  {

    /**
     * 获取库存列表（条件+分页）
     * @param query 查询条件
     * @return 库存列表分页数据
     */
    PageDTO<InventoryListDTO> getInventoryList(InventoryQuery query);

    /**
     * 获取指定库存详情（条件+分页）
     * @param query 查询条件
     * @return
     */
    PageDTO<InventoryDetailDTO> getInventoryDetail(InventoryDetailQuery query);

    /**
     * 导出库存列表数据Excel
     * @param query
     * @return
     */
    ResponseEntity<byte[]> getListExport(InventoryQuery query);

    /**
     * 导出库存详情数据Excel
     * @param id
     * @return
     */
    ResponseEntity<byte[]> getDetailExport(String id, List<String> warehouseIds);
}
