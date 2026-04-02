package com.zeroone.star.finance.controller;

import com.zeroone.star.finance.service.IOceService;
import com.zeroone.star.finance.service.otherexpense.OtherExpenseService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.*;
import com.zeroone.star.project.j8.finance.OtherExpenseApis;
import com.zeroone.star.project.query.j8.finance.othexpense.OtherExpenseQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 其它支出单controller层
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
@Validated
@RestController
@RequestMapping("/finance/other-expense")
@Api(tags = "资金管理 - 其它支出单")
public class OtherExpenseController implements OtherExpenseApis {
    @Resource
    private OtherExpenseService otherExpenseService;

    @Resource
    private IOceService oceService;

    /**
     * 1.获取支出单列表（支持条件 + 分页）
     *
     * @param query 查询参数
     * @return 其它支出单列表
     */
    @GetMapping("/list")
    @ApiOperation("查询其它支出单列表")
    public JsonVO<PageDTO<OtherExpenseDTO>> getExpenseList(OtherExpenseQuery query) {
        return JsonVO.success(otherExpenseService.getExpenseList(query));
    }


    /**
     * 2.根据ID获取支出单详情
     *
     * @param id 查询ID
     * @return 支出单详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查询支出单详情")
    public JsonVO<OtherExpenseDetailDTO> getDetailById(@PathVariable String id) {
        OtherExpenseDetailDTO dto = otherExpenseService.getDetailById(id);
        if (dto == null) return JsonVO.create(null, 404, "不存在对应单据");
        return JsonVO.success(dto);
    }

    /**
     * 3.新增支出单
     *
     * @param dto 新增支出单数据
     * @return 新增是否成功
     */
    @PostMapping("/add")
    @ApiOperation("新增支出单")
    public JsonVO<String> addOtherExpense(@RequestBody @Validated OtherExpenseViewSampleDTO dto) {
        // dto 内封装了主表与子表数据
        // 调用 Service 层处理业务逻辑
        OtherExpenseViewDTO main = dto.getOtherExpense();
        List<OtherExpenseInfoViewDTO> details = dto.getOtherExpenseInfoList();

        try {
            boolean ok = otherExpenseService.addOtherExpense(main, details);
            if (ok) {
                return JsonVO.success("新增支出单成功");
            } else {
                return JsonVO.create("新增支出单失败", ResultStatus.SERVER_ERROR);
            }
        } catch (RuntimeException e) {
            // 捕获 Service 抛出的校验异常，并返回前端
            return JsonVO.fail(e.getMessage());
        }
    }

    /**
     * 4.修改支出单
     *
     * @param dto 修改支出单数据
     * @return 修改是否成功
     */
    @PutMapping("/update")
    @ApiOperation("修改支出单")
    public JsonVO<String> updateOtherExpense(@RequestBody @Validated OtherExpenseViewSampleDTO dto) {
        OtherExpenseViewDTO main = dto.getOtherExpense();
        List<OtherExpenseInfoViewDTO> details = dto.getOtherExpenseInfoList();

        try {
            boolean ok = otherExpenseService.updateOtherExpense(main, details);
            if (ok) {
                return JsonVO.success("修改支出单成功");
            } else {
                return JsonVO.create("修改支出单失败", ResultStatus.SERVER_ERROR);
            }
        } catch (RuntimeException e) {
            // 捕获 Service 抛出的校验异常，并返回前端
            return JsonVO.fail(e.getMessage());
        }
    }

    /**
     * 5.审核/反审核支出单（支持批量）
     *
     * @param ids     支出单id
     * @param approve 是否审核
     * @return 是否成功
     */
    @PostMapping("/examine")
    @ApiOperation("审核/反审核支出单")
    public JsonVO<String> examineOtherExpense(
            @RequestBody
            @ApiParam(value = "支出单id列表", required = true, example = "[\"1\",\"2\"]") List<String> ids,
            @RequestParam("approve") boolean approve) {
        boolean ok = otherExpenseService.examineOtherExpense(ids, approve);
        return ok ? JsonVO.success("操作单据成功") : JsonVO.fail("操作单据失败");
    }

    /**
     * 6.删除其他支出单（可批量）
     *
     * @param ids 删除其他支出单的ids
     * @return 删除是否成功
     */
    @DeleteMapping("/batch-delete")
    @ApiOperation("批量删除支出单")
    public JsonVO<String> deleteExpenseForm(@RequestBody @ApiParam(value = "删除的id列表", required = true, example = "[\"1\" , \"2\" ]") List<String> ids) {
        //调用service
        return oceService.deleteOtherExpenseForm(ids);

    }


    /**
     * 7.通过文件导入数据
     *
     * @param file 上传的Excel或CSV文件
     * @return JsonVO<OtherExpenseImportResultDTO> 导入的结果
     */
    @PostMapping("/import")
    @ApiOperation("导入支出单数据(Excel/CSV)")
    public JsonVO<OtherExpenseImportResultDTO> importExpenseData(@RequestParam("file") MultipartFile file) {
        try {
            OtherExpenseImportResultDTO result = otherExpenseService.importOtherExpenseExcel(file).getData();
            return JsonVO.success(result); // 成功时返回结果
        } catch (Exception e) {
            // 异常时返回错误信息
            OtherExpenseImportResultDTO errorResult = new OtherExpenseImportResultDTO(
                    0, 0, 0, Collections.singletonList("导入失败：" + e.getMessage())
            );
            return JsonVO.fail(errorResult);
        }
    }

    /**
     * 8.导出简表（基础字段）
     *
     * @param ids 支出单ID列表
     * @return 文件字节流（ResponseEntity<byte[]>）
     */
    @SneakyThrows
    @PostMapping(value = "/export", produces = "application/octet-stream")
    @ApiOperation(value = "导出其它支出单")
    public ResponseEntity<byte[]> export(@RequestBody List<String> ids) {
        return otherExpenseService.export(ids);
    }

    /**
     * 9. 导出详表（含明细字段）
     *
     * @param ids 选中支出单id列表
     * @return 文件字节流（ResponseEntity<byte[]>）
     */
    @SneakyThrows
    @PostMapping(value = "/exportDetail", produces = "application/octet-stream")
    @ApiOperation(value = "导出其它支出单明细")
    public ResponseEntity<byte[]> exportDetail(@RequestBody List<String> ids) {
        return otherExpenseService.exportDetail(ids);
    }
}