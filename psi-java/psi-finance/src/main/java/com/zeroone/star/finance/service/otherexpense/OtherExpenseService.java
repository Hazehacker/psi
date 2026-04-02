package com.zeroone.star.finance.service.otherexpense;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.*;
import com.zeroone.star.project.query.j8.finance.othexpense.OtherExpenseQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 资金详情 服务类
 * </p>
 *
 * @author awei
 * @since 2025-10-21
 */
public interface OtherExpenseService {
    PageDTO<OtherExpenseDTO> getExpenseList(OtherExpenseQuery query);

    OtherExpenseDetailDTO getDetailById(String id);

    JsonVO<OtherExpenseImportResultDTO> importOtherExpenseExcel(MultipartFile file);

    ResponseEntity<byte[]> export(List<String> ids);

    ResponseEntity<byte[]> exportDetail(List<String> ids);

    // 新增支出单
    boolean addOtherExpense(OtherExpenseViewDTO otherExpense, List<OtherExpenseInfoViewDTO> otherExpenseInfoList);

    // 修改支出单
    boolean updateOtherExpense(OtherExpenseViewDTO otherExpense, List<OtherExpenseInfoViewDTO> otherExpenseInfoList);

    // 批量审核/反审核
    boolean examineOtherExpense(List<String> ids, boolean approve);
}
