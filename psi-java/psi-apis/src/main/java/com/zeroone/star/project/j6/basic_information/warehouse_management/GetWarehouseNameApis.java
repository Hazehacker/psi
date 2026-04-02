package com.zeroone.star.project.j6.basic_information.warehouse_management;

import com.zeroone.star.project.dto.j6.basic_information.warehouse_management.WarehouseNameDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * <p>
 * 描述：仓库名称列表获取接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 温昊璇
 * @version 1.0.0
 */
public interface GetWarehouseNameApis {
    // 获取仓库名称列表
    JsonVO<List<WarehouseNameDTO>> getWarehouseNameList();
}
