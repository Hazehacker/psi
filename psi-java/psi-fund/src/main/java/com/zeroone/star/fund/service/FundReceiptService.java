package com.zeroone.star.fund.service;

import com.zeroone.star.project.dto.j4.fund.ReceiptAddDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptAuditDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptDeleteDTO;
import com.zeroone.star.project.dto.j4.fund.ReceiptUpdateDTO;
import com.zeroone.star.project.vo.fund.ImportResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 描述：资金管理-收款单服务接口
 * </p>
 * <p>版权：&copy;01星球</p>
 * <p>地址：01星球总部</p>
 * @author ikun
 * @version 1.0.0
 */
public interface FundReceiptService {
    /**
     * 新增收款单
     * @param receiptAddDTO 收款单数据传输对象
     */
    void addReceipt(ReceiptAddDTO receiptAddDTO);

    /**
     * 导入收款单数据
     * @param file Excel文件
     * @return 导入结果
     */
    ImportResultVO importReceiptData(MultipartFile file);

    /**
     * 导出简单收款单报表
     * @return Excel文件字节数组
     */
    byte[] exportSimpleReceiptReport();

    /**
     * 导出详细收款单报表
     * @return Excel文件字节数组
     */
    byte[] exportDetailReceiptReport();



    /**
     * 更新收款单
     * @param receiptUpdateDTO 收款单更新数据传输对象
     */
    void updateReceipt(ReceiptUpdateDTO receiptUpdateDTO);

    /**
     * 审核收款单
     * @param receiptAuditDTO 审核数据
     */
    void auditReceipt(ReceiptAuditDTO receiptAuditDTO);

    /**
     * 删除收款单
     * @param receiptDeleteDTO 删除数据
     */
    void deleteReceipt(ReceiptDeleteDTO receiptDeleteDTO);
}