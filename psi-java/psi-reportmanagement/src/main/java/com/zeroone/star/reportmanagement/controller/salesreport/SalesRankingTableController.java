package com.zeroone.star.reportmanagement.controller.salesreport;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;
import com.zeroone.star.project.j5.salesreport.SalesRankingTableApi;
import com.zeroone.star.project.query.j5.salesreport.NoRequiredSalesRankingTableQuery;
import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.salesreport.SalesRankingTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/j5-reportmanagement/sales-ranking-table")
@Api(tags = "销售排行表")
public class SalesRankingTableController implements SalesRankingTableApi {

    @Autowired
    private SalesRankingTableService salesRankingTableService;

    @Autowired
    private EasyExcelComponent excelUtiles;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<SalesRankingTableDTO>> listSalesRankingTableDTO(
            @Validated SalesRankingTableQury query) {


        String beginTime = query.getBegintime();
        String endTime = query.getEndtime();
        // 定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        //校验开始时间格式
        if (beginTime != null && !beginTime.trim().isEmpty()) {
            try {
                sdf.parse(beginTime); // 尝试解析
            } catch (ParseException e) {
                throw new IllegalArgumentException("开始时间格式错误，支持：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss");
            }
        }

        //校验结束时间格式
        if (endTime != null && !endTime.trim().isEmpty()) {
            try {
                sdf.parse(endTime); // 尝试解析，失败则抛异常
            } catch (ParseException e) {
                throw new IllegalArgumentException("结束时间格式错误，支持：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss");
            }
        }

        //校验开始时间 <= 结束时间
        if (beginTime != null && !beginTime.trim().isEmpty()
                && endTime != null && !endTime.trim().isEmpty()) {
            try {
                Date begin = sdf.parse(beginTime);
                Date end = sdf.parse(endTime);
                Assert.isTrue(begin.getTime() <= end.getTime(), "开始时间不能晚于结束时间");
            } catch (ParseException e) {
                throw new IllegalArgumentException("日期解析异常");
            }
        }

        PageDTO<SalesRankingTableDTO> salesRankingTableDTOPage = salesRankingTableService.listSalesRankingTable(query);

        return JsonVO.success(salesRankingTableDTOPage);

    }
    @GetMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportListSalesRankingTableToExcel(NoRequiredSalesRankingTableQuery query) {
        PageDTO<SalesRankingTableDTO> salesRankingTableDTOPage = salesRankingTableService.listSalesRankingTable(query);
        List<SalesRankingTableDTO> salesRankingTableDTOPageRows = salesRankingTableDTOPage.getRows();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 生成Excel
        try {
            excelUtiles.export("销售排行表",outStream,SalesRankingTableDTO.class,salesRankingTableDTOPageRows);
        }catch (Exception e){
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        String filename = "salesRanking-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return  new ResponseEntity<>(outStream.toByteArray(), headers, HttpStatus.CREATED);
    }
}
