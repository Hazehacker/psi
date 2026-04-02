package com.zeroone.star.basic_information.controller;


import com.zeroone.star.basic_information.entity.Supplier;
import com.zeroone.star.basic_information.service.ISupplierService;
import com.zeroone.star.basic_information.service.impl.MsSupplierMapper;
import com.zeroone.star.basic_information.service.querySupplierListService;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.SupplierDTO;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierDto;
import com.zeroone.star.project.dto.j6.basic_information.supplier_management.querySupplierListDto;
import com.zeroone.star.project.j6.basic_information.supplier_management.SupplierApis;
import com.zeroone.star.project.query.j6.basic_information.supplier_management.supplierListQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.supplier_management.ImportResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 供应商 前端控制器
 * </p>
 *
 * @author 趣味生煎
 * @since 2025-10-21
 */
@Slf4j
@RestController
@RequestMapping("/basic_information/supplier")
@Api(tags = "供应商管理")
public class SupplierController implements SupplierApis {

    @Resource
    ISupplierService supplierService;

    @Resource
    MsSupplierMapper ms;

    @Resource
    FastDfsClientComponent dfs;

    @Value("${fastdfs.nginx-servers}")
    private String fileServerUrl;



    @Resource
    querySupplierListService querySupplierListService;
    @ApiOperation(value = "查询供应商列表")
    @GetMapping("/querySupplierList")
    public JsonVO<PageDTO<querySupplierListDto>> querySupplierList(supplierListQuery query) {
        return JsonVO.success(querySupplierListService.querySupplierList(query));
    }

    @Override
    public JsonVO<PageDTO<querySupplierDto>> querySupplier(supplierListQuery query) {
        return null;
    }

    @GetMapping("/query-one")
    @ApiOperation("查询指定供应商信息")
    @ApiImplicitParam(name = "id", value = "供应商ID", required = true)
    @Override
    public JsonVO<SupplierDTO> querySupplierById(Integer id) {
        return JsonVO.success(supplierService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation("添加供应商")
    @Override
    public JsonVO<String> addSupplier(@Validated @RequestBody SupplierAddDTO supplierAddDTO) {
        Supplier supplier = ms.addDtoToSupplier(supplierAddDTO);
        if (supplierService.save(supplier)) {
            return JsonVO.success(supplier.getId().toString());
        }
        return JsonVO.fail(null);
    }

    @PutMapping("/update")
    @ApiOperation("修改供应商信息")
    @Override
    public JsonVO<String> updateSupplier(@Validated @RequestBody SupplierDTO supplierDTO) {
        if (supplierService.updateById(ms.supplierDtoToSupplier(supplierDTO))) {
            return JsonVO.success(supplierDTO.getId().toString());
        }
        return JsonVO.fail(null);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除供应商")
    @Override
    public JsonVO<String> deleteSupplier(@RequestBody List<Integer> ids) {
        if (supplierService.removeByIds(ids)) {
            return JsonVO.success(ids.toString());
        }
        return JsonVO.fail(null);
    }

    /**
     * 导入供应商数据
     * @param file Excel文件
     * @return 导入结果
     */
    @SneakyThrows
    @PostMapping("/import")
    @ApiOperation(value = "导入供应商数据")
    @ResponseBody
    public JsonVO<ImportResultVO> importSuppliers(MultipartFile file,String url) {
        // 直接调用服务层处理导入
        ImportResultVO importResult = new ImportResultVO();
        try{
            importResult = supplierService.importSuppliers(file,url);
            return JsonVO.success(importResult);
        }catch (Exception e){
            log.error("导入供应商数据失败", e);
            return JsonVO.fail(importResult);
        }

    }


    @Override
    @GetMapping("/export")
    @ApiOperation("导出供应商数据")
    public JsonVO<String> exportSuppliers(supplierListQuery query) {
        try {
            String fileUrl = supplierService.exportSuppliers(query);
            return JsonVO.success(fileUrl);
        } catch (Exception e) {
            log.error("导出供应商数据失败", e);
            return JsonVO.fail("导出失败: " + e.getMessage());
        }
    }

    @Override
    @GetMapping("/template")
    @ApiOperation("下载导入模板")
    public JsonVO<String> downloadTemplate() {
        try {
            String templateUrl = supplierService.downloadTemplate();
            return JsonVO.success(templateUrl);
        } catch (Exception e) {
            log.error("下载模板失败", e);
            return JsonVO.fail("下载模板失败: " + e.getMessage());
        }
    }


}


