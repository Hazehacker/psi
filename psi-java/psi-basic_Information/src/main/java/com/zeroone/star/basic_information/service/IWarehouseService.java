package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;

import java.util.List;

public interface IWarehouseService  extends IService<Warehouse> {
    List<WarehouseNameDTO> getWarehouseNameList();
}
