package com.zeroone.star.reportmanagement.controller.fundreport;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementDetailDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainExportDTO;
import com.zeroone.star.project.j5.fundreport.SupplierStatementApi;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.SupplierStatementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商对账单控制器
 *
 * @author toexpl
 * @since 2025/10/21
 */

@RestController
@RequestMapping("/j5-reportmanagement/supplier-statement")
@Api(tags = "供应商对账单")
public class SupplierStatementController implements SupplierStatementApi {

    @Resource
    SupplierStatementService supplierStatementService;
    @Resource
    EasyExcelComponent easyExcelComponent;

    /**
     * 供应商对账单信息查询
     */
    @Override
    @ApiOperation(value = "获取报表（条件+分页）")
    @GetMapping
    public JsonVO<PageDTO<SupplierStatementMainDTO>> supplierStatementQueryBy(@Validated SupplierStatementQuery query) {

        Page<SupplierStatementMainDTO> page=supplierStatementService.listSupplierStatement(query);

        PageDTO<SupplierStatementMainDTO> pageDTO=PageDTO.create(page);

        return JsonVO.success(pageDTO);
    }

    /**
     * 供应商对账单导出
     */
    @SneakyThrows
    @Override
    @ApiOperation(value = "导出报表")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public ResponseEntity<byte[]> supplierStatementExport(@Validated SupplierStatementQuery query) {

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        List<SupplierStatementMainDTO> list=supplierStatementService.exportSupplierStatement(query);

        ExcelWriter writer= EasyExcel.write(outputStream).build();

        WriteSheet mainSheet=EasyExcel.writerSheet("主表")
                .head(SupplierStatementMainExportDTO.class)
                .build();

        List<SupplierStatementMainExportDTO> mainExportDTOList=list.stream().map(main->{
            SupplierStatementMainExportDTO dto=new SupplierStatementMainExportDTO();
            BeanUtils.copyProperties(main,dto);
            return dto;
        }).collect(Collectors.toList());

        writer.write(mainExportDTOList,mainSheet);

        List<SupplierStatementDetailDTO> detailDTOList=new ArrayList<>();
        for(SupplierStatementMainDTO dto:list){
            if(dto.getDetails()!=null){
                detailDTOList.addAll(dto.getDetails());
            }
        }

        WriteSheet detailSheet=EasyExcel.writerSheet("明细")
                .head(SupplierStatementDetailDTO.class)
                .build();

        writer.write(detailDTOList,detailSheet);
        writer.finish();

        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName= URLEncoder.encode("sup"+ LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx",
                StandardCharsets.UTF_8.name());
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename*=UTF-8''"+fileName);

        return new  ResponseEntity<>(outputStream.toByteArray(),headers, HttpStatus.OK);
    }
}
