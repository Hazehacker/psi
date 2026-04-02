package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Frame;
import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.project.dto.j6.basic_information.frame.FrameDTO;
import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author 趣味生煎
 * @version 1.0
 * 描述：供应商对应MapStruct映射接口
 */
@Mapper(componentModel = "spring")
public interface MsFrameMapper {

    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    FrameDTO frameToFrameDto(Frame entity);
    List<FrameDTO> framesToFrameDtos(List<Frame> entity);

    /**
     * 映射实体
     * @param dto SupplierDTO
     * @return 实体
     */
    Frame frameDtoToFrame(FrameDTO dto);


}
