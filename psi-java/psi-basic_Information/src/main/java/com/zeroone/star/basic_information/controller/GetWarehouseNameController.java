package com.zeroone.star.basic_information.controller;

import com.zeroone.star.basic_information.service.IWarehouseService;
import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;
import com.zeroone.star.project.j6.basic_information.warehouse_management.GetWarehouseNameApis;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/basic_information/warehouse")
public class GetWarehouseNameController implements GetWarehouseNameApis {
    @Resource
    private IWarehouseService warehouseService;
    @Override
    @GetMapping("/getWarehouseNameList")
    public JsonVO<List<WarehouseNameDTO>> getWarehouseNameList() {
        try {
            List<WarehouseNameDTO> warehouseNameList = warehouseService.getWarehouseNameList();
            return JsonVO.success(warehouseNameList);
        } catch (Exception e) {
            return JsonVO.fail(null);
        }
    }
}
