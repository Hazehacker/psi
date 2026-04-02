package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商对应MapStruct映射接口
 */
@Mapper(componentModel = "spring")
public interface MsWarehouseMapper {
    /**
     * 单个实体转DTO
     * @param entity 实体
     * @return DTO
     */
    WarehouseNameDTO warehouseToWarehouseNameDto(Warehouse entity);

    /**
     * 列表实体转DTO列表
     * @param entity 实体列表
     * @return DTO列表
     */
    List<WarehouseNameDTO> warehousesToWarehouseNameDtos(List<Warehouse> entity);


}
