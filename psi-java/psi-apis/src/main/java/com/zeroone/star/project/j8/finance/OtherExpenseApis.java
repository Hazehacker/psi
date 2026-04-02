package com.zeroone.star.project.j8.finance;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.*;
import com.zeroone.star.project.query.j8.finance.othexpense.OtherExpenseQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 其它支出单接口
 * ---------------------------------
 * 功能模块：
 * 1. 获取支出单列表（支持条件查询 + 分页）
 * 2. 获取指定支出单详情
 * 3. 新增支出单
 * 4. 修改支出单
 * 5. 审核/反审核支出单（支持批量）
 * 6. 删除支出单（支持批量）
 * 7. 导入数据（Excel/CSV）
 * 8. 导出简表（基础字段）
 * 9. 导出详表（含明细字段）
 */
public interface OtherExpenseApis {
    JsonVO<PageDTO<OtherExpenseDTO>> getExpenseList(OtherExpenseQuery query);
    JsonVO<OtherExpenseDetailDTO> getDetailById(String id);
    JsonVO<String> addOtherExpense(OtherExpenseViewSampleDTO otherExpense);
    JsonVO<String> updateOtherExpense(OtherExpenseViewSampleDTO otherExpense);
    JsonVO<String> examineOtherExpense(List<String> ids, boolean approve);
    JsonVO<String> deleteExpenseForm(List<String> ids);
    JsonVO<OtherExpenseImportResultDTO> importExpenseData(MultipartFile file);
    ResponseEntity<byte[]> export(List<String> ids);
    ResponseEntity<byte[]> exportDetail(List<String> ids);
}
