package com.zeroone.star.project.j8.finance;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.*;
import com.zeroone.star.project.query.j8.finance.otherincome.OtherIncomeQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 其它收入单接口
 * ---------------------------------
 * 功能模块：
 * 1. 获取收入单列表（支持条件查询 + 分页）
 * 2. 获取指定收入单详情
 * 3. 新增收入单
 * 4. 修改收入单
 * 5. 审核/反审核收入单（支持批量）
 * 6. 删除收入单（支持批量）
 * 7. 导入数据（Excel/CSV）
 * 8. 导出简表（基础字段）
 * 9. 导出详表（含明细字段）
 */
public interface OtherIncomeApis {
    JsonVO<PageDTO<OtherIncomeDTO>> getIncomeList(OtherIncomeQuery query);
    JsonVO<OtherIncomeDetailDTO> getDetailIncomeById(String id);
    JsonVO<String> addIncome(AddOtherIncomeDTO dto);
    JsonVO<String> reviseIncome(@RequestBody AddOtherIncomeDTO dto);
    JsonVO<OtherIncomeCheckOrUncheckDTO> examineOtherIncome(@RequestBody Map<String, Object> requestParams);
    JsonVO<String> deleteIncomeForm(List<String> ids);
    JsonVO<String> importExpenseData(MultipartFile file);
    ResponseEntity<byte[]> exportSimpleReportExcel(@RequestBody Map<String, Object> body);
    ResponseEntity<byte[]> exportDetailedReportExcel(@RequestBody List<String> iceIds,HttpServletRequest request);

}
