package com.zeroone.star.storemanagement.mapper;

import com.zeroone.star.project.dto.j2.store.InventoryVerifyListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InventoryVerifyMapper {

    List<InventoryVerifyListDTO> selectInventoryVerifyListDTO();
}
