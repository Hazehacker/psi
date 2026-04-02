package com.zeroone.star.storemanagement.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.dto.j2.store.OtherInListInfoDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.project.j2.store.InventoryVerifyApis;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.service.InventoryVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.controller
 * @Author: 高
 * @CreateTime: 2025-10-19 18:49
 * @Description: 库存盘点控制器类
 * @Version: 1.0
 */
@RestController
@RequestMapping("/verify")
@Api(tags = "库存盘点")
@Slf4j
public class InventoryVerifyController implements InventoryVerifyApis {


    @Autowired
    InventoryVerifyService inventoryVerifyService;

    @PostMapping ("/inventoryPlus")
    @ApiOperation(value = "获取盘盈单数据")
    @Override
    public JsonVO<PageDTO<OtherInListInfoDTO>> getInventoryPlus(@RequestBody List<InventoryVerifyDTO> inventoryVerifyList) {
        return JsonVO.success(inventoryVerifyService.getInventoryPlus(inventoryVerifyList));
    }

    @PostMapping("/inventoryReduce")
    @ApiOperation(value = "获取盘亏单数据")
    @Override
    public JsonVO<PageDTO<OtherOutListInfoDTO>> getInventoryReduce(@RequestBody List<InventoryVerifyDTO> inventoryVerifyList) {
        return JsonVO.success(inventoryVerifyService.getInventoryReduce(inventoryVerifyList));
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "导出库存盘点单")
    @Override
    public ResponseEntity<byte[]> exportInventoryVerifyExcel() throws IOException {
        try {
            log.info("开始导出库存盘点单Excel");

            // 调用服务层导出方法
            ByteArrayOutputStream outputStream = inventoryVerifyService.exportInventoryVerifyExcel();
            byte[] excelBytes = outputStream.toByteArray();

            // 生成文件名，包含时间戳避免重复
           // String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = URLEncoder.encode("库存盘点单" + ".xlsx", "UTF-8");

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            log.info("成功导出库存盘点单Excel，文件名: {}, 大小: {} bytes", fileName, excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("导出库存盘点单Excel失败", e);

            // 返回错误信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            String errorMessage = "导出库存盘点单Excel失败: " + e.getMessage();

            return new ResponseEntity<>(errorMessage.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
