package com.zeroone.star.reportmanagement.controller.salesreport;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProductDetail;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDetailedDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitExcelDTO;
import com.zeroone.star.project.j5.salesreport.SalesProfitApi;
import com.zeroone.star.project.query.j5.salesreport.SalesQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.reportmanagement.service.SalesProfitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售利润表控制器类
 * @author 言语
 * @date 2025/10/20
 */
@RestController
@RequestMapping("/j5-reportmanagement/sales-profit")
@Api(tags = "销售利润表")
public class SalesProfitController implements SalesProfitApi {

    @Autowired
    private SalesProfitService salesProfitService;

    @GetMapping()
    @ApiOperation(value = "获取报表（条件+分页）")
    @Override
    public JsonVO<PageDTO<Object>> listSalesProfitForm(@Validated SalesQuery query) {
        return JsonVO.success(salesProfitService.query(query));
    }

    @Resource
    EasyExcelComponent excel;

    @GetMapping(value = "/export")
    @ApiOperation(value = "导出数据")
    @Override
    public ResponseEntity<byte[]> exportSalesProfitFormToExcel(@Validated SalesQuery query) throws IOException {
        PageDTO<Object> pageDTO = salesProfitService.query(query);
        List<Object> rows = pageDTO.getRows();
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        if (query.getData().equals("隐藏明细")) {
            List<SalesProfitDTO> dtoList = rows.stream()
                    .filter(obj -> obj instanceof SalesProfitDTO)
                    .map(obj -> (SalesProfitDTO) obj)
                    .collect(Collectors.toList());
            excel.export("销售利润表", out, SalesProfitDTO.class, dtoList);
        } else if (query.getData().equals("显示明细")) {
            // 重新组织数据结构以匹配附件格式
            List<SalesProfitExcelDTO> excelDataList = organizeExcelData(rows);
            excel.export("销售利润表", out, SalesProfitExcelDTO.class, excelDataList);
        }
        // 响应给前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "rep-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        out.close();
        return res;
    }

    /**
     * 重新组织数据以匹配Excel格式
     */
    private List<SalesProfitExcelDTO> organizeExcelData(List<Object> rows) {
        List<SalesProfitExcelDTO> result = new ArrayList<>();

        for (Object row : rows) {
            if (row instanceof SalesProfitDetailedDTO) {
                SalesProfitDetailedDTO detailedDTO = (SalesProfitDetailedDTO) row;

                // 添加主行（单据信息）
                SalesProfitExcelDTO mainRow = createMainRow(detailedDTO);
                result.add(mainRow);

                // 添加明细行（商品信息）
                if (detailedDTO.getList() != null && !detailedDTO.getList().isEmpty()) {
                    for (SalesProductDetail productDetail : detailedDTO.getList()) {
                        SalesProfitExcelDTO detailRow = createDetailRow(detailedDTO, productDetail);
                        result.add(detailRow);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 创建主行（单据信息）
     */
    private SalesProfitExcelDTO createMainRow(SalesProfitDetailedDTO detailedDTO) {
        SalesProfitExcelDTO mainRow = new SalesProfitExcelDTO();

        // 设置单据级别信息
        mainRow.setType(detailedDTO.getType());
        mainRow.setFrame(detailedDTO.getFrame());
        mainRow.setCustomer(detailedDTO.getCustomer());
        mainRow.setTime(detailedDTO.getTime());
        mainRow.setNumber(detailedDTO.getNumber());

        // 单据金额相关字段
        mainRow.setTotal(detailedDTO.getTotal());
        mainRow.setDiscount(detailedDTO.getDiscount());
        mainRow.setActual(detailedDTO.getActual());

        // 成本利润相关字段（单据级别）
        mainRow.setBuy(detailedDTO.getBuy());
        mainRow.setGrossProfit(detailedDTO.getGrossProfit());
        mainRow.setGrossProfitMargin(detailedDTO.getGrossProfitMargin());
        mainRow.setCost(detailedDTO.getCost());
        mainRow.setNetProfit(detailedDTO.getNetProfit());
        mainRow.setNetProfitMargin(detailedDTO.getNetProfitMargin());

        // 核销相关字段
        mainRow.setMoney(detailedDTO.getMoney());
        mainRow.setNucleus(convertNucleusStatus(detailedDTO.getNucleus()));

        // 人员字段
        mainRow.setUser(detailedDTO.getUser());
        mainRow.setPeople(detailedDTO.getPeople());
        mainRow.setData(detailedDTO.getData());

        return mainRow;
    }

    /**
     * 创建明细行（商品信息）
     */
    private SalesProfitExcelDTO createDetailRow(SalesProfitDetailedDTO detailedDTO, SalesProductDetail productDetail) {
        SalesProfitExcelDTO detailRow = new SalesProfitExcelDTO();

        // 复制单据基本信息（但商品相关字段使用明细数据）
        detailRow.setType(null);
        detailRow.setFrame(null);
        detailRow.setCustomer(null);
        detailRow.setTime(null);
        detailRow.setNumber(null);

        // 设置商品明细信息（从SalesProductDetail获取）
        detailRow.setGoodsName(productDetail.getGoodsName());
        detailRow.setAttr(productDetail.getAttr());
        detailRow.setUnit(productDetail.getUnit());
        detailRow.setPrice(productDetail.getPrice());
        detailRow.setNums(productDetail.getNums());
        detailRow.setDiscountInfo(productDetail.getDiscountInfo());
        detailRow.setTotalInfo(productDetail.getTotalInfo());
        detailRow.setTax(productDetail.getTax());
        detailRow.setTpt(productDetail.getTpt());
        detailRow.setBuy(productDetail.getBuy());
        detailRow.setGrossProfit(productDetail.getGrossProfit());
        detailRow.setGrossProfitMargin(productDetail.getGrossProfitMargin());

        // 核销状态（从主单据获取）
        detailRow.setNucleus(null);

        // 人员字段（从主单据获取）
        detailRow.setUser(null);
        detailRow.setPeople(null);
        detailRow.setData(null);

        return detailRow;
    }

    /**
     * 转换核销状态
     */
    private String convertNucleusStatus(Integer nucleus) {
        if (nucleus == null) return "未核销";
        switch (nucleus) {
            case 1: return "已核销";
            case 2: return "部分核销";
            default: return "未核销";
        }
    }
}
