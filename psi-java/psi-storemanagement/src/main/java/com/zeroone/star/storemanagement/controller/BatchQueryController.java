package com.zeroone.star.storemanagement.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.BatchDetailDTO;
import com.zeroone.star.project.dto.j2.store.BatchListDTO;
import com.zeroone.star.project.j2.store.BatchQueryApis;
import com.zeroone.star.project.query.j2.store.BatchDetailQuery;
import com.zeroone.star.project.query.j2.store.BatchQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.service.IBatchQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.controller
 * @Author: 高
 * @CreateTime: 2025-10-19 18:50
 * @Description: 批次查询控制器类
 * @Version: 1.0
 */
@RestController
@RequestMapping("/batch")
@Api(tags = "批次查询")
@Slf4j
public class BatchQueryController implements BatchQueryApis {

    @Resource
    private IBatchQueryService batchService;

    @GetMapping("/list")
    @ApiOperation(value = "获取批次列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<BatchListDTO>> listBatch(BatchQuery query) {
        return JsonVO.success( batchService.listBatch(query));
    }


    /**
     * 获取指定批次详情数据
     * @param batchDetailQuery
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取指定批次详情数据")
    @Override
    public JsonVO<PageDTO<BatchDetailDTO>> getBatchDetail(BatchDetailQuery batchDetailQuery) {
        return JsonVO.success(batchService.getBatchDetail(batchDetailQuery));
    }


    /**
     * 导出批次列表数据为Excel
     * @param query 批次查询条件
     * @return Excel文件的ResponseEntity
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出批次数据")
    @Override
    public ResponseEntity<byte[]> exportBatchListExcel(BatchQuery query) {
        try {
            log.info("开始导出批次列表Excel，查询条件: {}", query);

            // 调用服务层导出方法
            ByteArrayOutputStream outputStream = batchService.exportBatchListExcel(query);
            byte[] excelBytes = outputStream.toByteArray();

            // 生成文件名，包含时间戳避免重复
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = URLEncoder.encode("批次列表_" + timestamp + ".xlsx", "UTF-8");

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            log.info("成功导出批次列表Excel，文件名: {}, 大小: {} bytes", fileName, excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("导出批次列表Excel失败", e);

            // 返回错误信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            String errorMessage = "导出批次列表Excel失败: " + e.getMessage();

            return new ResponseEntity<>(errorMessage.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 导出批次详情数据为Excel
     * @param query 批次详情查询条件
     * @return Excel文件的ResponseEntity
     */
    @GetMapping("/exportDetail")
    @ApiOperation(value = "导出批次详情数据")
    @Override
    public ResponseEntity<byte[]> exportBatchDetailExcel(BatchDetailQuery query) {
        try {
            log.info("开始导出批次详情Excel，批次ID: {}", query.getPid());

            // 参数校验
            if (query.getPid() == null || query.getPid().stream().allMatch(pid -> pid == null || pid.trim().isEmpty())) {
                log.warn("批次ID为空，无法导出");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity<>("批次ID不能为空".getBytes(), headers, HttpStatus.BAD_REQUEST);
            }

            // 调用服务层导出方法
            ByteArrayOutputStream outputStream = batchService.exportBatchDetailExcel(query);
            byte[] excelBytes = outputStream.toByteArray();

            // 生成文件名，包含批次ID和时间戳
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = URLEncoder.encode("批次详情_" + query.getPid() + "_" + timestamp + ".xlsx", "UTF-8");

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            log.info("成功导出批次详情Excel，文件名: {}, 大小: {} bytes", fileName, excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("导出批次详情Excel失败", e);
            // 返回错误信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            String errorMessage = "导出批次详情Excel失败: " + e.getMessage();

            return new ResponseEntity<>(errorMessage.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
