package com.zeroone.star.storemanagement.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface InventoryVerifyService {
    /**
     * 获取盘盈单数据
     * @param inventoryVerifyList
     * @return
     */
    PageDTO<OtherInListInfoDTO> getInventoryPlus(List<InventoryVerifyDTO> inventoryVerifyList);

    /**
     * 获取盘亏单数据
     * @param inventoryVerifyList
     * @return
     */
    PageDTO<OtherOutListInfoDTO> getInventoryReduce(List<InventoryVerifyDTO> inventoryVerifyList);

    /**
     * 导出库存盘点单
     *
     * @param inventoryVerifyList
     * @return
     */
    ByteArrayOutputStream exportInventoryVerifyExcel() throws IOException;
}
