package com.zeroone.star.finance.service;

import com.zeroone.star.project.dto.j8.finance.otherincome.AddOtherIncomeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface IOtherIncomeService {
    // 8. 简表导出（你已有）
    ResponseEntity<byte[]> exportSimpleReportExcel(List<String> iceIds) throws IOException;

    // 9. 详表导出（你已有）
    ResponseEntity<byte[]> exportDetailedReportExcel(List<String> iceIds, HttpServletRequest request) throws IOException;

    // 4. 修改收入单（更新单头 + 明细，要求未审核）
    String reviseOtherIncome(AddOtherIncomeDTO dto);

    // 6. 批量删除收入单（要求未审核；级联删明细与核销记录）
    int batchDeleteOtherIncome(List<String> ids);

    // 7. 导入收入单（Excel/CSV）
    String importOtherIncome(MultipartFile file);
}
