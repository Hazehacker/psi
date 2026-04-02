package com.zeroone.star.reportmanagement.controller.fundreport;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementDetailDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainDTO;
import com.zeroone.star.project.dto.j5.fundreport.CustomerStatementMainExportDTO;
import com.zeroone.star.project.j5.fundreport.CustomerStatementApi;
import com.zeroone.star.project.query.j5.fundreport.CustomerStatementQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.CustomerStatementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
 * 客户对账单控制器
 *
 * @author toexpl
 * @since 2025/10/21
 */

@RestController
@RequestMapping("/j5-reportmanagement/customer-statement")
@Api(tags = "客户对账单")
public class CustomerStatementController implements CustomerStatementApi {

    @Autowired
    CustomerStatementService customerStatementService;
    @Resource
    EasyExcelComponent easyExcelComponent;

    /**
     * 客户对账单信息查询
     * @param query
     * @return
     */
    @Override
    @ApiOperation(value = "获取报表（条件+分页）")
    @GetMapping
    public JsonVO<PageDTO<CustomerStatementMainDTO>> customerStatementQueryBy(@Validated CustomerStatementQuery query) {

//        目前先将理想化情况都写出

        Page<CustomerStatementMainDTO> page = customerStatementService.listCustomerStatement(query);

        PageDTO<CustomerStatementMainDTO> pageDTO=PageDTO.create(page);

        return JsonVO.success(pageDTO);

    }

    /**
     * 客户对账单导出
     * @param query
     * @return
     */

    @SneakyThrows
    @Override
    @GetMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    public ResponseEntity<byte[]> customerStatementExport(@Validated CustomerStatementQuery query) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        List<CustomerStatementMainDTO> list = customerStatementService.exportCustomerStatement(query);

        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

        WriteSheet mainSheet = EasyExcel.writerSheet("主表")
                .head(CustomerStatementMainExportDTO.class)
                .build();

        List<CustomerStatementMainExportDTO> exportList = list.stream().map(main -> {
            CustomerStatementMainExportDTO dto = new CustomerStatementMainExportDTO();
            BeanUtils.copyProperties(main, dto);
            return dto;
        }).collect(Collectors.toList());

        excelWriter.write(exportList, mainSheet);

        List<CustomerStatementDetailDTO> allDetails = new ArrayList<>();
        for (CustomerStatementMainDTO main : list) {
            if (main.getDetails() != null) {
                allDetails.addAll(main.getDetails());
            }
        }

        WriteSheet detailSheet = EasyExcel.writerSheet("明细")
                .head(CustomerStatementDetailDTO.class)
                .build();

        excelWriter.write(allDetails, detailSheet);
        excelWriter.finish();

        HttpHeaders headers=new HttpHeaders();
        String fileName= URLEncoder.encode("sup"+ LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx",
                StandardCharsets.UTF_8.name());
        headers.setContentDispositionFormData("attachment",fileName);
        headers.setContentType(
                MediaType.parseMediaType("application/vnd.ms-excel"));

//        headers.add(HttpHeaders.CONTENT_TYPE,
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename*=UTF-8''"+fileName);

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}
