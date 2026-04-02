package com.zeroone.star.project.j2.store;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.dto.j2.store.OtherInListInfoDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-19 18:45
 * @Description: 库存盘点接口
 * @Version: 1.0
 */
public interface InventoryVerifyApis {

    /**
     * 获取盘盈单数据
     * @param inventoryVerifyList 查询参数
     * @return  库存盘点数据
     */
    JsonVO<PageDTO<OtherInListInfoDTO>> getInventoryPlus(List<InventoryVerifyDTO> inventoryVerifyList);

    /**
     * 获取盘亏单数据
     * @return 库存盘点参数对象数据
     **/
    JsonVO<PageDTO<OtherOutListInfoDTO>> getInventoryReduce(List<InventoryVerifyDTO> inventoryVerifyList);


    /**
     * 导出库存盘点单
     * @param
     * @return 库存盘点单excel
     */
    ResponseEntity<byte[]> exportInventoryVerifyExcel() throws IOException;

}
