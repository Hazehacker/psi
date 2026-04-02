package com.zeroone.star.finance.controller;

import com.zeroone.star.finance.entity.Ice;
import com.zeroone.star.finance.service.IIceService;
import com.zeroone.star.finance.service.IOtherIncomeService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherincome.*;
import com.zeroone.star.project.j8.finance.OtherIncomeApis;
import com.zeroone.star.project.query.j8.finance.otherincome.OtherIncomeQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 其它收入单的Controller层
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
@RestController
@RequestMapping("/finance/other-income")
@Api(tags = "资金管理 - 其它收入单")
public class OtherIncomeController implements OtherIncomeApis {

    private static final Logger log = LoggerFactory.getLogger(OtherIncomeController.class);

    @Resource
    private IIceService incomeOrderService;

    /**
     * 1. 获取收入单列表（支持条件查询 + 分页）
     *
     * @param query 查询条件对象
     * @return 分页的收入单列表
     */
    @GetMapping("/list")
    @ApiOperation("获取其它收入单列表")
    public JsonVO<PageDTO<OtherIncomeDTO>> getIncomeList(OtherIncomeQuery query) {
        return JsonVO.success(incomeOrderService.listIncomeOrders(query));

    }

    /**
     * 2. 获取指定收入单详情
     *
     * @param id 收入单的唯一ID
     * @return 指定ID的收入单详细信息
     */
    @GetMapping("/{id}")
    @ApiOperation("获取收入单详情")
    public JsonVO<OtherIncomeDetailDTO> getDetailIncomeById(@PathVariable String id) {
        OtherIncomeDetailDTO vo = incomeOrderService.getIncomeOrder(id);
        return JsonVO.success(vo);
    }

    /**
     * 3. 新增收入单
     *
     * @param dto 包含新增收入单所需数据的DTO对象
     * @return 新增记录的唯一ID
     */
    @PostMapping("/add")
    @ApiOperation("新增收入单")
    public JsonVO<String> addIncome(@Validated @RequestBody AddOtherIncomeDTO dto) {
        String id;
        try {
            id = incomeOrderService.addIncomeOrder(dto);
        } catch (IllegalArgumentException e) {
            return JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(id);
    }


    /**
     * 4.修改收入单
     *
     * @param dto 包含新增收入单所需数据的DTO对象
     * @return 新增记录的唯一ID
     */
    @PostMapping("/revise")
    @ApiOperation("修改收入单")
    public JsonVO<String> reviseIncome(@Validated @RequestBody AddOtherIncomeDTO dto) {
        try {
            String id = otherIncomeService.reviseOtherIncome(dto);
            return JsonVO.success(id);
        } catch (Exception e) {
            log.error("revise failed", e);
            return JsonVO.fail(e.getMessage());
        }
    }


    /**
     * ice表服务
     */
    @Resource
    private IIceService iceService;

    /**
     * 5.审核/反审核收入单（支持批量）
     *
     * @param requestParams 操作的id集合 & 审核状态（0:未审核|1:已审核）
     * @return 审核/反审核结果
     */
    @PostMapping("/examine")
    @ApiOperation(value = "审核或反审核")
    public JsonVO<OtherIncomeCheckOrUncheckDTO> examineOtherIncome(
            @RequestBody
            @ApiParam(value = "审核/反审核请求参数，格式: {\"ids\": [\"id1\", \"id2\"], \"examine\": 0或1}", required = true)
            Map<String, Object> requestParams) {
        try {
            // 从请求参数中获取ids和examine
            Object idsObj = requestParams.get("ids");
            Object examineObj = requestParams.get("examine");

            // 参数校验
            if (idsObj == null || examineObj == null) {
                log.warn("examineOtherIncome: 参数不完整, ids={}, examine={}", idsObj, examineObj);
                return JsonVO.fail(null);
            }

            // 转换ids列表
            List<String> ids;
            if (idsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> idList = (List<Object>) idsObj;
                ids = idList.stream()
                        .filter(id -> id != null)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            } else if (idsObj instanceof String) {
                ids = Arrays.asList(idsObj.toString());
            } else {
                log.warn("examineOtherIncome: ids参数格式错误, ids={}", idsObj);
                return JsonVO.fail(null);
            }

            if (ids.isEmpty()) {
                log.warn("examineOtherIncome: ids列表为空");
                return JsonVO.fail(null);
            }

            // 转换examine值
            Integer examineInt;
            if (examineObj instanceof Integer) {
                examineInt = (Integer) examineObj;
            } else if (examineObj instanceof Boolean) {
                examineInt = (Boolean) examineObj ? 1 : 0;
            } else if (examineObj instanceof String) {
                examineInt = Integer.parseInt(examineObj.toString());
            } else {
                log.warn("examineOtherIncome: examine参数格式错误, examine={}", examineObj);
                return JsonVO.fail(null);
            }

            // 确保examine值只能是0或1
            if (examineInt != 0 && examineInt != 1) {
                log.warn("examineOtherIncome: examine值无效, examine={}", examineInt);
                return JsonVO.fail(null);
            }

            // 转换为Boolean类型（Ice实体中examine是Boolean类型）
            Integer examineValue = examineInt;

            log.info("examineOtherIncome: 开始批量更新审核状态, ids={}, examine={}", ids, examineValue);

            // 查询所有需要更新的记录
            List<Ice> iceList = iceService.listByIds(ids);

            if (iceList == null || iceList.isEmpty()) {
                log.warn("examineOtherIncome: 未找到对应的记录, ids={}", ids);
                return JsonVO.fail(null);
            }

            if (iceList.size() != ids.size()) {
                log.warn("examineOtherIncome: 部分记录未找到, 请求ids={}, 找到记录数={}", ids.size(), iceList.size());
            }

            // 批量更新examine状态
            for (Ice ice : iceList) {
                ice.setExamine(examineValue);
            }

            // 执行批量更新
            boolean updateResult = iceService.updateBatchById(iceList);

            if (updateResult) {
                log.info("examineOtherIncome: 批量更新成功, 更新记录数={}", iceList.size());

                // 构建返回DTO（返回第一个更新的记录信息作为示例）
                OtherIncomeCheckOrUncheckDTO resultDTO = new OtherIncomeCheckOrUncheckDTO();
                // Ice的id是String类型，DTO的id也是String类型，直接赋值
                resultDTO.setId(iceList.get(0).getId());
                resultDTO.setExamine(examineInt);

                return JsonVO.success(resultDTO);
            } else {
                log.error("examineOtherIncome: 批量更新失败");
                return JsonVO.fail(null);
            }

        } catch (Exception e) {
            log.error("examineOtherIncome: 更新审核状态时发生异常", e);
            return JsonVO.fail(null);
        }
    }

    /**
     * 6.删除其他支出单（可批量）
     *
     * @param ids 删除其他支出单的ids
     * @return 删除是否成功
     */
    @DeleteMapping("/batch-delete")
    @ApiOperation("批量删除收入单")
    public JsonVO<String> deleteIncomeForm(
            @RequestBody @ApiParam(value = "删除的id列表", required = true, example = "[\"ice001\",\"ice002\"]")
            List<String> ids) {
        try {
            int n = otherIncomeService.batchDeleteOtherIncome(ids);
            return JsonVO.success("已删除 " + n + " 条");
        } catch (IllegalStateException e) {
            return JsonVO.fail(e.getMessage());
        } catch (Exception e) {
            log.error("batch-delete failed", e);
            return JsonVO.fail("删除失败");
        }
    }

    /**
     * 7.通过文件导入数据
     *
     * @param file 上传的Excel或CSV文件
     * @return JsonVO<OtherExpenseImportResultDTO> 上传文件的详情况
     */
    @PostMapping("/import")
    @ApiOperation("导入收入单数据(Excel/CSV)")
    public JsonVO<String> importExpenseData(@RequestParam("file") MultipartFile file) {
        try {
            String r = otherIncomeService.importOtherIncome(file);
            return JsonVO.success(r);
        } catch (Exception e) {
            log.error("import failed", e);
            return JsonVO.fail(e.getMessage());
        }
    }

    /**
     * 8.导出其他收入单简单报表Excel
     *
     * @return Excel文件流
     */

    @PostMapping("/export/simple")
    @ApiOperation(value = "导出其他收入单简单报表Excel")
    public ResponseEntity<byte[]> exportSimpleReportExcel(@RequestBody Map<String, Object> body) {
        try {
            List<String> iceIds = parseIdList(body, "iceIds");
            if (iceIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return otherIncomeService.exportSimpleReportExcel(iceIds);
        } catch (Exception e) {
            log.error("export/simple failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Resource
    private IOtherIncomeService otherIncomeService;

    /**
     * 9.导出其他收入单详细报表Excel
     *
     * @param iceIds  请求体，包含iceIds数组，例如：{"iceIds": ["ice001", "ice002"]}
     * @param request HTTP请求对象
     * @return ZIP文件流（包含多个Excel文件）
     */
    @PostMapping("/export/detail")
    @ApiOperation(value = "导出其他收入单详细报表Excel（ZIP格式）")
    public ResponseEntity<byte[]> exportDetailedReportExcel(@RequestBody List<String> iceIds, HttpServletRequest request) {
        try {
            log.info("export/detail: 开始导出详细报表, body keys={}", iceIds);


            // 参数校验
            if (iceIds.isEmpty()) {
                log.warn("export/detail: 未提供有效的iceIds");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            log.info("export/detail: iceIds={} count={}", iceIds, iceIds.size());

            return otherIncomeService.exportDetailedReportExcel(iceIds, request);

        } catch (Exception e) {
            log.error("export/detail failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 解析ID列表的辅助方法
     */
    private List<String> parseIdList(Map<String, Object> body, String key) {
        List<String> ids = new ArrayList<>();
        if (body == null) {
            return ids;
        }

        Object idsObj = body.get(key);
        if (idsObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> idList = (List<Object>) idsObj;
            for (Object id : idList) {
                if (id != null) {
                    String idStr = id.toString().trim();
                    if (!idStr.isEmpty()) {
                        ids.add(idStr);
                    }
                }
            }
        } else if (idsObj instanceof String) {
            String idStr = idsObj.toString().trim();
            if (!idStr.isEmpty()) {
                ids.add(idStr);
            }
        }
        return ids;
    }
}
