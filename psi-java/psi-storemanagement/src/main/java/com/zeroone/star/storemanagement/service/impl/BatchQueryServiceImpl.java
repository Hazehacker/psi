package com.zeroone.star.storemanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.query.j2.store.BatchDetailQuery;
import com.zeroone.star.project.query.j2.store.BatchQuery;
import com.zeroone.star.storemanagement.mapper.BatchInfoMapper;
import com.zeroone.star.storemanagement.mapper.BatchListMapper;
import com.zeroone.star.storemanagement.service.IBatchQueryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.service.impl
 * @Author: 高
 * @CreateTime: 2025-10-24 20:16
 * @Description: 批次查询服务实现类
 * @Version: 1.0
 */
@Service
@Slf4j
public class BatchQueryServiceImpl implements IBatchQueryService {

    @Resource
    private BatchInfoMapper batchInfoMapper;

    @Resource
    private BatchListMapper batchListMapper;

    @Resource
    private EasyExcelComponent easyExcelComponent;

    @Resource
    UserHolder userHolder;

    @Override
    public PageDTO<BatchListDTO> listBatch(BatchQuery query) {
            // 处理分类递归查询
            if (query.getGoodsCategoryId() != null && !query.getGoodsCategoryId().isEmpty()) {
                Set<String> allCategoryIds = getCategoryTreeIds(query.getGoodsCategoryId());
                query.setGoodsCategoryIds(new ArrayList<>(allCategoryIds));
            }

            // 创建分页对象
            Page<BatchListDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());

            // 1. 查询商品基本信息
            Page<BatchListDTO> goodsPage = batchListMapper.selectBatchGoodsList(page, query);
            List<BatchListDTO> goodsList = goodsPage.getRecords();

            if (goodsList.isEmpty()) {
                return PageDTO.create(goodsPage);
            }

            // 提取商品ID列表和商品信息
            List<String> goodsIds = goodsList.stream()
                    .map(BatchListDTO::getId)
                    .collect(Collectors.toList());


            Map<String, BigDecimal> goodsStockMap = goodsList.stream()
                    .collect(Collectors.toMap(BatchListDTO::getId, BatchListDTO::getStock));

            Map<String, Integer> goodsProtectMap = goodsList.stream()
                    .collect(Collectors.toMap(BatchListDTO::getId, BatchListDTO::getProtect));

            // 2. 查询商品在各仓库的库存汇总
            List<WarehouseStockDTO> warehouseStocks = batchListMapper.selectGoodsWarehouseStock(goodsIds, query);
            Map<String, List<WarehouseStockDTO>> goodsWarehouseMap = warehouseStocks.stream()
                    .collect(Collectors.groupingBy(WarehouseStockDTO::getGoodsId));

            // 3. 查询商品属性信息
            List<BatchAttrDTO> attrStocks = batchListMapper.selectGoodsAttrStock(goodsIds, query);
            Map<String, List<BatchAttrDTO>> goodsAttrMap = attrStocks.stream()
                    .collect(Collectors.groupingBy(BatchAttrDTO::getGoodsId));

            // 4. 查询批次详情信息
            List<BatchDocumentDTO> allBatchDocuments = batchListMapper.selectBatchDocumentsByGoodsIds(goodsIds, query);

            // 5. 设置预警状态和过期日期
            for (BatchDocumentDTO document : allBatchDocuments) {
                BigDecimal stockThreshold = goodsStockMap.get(document.getGoodsId());
                document.setIsWarning(document.getNums().compareTo(stockThreshold) <= 0);

                // 计算过期日期：生产日期 + 保质期天数
                Integer protectDays = goodsProtectMap.get(document.getGoodsId());
                if (document.getProductDate() != null && protectDays != null && protectDays > 0) {
                    LocalDate expireDate = document.getProductDate().plusDays(protectDays);
                    document.setExpireDate(expireDate);
                }
            }

            // 6. 按商品ID、属性名称、批次号进行分组
            Map<String, Map<String, Map<String, List<BatchDocumentDTO>>>> batchGroupMap = allBatchDocuments.stream()
                    .collect(Collectors.groupingBy(
                            BatchDocumentDTO::getGoodsId,
                            Collectors.groupingBy(
                                    dto -> {
                                        // 修正无属性商品的处理
                                        if (dto.getAttrName() == null || dto.getAttrName().isEmpty()) {
                                            return "NO_ATTR";
                                        }
                                        return dto.getAttrName();
                                    },
                                    Collectors.groupingBy(BatchDocumentDTO::getBatchNumber)
                            )
                    ));


            //7. 组装数据
            for (BatchListDTO goods : goodsList) {
                String goodsId = goods.getId();
                BigDecimal stockThreshold = goods.getStock();

                // 设置商品在各仓库的库存汇总
                List<WarehouseStockDTO> goodsWarehouses = goodsWarehouseMap.getOrDefault(goodsId, new ArrayList<>());
                goods.setGoodsWarehouses(goodsWarehouses);

                // 判断商品是否有属性
                List<BatchAttrDTO> attrs = goodsAttrMap.get(goodsId);
                boolean hasAttr = attrs != null && !attrs.isEmpty();

                goods.setHasAttr(hasAttr);

                if (hasAttr) {
                    // 有属性商品：按属性分组
                    List<BatchAttrDTO> attrBatches = new ArrayList<>();

                    for (BatchAttrDTO attr : attrs) {
                        BatchAttrDTO attrDTO = new BatchAttrDTO();
                        attrDTO.setAttrName(attr.getAttrName());
                        attrDTO.setAttrStock(attr.getAttrStock());

                        // 获取该属性下的批次信息
                        Map<String, Map<String, List<BatchDocumentDTO>>> goodsBatchMap = batchGroupMap.getOrDefault(goodsId, new HashMap<>());
                        Map<String, List<BatchDocumentDTO>> attrBatchMap = goodsBatchMap.getOrDefault(attr.getAttrName(), new HashMap<>());

                        List<BatchNumberDTO> batchNumbers = buildBatchNumbers(attrBatchMap, stockThreshold, query);
                        attrDTO.setBatches(batchNumbers);

                        attrBatches.add(attrDTO);
                    }

                    goods.setAttrBatches(attrBatches);// 直接设置attrBatches
                    goods.setNoAttrBatches(new ArrayList<>());
                } else {
                    // 无属性商品：直接显示批次
                    Map<String, Map<String, List<BatchDocumentDTO>>> goodsBatchMap = batchGroupMap.getOrDefault(goodsId, new HashMap<>());
                    Map<String, List<BatchDocumentDTO>> noAttrBatchMap = goodsBatchMap.getOrDefault("NO_ATTR", new HashMap<>());

                    List<BatchNumberDTO> batchNumbers = buildBatchNumbers(noAttrBatchMap, stockThreshold, query);

                    goods.setNoAttrBatches(batchNumbers); // 直接设置noAttrBatches
                    goods.setAttrBatches(new ArrayList<>());
                }
            }

            return PageDTO.create(goodsPage);


    }

    /**
     * 递归获取类别树的所有ID
     */
    private Set<String> getCategoryTreeIds(String categoryId) {
        Set<String> result = new HashSet<>();
        result.add(categoryId);
        findChildCategoriesRecursive(categoryId, result);
        return result;
    }

    /**
     * 具体的递归方法
     */
    private void findChildCategoriesRecursive(String parentId, Set<String> result) {
        List<String> childIds = batchListMapper.selectChildCategoryIds(parentId);
        if (childIds != null && !childIds.isEmpty()) {
            for (String childId : childIds) {
                if (!result.contains(childId)) {
                    result.add(childId);
                    findChildCategoriesRecursive(childId, result);
                }
            }
        }
    }



    private List<BatchNumberDTO> buildBatchNumbers(Map<String, List<BatchDocumentDTO>> batchMap, BigDecimal stockThreshold, BatchQuery query) {
        List<BatchNumberDTO> batchNumbers = new ArrayList<>();

        for (Map.Entry<String, List<BatchDocumentDTO>> entry : batchMap.entrySet()) {
            String batchNumber = entry.getKey();
            List<BatchDocumentDTO> documents = entry.getValue();

            // 判断批次号是否预警：只要有一个明细记录 nums <= stock
            boolean isBatchWarning = documents.stream()
                    .anyMatch(doc -> doc.getNums().compareTo(stockThreshold) <= 0);

            // 如果是预警批次查询，只显示预警的批次
            if (query.getBatchState() != null && query.getBatchState() == 1 && !isBatchWarning) {
                continue; // 跳过非预警批次
            }

            BatchNumberDTO batchNumberDTO = new BatchNumberDTO();
            batchNumberDTO.setBatchNumber(batchNumber);
            batchNumberDTO.setBatchDocuments(documents);

            // 计算该批次总库存
            BigDecimal totalStock = documents.stream()
                    .map(BatchDocumentDTO::getNums)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            batchNumberDTO.setTotalStock(totalStock);
            batchNumberDTO.setIsWarning(isBatchWarning);

            batchNumbers.add(batchNumberDTO);
        }

        return batchNumbers;
    }


    /**
     * 获取指定批次详情数据（分页）
     * @param batchDetailQuery 查询条件对象，包含批次pid、单据类型、时间范围、单据编号等过滤条件以及分页参数
     * @return PageDTO<BatchDetailDTO> 分页后的批次详情数据列表，每条记录包含pid、time、type、number、info、nums等字段
     */
    @SneakyThrows
    public PageDTO<BatchDetailDTO> getBatchDetail(BatchDetailQuery batchDetailQuery) {
        if (batchDetailQuery.getPid() == null){
            throw new Exception("批次pid不能为空");
        }
        // 创建分页对象
        Page<BatchDetailDTO> page = new Page<>(batchDetailQuery.getPageIndex(), batchDetailQuery.getPageSize());
        // 调用mapper层获取批次详情列表数据（分页）
        Page<BatchDetailDTO> resultPage = batchInfoMapper.getBatchDetail(page, batchDetailQuery);
        
        // 为每个批次详情设置所属组织
        // 从当前用户获取组织名称
//        UserDTO user = userHolder.getCurrentUser();
//        resultPage.getRecords().forEach(dto -> dto.setFrame(user != null ? user.getFrameName() : "默认组织"));
        resultPage.getRecords().forEach(dto -> dto.setFrame("默认组织"));

        return PageDTO.create(resultPage);
    }

    /**
     * 将BatchListDTO转换为扁平化的导出DTO列表 - 改进版本
     */
    private List<BatchListExportDTO> convertToExportDTO(List<BatchListDTO> batchList) {
        List<BatchListExportDTO> exportList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        log.info("开始转换数据，总商品数: {}", batchList.size());

        for (BatchListDTO batch : batchList) {
            log.debug("处理商品: {}, hasAttr: {}", batch.getName(), batch.getHasAttr());

            // 标记是否已添加该商品的任何数据
            boolean hasAddedAnyRow = false;

            // 情况1：有属性的商品
            if (Boolean.TRUE.equals(batch.getHasAttr()) && batch.getAttrBatches() != null) {
                for (BatchAttrDTO attrBatch : batch.getAttrBatches()) {
                    String attrName = attrBatch.getAttrName() != null ? attrBatch.getAttrName() : "";

                    // 情况1.1：属性下有批次信息
                    if (attrBatch.getBatches() != null && !attrBatch.getBatches().isEmpty()) {
                        for (BatchNumberDTO batchNumber : attrBatch.getBatches()) {
                            String batchNum = batchNumber.getBatchNumber() != null ? batchNumber.getBatchNumber() : "";

                            // 情况1.1.1：批次下有明细单据
                            if (batchNumber.getBatchDocuments() != null && !batchNumber.getBatchDocuments().isEmpty()) {
                                for (BatchDocumentDTO doc : batchNumber.getBatchDocuments()) {
                                    BatchListExportDTO exportDTO = createExportDTO(batch, attrName, batchNum, doc, formatter);
                                    exportList.add(exportDTO);
                                    hasAddedAnyRow = true;
                                }
                            } else {
                                // 情况1.1.2：批次下没有明细单据，但有批次汇总信息
                                BatchListExportDTO exportDTO = createExportDTO(batch, attrName, batchNum, null, formatter);
                                exportDTO.setStock(batchNumber.getTotalStock() != null ? batchNumber.getTotalStock() : BigDecimal.ZERO);
                                exportDTO.setIsWarning(Boolean.TRUE.equals(batchNumber.getIsWarning()) ? "是" : "否");
                                exportList.add(exportDTO);
                                hasAddedAnyRow = true;
                            }
                        }
                    } else {
                        // 情况1.2：属性下没有批次信息，显示属性库存
                        BatchListExportDTO exportDTO = createExportDTO(batch, attrName, "", null, formatter);
                        exportDTO.setStock(attrBatch.getAttrStock() != null ? attrBatch.getAttrStock() : BigDecimal.ZERO);
                        exportDTO.setIsWarning("否");
                        exportList.add(exportDTO);
                        hasAddedAnyRow = true;
                    }
                }
            }

            // 情况2：无属性的商品
            if (batch.getNoAttrBatches() != null && !batch.getNoAttrBatches().isEmpty()) {
                for (BatchNumberDTO batchNumber : batch.getNoAttrBatches()) {
                    String batchNum = batchNumber.getBatchNumber() != null ? batchNumber.getBatchNumber() : "";

                    // 情况2.1：批次下有明细单据
                    if (batchNumber.getBatchDocuments() != null && !batchNumber.getBatchDocuments().isEmpty()) {
                        for (BatchDocumentDTO doc : batchNumber.getBatchDocuments()) {
                            BatchListExportDTO exportDTO = createExportDTO(batch, "", batchNum, doc, formatter);
                            exportList.add(exportDTO);
                            hasAddedAnyRow = true;
                        }
                    } else {
                        // 情况2.2：批次下没有明细单据，但有批次汇总信息
                        BatchListExportDTO exportDTO = createExportDTO(batch, "", batchNum, null, formatter);
                        exportDTO.setStock(batchNumber.getTotalStock() != null ? batchNumber.getTotalStock() : BigDecimal.ZERO);
                        exportDTO.setIsWarning(Boolean.TRUE.equals(batchNumber.getIsWarning()) ? "是" : "否");
                        exportList.add(exportDTO);
                        hasAddedAnyRow = true;
                    }
                }
            }

            // 情况3：既没有属性信息也没有批次信息，至少要显示商品基本信息
            if (!hasAddedAnyRow) {
                BatchListExportDTO exportDTO = createExportDTO(batch, "", "", null, formatter);
                exportDTO.setStock(batch.getTotalStock() != null ? batch.getTotalStock() : BigDecimal.ZERO);
                exportDTO.setIsWarning("否");
                exportList.add(exportDTO);
                log.debug("商品 {} 没有属性和批次信息，添加基本信息行", batch.getName());
            }
        }

        log.info("数据转换完成，导出行数: {}", exportList.size());
        return exportList;
    }

    /**
     * 创建导出对象 - 改进版本，确保所有字段都有值
     */
    private BatchListExportDTO createExportDTO(
            BatchListDTO batch,
            String attrName,
            String batchNumber,
            BatchDocumentDTO doc,
            DateTimeFormatter formatter) {

        BatchListExportDTO exportDTO = new BatchListExportDTO();

        // 商品基本信息 - 确保不为null，使用空字符串作为默认值
        exportDTO.setName(batch.getName() != null ? batch.getName() : "");
        exportDTO.setNumber(batch.getNumber() != null ? batch.getNumber() : "");
        exportDTO.setSpec(batch.getSpec() != null ? batch.getSpec() : "");
        exportDTO.setBrand(batch.getBrand() != null ? batch.getBrand() : "");
        exportDTO.setUnit(batch.getUnit() != null ? batch.getUnit() : "");
        exportDTO.setCode(batch.getCode() != null ? batch.getCode() : "");
        exportDTO.setCategoryName(batch.getCategoryName() != null ? batch.getCategoryName() : "");
        exportDTO.setProtect(batch.getProtect() != null ? batch.getProtect() : 0);
        exportDTO.setThreshold(batch.getStock() != null ? batch.getStock() : BigDecimal.ZERO);
        exportDTO.setData(batch.getData() != null ? batch.getData() : "");

        // 属性信息 - 空属性显示"无属性"
        exportDTO.setAttrName(attrName != null && !attrName.isEmpty() ? attrName : "无属性");

        // 批次号
        exportDTO.setBatchNumber(batchNumber != null ? batchNumber : "");

        // 批次明细信息
        if (doc != null) {
            // 有具体单据明细
            exportDTO.setProductDate(doc.getProductDate() != null ?
                    doc.getProductDate().format(formatter) : "");
            exportDTO.setExpireDate(doc.getExpireDate() != null ?
                    doc.getExpireDate().format(formatter) : "");
            exportDTO.setStock(doc.getNums() != null ? doc.getNums() : BigDecimal.ZERO);
            exportDTO.setIsWarning(Boolean.TRUE.equals(doc.getIsWarning()) ? "是" : "否");
        } else {
            // 没有具体单据明细，使用默认值
            exportDTO.setProductDate("");
            exportDTO.setExpireDate("");
            // stock 由调用方设置
            if (exportDTO.getStock() == null) {
                exportDTO.setStock(BigDecimal.ZERO);
            }
            // isWarning 由调用方设置
            if (exportDTO.getIsWarning() == null) {
                exportDTO.setIsWarning("否");
            }
        }

        return exportDTO;
    }

    /**
     * 导出批次列表Excel - 修复版本
     */
    @Override
    public ByteArrayOutputStream exportBatchListExcel(BatchQuery query) throws IOException {
        try {
            log.info("=== 开始导出批次列表Excel ===");
            log.info("原始查询条件: {}", query);

            // 保存原始的分页参数
            Long originalPageIndex = query.getPageIndex();
            Long originalPageSize = query.getPageSize();

            // 修改为大分页获取所有数据
            query.setPageIndex(1L);
            query.setPageSize(10000L);

            log.info("修改后的查询条件（用于导出）: pageIndex={}, pageSize={}",
                    query.getPageIndex(), query.getPageSize());

            // 直接使用传入的query对象查询，这样可以保持所有条件和内部处理逻辑
            log.info("开始查询批次数据...");
            PageDTO<BatchListDTO> batchData = this.listBatch(query);

            // 恢复原始分页参数（避免影响外部）
            query.setPageIndex(originalPageIndex);
            query.setPageSize(originalPageSize);

            List<BatchListDTO> dataList = batchData.getRows();
            log.info("查询到 {} 条商品数据，总记录数: {}",
                    dataList != null ? dataList.size() : 0,
                    batchData.getTotal());

            if (dataList == null || dataList.isEmpty()) {
                log.warn("批次列表数据为空，将导出空Excel");
                dataList = new ArrayList<>();
            }

            // 转换为扁平化的导出DTO
            log.info("开始转换数据为导出格式...");
            List<BatchListExportDTO> exportList = convertToExportDTO(dataList);
            log.info("转换完成，共 {} 行导出数据", exportList.size());

            // 打印前几行数据用于调试
            if (!exportList.isEmpty()) {
                log.info("导出数据预览（前3行）:");
                for (int i = 0; i < Math.min(3, exportList.size()); i++) {
                    log.info("  第{}行: 商品={}, 属性={}, 批次={}, 库存={}",
                            i + 1,
                            exportList.get(i).getName(),
                            exportList.get(i).getAttrName(),
                            exportList.get(i).getBatchNumber(),
                            exportList.get(i).getStock());
                }
            }

            // 使用EasyExcel组件导出数据
            log.info("开始生成Excel文件...");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            easyExcelComponent.export("批次列表", outputStream, BatchListExportDTO.class, exportList);

            log.info("=== 成功导出批次列表Excel，共 {} 条记录，文件大小: {} bytes ===",
                    exportList.size(), outputStream.size());
            return outputStream;

        } catch (Exception e) {
            log.error("导出批次列表Excel失败", e);
            throw new IOException("导出批次列表Excel失败: " + e.getMessage(), e);
        }
    }



    /**
     * 导出批次详情到Excel
     * @param query 批次详情查询条件
     * @return Excel文件字节流
     * @throws IOException IO异常
     */
    @Override
    public ByteArrayOutputStream exportBatchDetailExcel(BatchDetailQuery query) throws IOException {
        try {
            log.info("开始导出批次详情Excel，批次ID: {}", query.getPid());

            // 设置一个较大的分页查询，获取所有数据用于导出
            BatchDetailQuery exportQuery = new BatchDetailQuery();
            exportQuery.setPageIndex(1L);
            exportQuery.setPageSize(10000L);  // 设置一个较大的数字获取所有数据
            exportQuery.setPid(query.getPid());  // 设置批次ID

            // 复制原查询条件的过滤参数
            if (query.getNumber() != null) {
                exportQuery.setNumber(query.getNumber());
            }
            if (query.getType() != null) {
                exportQuery.setType(query.getType());
            }
            if (query.getStartTime() != null) {
                exportQuery.setStartTime(query.getStartTime());
            }
            if (query.getEndTime() != null) {
                exportQuery.setEndTime(query.getEndTime());
            }

            // 查询数据
            PageDTO<BatchDetailDTO> detailData = this.getBatchDetail(exportQuery);
            List<BatchDetailDTO> dataList = detailData.getRows();

            if (dataList == null || dataList.isEmpty()) {
                log.warn("批次详情数据为空，将导出空Excel");
                dataList = new ArrayList<>();
            }

            // 使用EasyExcel组件导出数据
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            easyExcelComponent.export("批次详情", outputStream, BatchDetailDTO.class, dataList);

            log.info("成功导出批次详情Excel，共{}条记录", dataList.size());
            return outputStream;

        } catch (Exception e) {
            log.error("导出批次详情Excel失败", e);
            throw new IOException("导出批次详情Excel失败: " + e.getMessage(), e);
        }
    }
}
