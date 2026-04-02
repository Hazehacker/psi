package com.zeroone.star.basic_information.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Warehouse;
import com.zeroone.star.basic_information.mapper.WarehouseMapper;
import com.zeroone.star.basic_information.service.IWarehouseService;
import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetWarehouseNameServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {
    @Resource
    MsWarehouseMapper ms;

    @Override
    public List<WarehouseNameDTO> getWarehouseNameList() {
        List<Warehouse> warehouses = baseMapper.selectList(null);
        List<WarehouseNameDTO> warehouseNameDtos = ms.warehousesToWarehouseNameDtos(warehouses);
        return new ArrayList<>(warehouseNameDtos);
    }
}
