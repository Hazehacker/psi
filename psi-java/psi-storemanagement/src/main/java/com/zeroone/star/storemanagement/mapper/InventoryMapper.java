package com.zeroone.star.storemanagement.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.AttrStockDTO;
import com.zeroone.star.project.dto.j2.store.InventoryListDTO;
import com.zeroone.star.project.dto.j2.store.WarehouseStockDTO;
import com.zeroone.star.project.query.j2.store.InventoryQuery;
import com.zeroone.star.storemanagement.entity.GoodsDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InventoryMapper extends BaseMapper<GoodsDO> {
    /**
     * 查询商品的基本信息（分页）
     * @param page 分页参数
     * @param query 查询参数
     * @return
     */
    Page<InventoryListDTO> selectInventoryBaseList(Page<InventoryListDTO> page, @Param("query")InventoryQuery query);

    /**
     * 查询商品辅助属性基本信息
     * @param goodsIds 商品ID列表
     * @return
     */
    List<AttrStockDTO> selectAttrStockByGoodsIds(@Param("goodsIds") List<String> goodsIds);

    /**
     * 查询商品仓库库存信息
     * @param goodsIds 商品ID列表
     * @param warehouseIds 仓库ID列表
     * @return
     */
    List<WarehouseStockDTO> selectWarehouseStockByGoodsIds(
            @Param("goodsIds") List<String> goodsIds,
            @Param("warehouseIds") List<String> warehouseIds);

    /**
     * 查询子类别ID
     * @param parentId
     * @return
     */
    List<String> selectChildCategoryIds(@Param("parentId") String parentId);
}
