package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.InventoryDetailDTO;
import com.zeroone.star.project.dto.j2.store.InventoryDetailExcelDTO;
import com.zeroone.star.project.query.j2.store.InventoryDetailQuery;
import com.zeroone.star.storemanagement.entity.RoomInfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryDetailMapper extends BaseMapper<RoomInfoDO> {
    List<InventoryDetailExcelDTO> getDetailList(@Param("id") String id,
                                                @Param("warehouseIds") List<String> warehouseIds);

    /**
     * 查询库存详情
     * @param page
     * @param query
     * @return
     */
    Page<InventoryDetailDTO> selectInventoryDetailList(Page<InventoryDetailDTO> page, @Param("query")InventoryDetailQuery query);
}
