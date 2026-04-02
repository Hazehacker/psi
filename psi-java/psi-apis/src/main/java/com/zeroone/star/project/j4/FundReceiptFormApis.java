package com.zeroone.star.project.j4;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptDetailDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptListDTO;
import com.zeroone.star.project.query.j4.fund.FundReceiptListQuery;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAuditDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptDeleteDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.fund.ImportResultVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAuditDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptDeleteDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * <p>
 * 描述：资金管理-收款单接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author 阿伟学长
 * @version 1.0.0
 */
public interface FundReceiptFormApis {
    /**
     * 获取收款单列表
     * @param query
     * @return
     */
    JsonVO<PageDTO<FundReceiptListDTO>> fundReceiptList(FundReceiptListQuery query);

    /**
     * 获取收款单详情
     * @param id
     * @return
     */
    JsonVO<FundReceiptDetailDTO> fundReceiptDetail(@NotNull String id);

    /**
     * 导入资金收款单数据
     * @param file Excel文件
     * @return 导入结果
     */
    @ApiOperation(value = "导入资金收款单数据")
    @PostMapping("/import")
    JsonVO<ImportResultVO> importSaleCollection(
            @ApiParam(value = "Excel文件", required = true)
            @RequestPart("file") MultipartFile file);

    /**
     * 导出资金收款单简单报表
     * @return 文件字节数组响应
     */
    @ApiOperation(value = "导出资金收款单简单报表")
    @GetMapping("/export/simple")
    ResponseEntity<byte[]> exportSimpleSaleCollection();

    /**
     * 导出资金收款单详细报表
     * @return 文件字节数组响应
     */
    @ApiOperation(value = "导出资金收款单详细报表")
    @GetMapping("/export/detail")
    ResponseEntity<byte[]> exportDetailSaleCollection();

    /**
     * 新增收款单
     */
    JsonVO<String> addReceipt(ReceiptAddDTO receiptAddDTO);

    /**
     * 修改收款单
     */
    JsonVO<String> updateReceipt(String id, ReceiptUpdateDTO receiptUpdateDTO);

    /**
     * 审核收款单
     */
    JsonVO<String> auditReceipt(ReceiptAuditDTO receiptAuditDTO);

    /**
     * 删除收款单
     */
    JsonVO<String> deleteReceipt(ReceiptDeleteDTO receiptDeleteDTO);
}
