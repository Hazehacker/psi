package com.zeroone.star.basic_information.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Category;
import com.zeroone.star.basic_information.entity.Goods;
import com.zeroone.star.basic_information.mapper.CategoryMapper;
import com.zeroone.star.basic_information.mapper.GoodsMapper;
import com.zeroone.star.basic_information.service.IGoodsService;
import com.zeroone.star.basic_information.service.util.SimpleImportListener;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsSelectDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsExcelDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsImportResultDTO;
import com.zeroone.star.project.query.j6.basic_information.product_management.GoodsQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zeroone.star.project.vo.ResultStatus.FAIL;

/**
 * 商品服务实现类
 * 
 * @author 杨潇, nick8311370879
 * @since 2025-10-19
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    MsGoodsMapper msGoodsMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public PageDTO<GoodsDTO> listGoods(GoodsQuery query) {
        // 构建分页查询对象
        Page<Goods> page = new Page<>(query.getPageIndex(), query.getPageSize());

        // 构建查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.like(!StringUtils.isEmpty(query.getNumber()), "number", query.getNumber());
        queryWrapper.eq(!StringUtils.isEmpty(query.getCategory()), "category", query.getCategory());
        queryWrapper.eq(!StringUtils.isEmpty(query.getBrand()), "brand", query.getBrand());
        queryWrapper.eq(query.getType() != null, "type", query.getType());
        queryWrapper.eq(!StringUtils.isEmpty(query.getCode()), "code", query.getCode());
        queryWrapper.orderByDesc("id");

        // 分页查询
        Page<Goods> pageRes = baseMapper.selectPage(page, queryWrapper);

        // 获取所有分类ID
        List<String> categoryIds = pageRes.getRecords().stream()
                .map(Goods::getCategory)
                .filter(id -> !StringUtils.isEmpty(id))
                .distinct()
                .collect(Collectors.toList());

        // 批量查询分类名称
        Map<String, String> categoryNameMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryMapper.selectBatchIds(categoryIds);
            categoryNameMap = categories.stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
        }

        // 转换并填充分类名称
        final Map<String, String> finalCategoryNameMap = categoryNameMap;
        return PageDTO.create(pageRes, goods -> {
            GoodsDTO dto = msGoodsMapper.goodsToGoodsDto(goods);
            dto.setCategoryName(finalCategoryNameMap.get(goods.getCategory()));
            return dto;
        });
    }

    @Override
    public GoodsDTO getGoodsById(String id) {
        Goods goods = baseMapper.selectById(id);
        if (goods != null) {
            GoodsDTO dto = msGoodsMapper.goodsToGoodsDto(goods);

            // 查询分类名称
            if (!StringUtils.isEmpty(goods.getCategory())) {
                Category category = categoryMapper.selectById(goods.getCategory());
                if (category != null) {
                    dto.setCategoryName(category.getName());
                }
            }

            return dto;
        }
        return null;
    }

    @Override
    public PageDTO<GoodsSelectDTO> listGoodsSelect(GoodsQuery query) {
        // 构建分页查询对象
        Page<Goods> page = new Page<>(query.getPageIndex(), query.getPageSize());

        // 构建查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.like(!StringUtils.isEmpty(query.getNumber()), "number", query.getNumber());
        queryWrapper.eq(!StringUtils.isEmpty(query.getCategory()), "category", query.getCategory());
        queryWrapper.eq(!StringUtils.isEmpty(query.getBrand()), "brand", query.getBrand());
        queryWrapper.eq(query.getType() != null, "type", query.getType());
        queryWrapper.orderByDesc("id");

        // 分页查询
        Page<Goods> pageRes = baseMapper.selectPage(page, queryWrapper);

        // 获取所有分类ID
        List<String> categoryIds = pageRes.getRecords().stream()
                .map(Goods::getCategory)
                .filter(id -> !StringUtils.isEmpty(id))
                .distinct()
                .collect(Collectors.toList());

        // 批量查询分类名称
        Map<String, String> categoryNameMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryMapper.selectBatchIds(categoryIds);
            categoryNameMap = categories.stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
        }

        // 转换并填充分类名称
        final Map<String, String> finalCategoryNameMap = categoryNameMap;
        return PageDTO.create(pageRes, goods -> {
            GoodsSelectDTO dto = msGoodsMapper.goodsToGoodsSelectDto(goods);
            dto.setCategoryName(finalCategoryNameMap.get(goods.getCategory()));
            return dto;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<GoodsImportResultDTO> importGoods(MultipartFile file) {
        SimpleImportListener<GoodsExcelDTO> listener = new SimpleImportListener<>();

        try {
            // 开始读取Excel，headRowNumber(1)表示跳过标题行
            EasyExcel.read(file.getInputStream(), GoodsExcelDTO.class, listener)
                    .sheet()
                    .headRowNumber(1)
                    .doRead(); // 正常情况会执行到底

            // 以下代码仅在全部行校验通过时执行
            List<GoodsExcelDTO> successList = listener.getSuccessList();

            // 进行后续业务处理，如批量保存
            if (!successList.isEmpty()) {
                // 创建一个空列表，用来存放转换后的商品数据
                List<Goods> goodsList = new ArrayList<>();

                // 遍历Excel中的每一行数据
                for (GoodsExcelDTO excelData : successList) {
                    // 把Excel数据转换成数据库实体
                    Goods dbEntity = msGoodsMapper.toEntity(excelData);
                    // 生成ID（使用时间戳+随机数）
                    dbEntity.setId("goods_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000));
                    // 设置拼音（简单处理，可以用拼音库优化）
                    if (dbEntity.getName() != null) {
                        dbEntity.setPy(dbEntity.getName().toLowerCase().replaceAll("[^a-z0-9]", ""));
                    }
                    // 添加到待保存的列表中
                    goodsList.add(dbEntity);
                }

                // 一次性保存所有商品到数据库
                boolean saved = this.saveBatch(goodsList);
                System.out.println("批量保存结果: " + saved + ", 保存数量: " + goodsList.size());
            }

            GoodsImportResultDTO successResult = new GoodsImportResultDTO();
            successResult.setSuccessCount(successList.size());
            successResult.setMessage("成功导入 " + successList.size() + " 条数据");
            return JsonVO.success(successResult);

        } catch (RuntimeException e) {
            // 检查是否是我们的校验异常
            if (e.getMessage() != null && e.getMessage().contains("VALIDATION_ERROR:")) {
                // 这是我们的校验异常
                GoodsImportResultDTO errorResult = new GoodsImportResultDTO();
                errorResult.setSuccessCount(listener.getSuccessList().size());
                errorResult.setFailCount(1);
                errorResult.setMessage(listener.getErrorMessage());
                return JsonVO.create(errorResult, FAIL);
            } else {
                // 其他RuntimeException按系统错误处理
                GoodsImportResultDTO errorResult = new GoodsImportResultDTO();
                errorResult.setMessage("系统错误：" + e.getMessage());
                return JsonVO.create(errorResult, FAIL);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportGoods(List<String> ids) {
        try {
            // 1. 查询要导出的商品数据
            List<Goods> goodsList;
            if (ids == null || ids.isEmpty()) {
                // 如果没有指定ID，导出所有商品
                goodsList = this.list();
            } else {
                // 根据ID列表查询商品
                goodsList = this.listByIds(ids);
            }

            System.out.println("查询到商品数量: " + goodsList.size());

            // 2. 手动转换为 Excel DTO（避免BLOB字段的映射问题）
            List<GoodsExcelDTO> excelDataList = new ArrayList<>();
            for (Goods goods : goodsList) {
                try {
                    GoodsExcelDTO dto = new GoodsExcelDTO();
                    dto.setName(goods.getName());
                    dto.setNumber(goods.getNumber());
                    dto.setSpec(goods.getSpec());
                    dto.setCategory(goods.getCategory());
                    dto.setBrand(goods.getBrand());
                    dto.setUnit(goods.getUnit());
                    dto.setBuy(goods.getBuy());
                    dto.setSell(goods.getSell());
                    dto.setRetailPrice(goods.getSell()); // 零售价使用销售价
                    dto.setCode(goods.getCode());
                    dto.setLocation(goods.getLocation());
                    dto.setStock(goods.getStock());
                    dto.setType(goods.getType());
                    dto.setData(goods.getData());
                    // 注意：不包含 imgs, details, units, strategy, more 等BLOB字段
                    excelDataList.add(dto);
                } catch (Exception e) {
                    System.err.println("转换商品失败，ID: " + goods.getId() + ", 原因: " + e.getMessage());
                    // 跳过有问题的数据，继续处理下一条
                }
            }

            System.out.println("成功转换商品数量: " + excelDataList.size());

            // 3. 使用 EasyExcel 写入到内存流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            EasyExcel.write(outputStream, GoodsExcelDTO.class)
                    .sheet("商品数据")
                    .doWrite(excelDataList);

            // 4. 返回字节数组
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出商品失败：" + e.getMessage(), e);
        }
    }
}
