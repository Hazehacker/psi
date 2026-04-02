package com.zeroone.star.project.j6.basic_information.product_management;

import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodExportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface ExportDataApis {
    /**
     * 导出数据
     * @param goodsExport 选择导出的数据
     * @return 返回结果
     */
    ResponseEntity<byte[]> exportGoods(@RequestBody GoodExportDTO goodsExport);

    /**
     * 下载模版
     * @return 返回模版
     */
    ResponseEntity<byte[]> downloadTemplate();

}