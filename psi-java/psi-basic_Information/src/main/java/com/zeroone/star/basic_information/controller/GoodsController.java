package com.zeroone.star.basic_information.controller;

import com.alibaba.excel.EasyExcel;
import com.zeroone.star.basic_information.entity.Goods;
import com.zeroone.star.basic_information.service.IAttributeService;
import com.zeroone.star.basic_information.service.ICategoryService;
import com.zeroone.star.basic_information.service.IGoodsService;
import com.zeroone.star.basic_information.service.impl.MsGoodsMapper;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.*;
import com.zeroone.star.project.j6.basic_information.product_management.GoodsApis;
import com.zeroone.star.project.query.j6.basic_information.product_management.AttributeQuery;
import com.zeroone.star.project.query.j6.basic_information.product_management.GoodsQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.product_management.CategoryTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品管理控制器（统一接口）
 *
 * @author 杨潇, nick8311370879
 * @since 2025-10-19
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "商品管理")
@Validated
public class GoodsController implements GoodsApis {

    @Resource
    IGoodsService goodsService;

    @Resource
    ICategoryService categoryService;

    @Resource
    IAttributeService attributeService;

    @Resource
    MsGoodsMapper msGoodsMapper;

    // ============ 商品查询相关接口 ============

    @GetMapping("/query-list")
    @ApiOperation(value = "获取商品列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<GoodsDTO>> queryGoodsList(@Validated GoodsQuery query) {
        return JsonVO.success(goodsService.listGoods(query));
    }

    @GetMapping("/query-detail")
    @ApiOperation(value = "获取指定商品详情")
    @Override
    public JsonVO<GoodsDTO> queryGoodsDetail(
            @ApiParam(value = "商品ID", required = true, example = "1a2b3c4d") @RequestParam String id) {
        GoodsDTO goods = goodsService.getGoodsById(id);
        if (goods != null) {
            return JsonVO.success(goods);
        }
        return JsonVO.fail(null);
    }

    @GetMapping("/select-list")
    @ApiOperation(value = "获取商品选择列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<GoodsSelectDTO>> queryGoodsSelectList(@Validated GoodsQuery query) {
        return JsonVO.success(goodsService.listGoodsSelect(query));
    }

    // ============ 商品操作相关接口 ============

    @PostMapping("/add")
    @ApiOperation(value = "新增商品")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonVO<String> addGoods(@Validated @RequestBody GoodsAddDTO goods) {
        Goods entity = msGoodsMapper.addDtoToGoods(goods);
        entity.setId("goods_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000));

        boolean saved = goodsService.save(entity);
        return saved ? JsonVO.success(entity.getId()) : JsonVO.fail("商品保存失败");
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改商品信息")
    @Override
    public JsonVO<String> updateGoods(@Validated @RequestBody GoodsDTO goods) {
        // 先检查商品是否存在
        Goods existGoods = goodsService.getById(goods.getId());
        if (existGoods == null) {
            return JsonVO.fail("商品不存在");
        }

        // 转换DTO为实体
        Goods entity = msGoodsMapper.goodsDtoToGoods(goods);

        // 执行更新
        boolean success = goodsService.updateById(entity);
        if (success) {
            return JsonVO.success("修改成功");
        }
        return JsonVO.fail("修改失败");
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "删除商品（支持批量）")
    @Override
    public JsonVO<String> removeGoods(
            @ApiParam(value = "商品ID列表", required = true) @RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return JsonVO.fail("商品ID不能为空");
        }

        // 批量删除（支持单个或多个）
        boolean success = goodsService.removeByIds(ids);
        if (success) {
            return JsonVO.success("删除成功，共删除" + ids.size() + "条记录");
        }
        return JsonVO.fail("删除失败");
    }

    // ============ 分类和属性相关接口 ============

    @GetMapping("/category-tree")
    @ApiOperation(value = "获取商品类别名称树")
    @Override
    public JsonVO<List<CategoryTreeVO>> queryCategoryTree() {
        return JsonVO.success(categoryService.getCategoryTree());
    }

    @GetMapping("/attribute-list")
    @ApiOperation(value = "获取属性名与属性内容列表（分页）")
    @Override
    public JsonVO<PageDTO<AttributeDTO>> queryAttributeList(@Validated AttributeQuery query) {
        return JsonVO.success(attributeService.listAttributes(query));
    }

    // ============ 导入导出相关接口 ============

    @PostMapping("/import")
    @ApiOperation(value = "导入商品", notes = "通过Excel文件批量导入商品信息")
    @Override
    public JsonVO<GoodsImportResultDTO> importGoods(
            @ApiParam(value = "商品导入文件", required = true) @RequestPart("file") MultipartFile file) throws IOException {
        return goodsService.importGoods(file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出商品", notes = "导出商品数据到Excel。可通过ids参数指定导出哪些商品，不传则导出所有商品")
    @Override
    public ResponseEntity<byte[]> exportGoods(
            @ApiParam(value = "商品导出参数", required = false) @RequestBody(required = false) GoodExportDTO goodsExport) {
        try {
            // 获取要导出的商品ID列表
            List<String> ids = null;
            if (goodsExport != null && goodsExport.getIds() != null && !goodsExport.getIds().isEmpty()) {
                ids = goodsExport.getIds();
            }

            // 调用Service层导出
            byte[] excelBytes = goodsService.exportGoods(ids);

            // 设置响应头
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=goods_export.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            // 导出失败时返回错误
            return ResponseEntity.status(500).body(("导出失败：" + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/downloadTemplate")
    @ApiOperation(value = "下载导入模板")
    @Override
    public ResponseEntity<byte[]> downloadTemplate() {
        List<GoodsExcelDTO> excelDataList = new ArrayList<>();

        // 使用 EasyExcel 写入到内存流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EasyExcel.write(outputStream, GoodsExcelDTO.class)
                .sheet("商品数据导入模板")
                .doWrite(excelDataList);
        byte[] byteArray = outputStream.toByteArray();

        // 设置响应头
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=goods_import_template.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok().headers(headers).body(byteArray);
    }
}
