package com.zeroone.star.storemanagement.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.query.j2.store.TransferQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.entity.*;
import com.zeroone.star.storemanagement.mapper.*;
import com.zeroone.star.storemanagement.service.ITransferService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferServiceImpl implements ITransferService {
    private final SwapMapper swapMapper;
    private final SwapInfoMapper swapInfoMapper;
    private final RoomMapper roomMapper;
    private final RoomInfoMapper roomInfoMapper;
    private final BatchMapper batchMapper;
    private final BatchInfoMapper batchInfoMapper;
    private final CostMapper costMapper;
    private final LogMapper logMapper;
    private final GoodsMapper goodsMapper;

    public TransferServiceImpl(SwapMapper swapMapper, SwapInfoMapper swapInfoMapper,
                               RoomMapper roomMapper, RoomInfoMapper roomInfoMapper,
                               BatchMapper batchMapper, BatchInfoMapper batchInfoMapper,
                               CostMapper costMapper, LogMapper logMapper,
                               GoodsMapper goodsMapper) {
        this.swapMapper = swapMapper;
        this.swapInfoMapper = swapInfoMapper;
        this.roomMapper = roomMapper;
        this.roomInfoMapper = roomInfoMapper;
        this.batchMapper = batchMapper;
        this.batchInfoMapper = batchInfoMapper;
        this.costMapper = costMapper;
        this.logMapper = logMapper;
        this.goodsMapper = goodsMapper;
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    private String generateClassId() {
        return String.valueOf(System.currentTimeMillis() % 1000000);
    }

    @Override
    public JsonVO<TransferDetailListDTO> detailTransferListAchieve(String id) {
        //根据调拨单的id查询到调拨单详细信息
        TransferDetailListDTO transferDetailListDTO = swapInfoMapper.detailTransfer(id);
        return JsonVO.success(transferDetailListDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<PageDTO<TransferListDTO>> queryTransferListAchieve(TransferQuery transferQuery) {
        //联表查询出符合要求的swap数据并分页
        //1.定义分页对象
        // 1. 每页数据条数（pageSize）从查询参数中获取
        long pageSize = transferQuery.getPageSize();
        // 获取当前页码（默认第1页）
        long pageIndex =transferQuery.getPageIndex();
        Page<TransferListDTO> page = new Page<>(pageIndex, pageSize);
        // 2. 执行分页查询
        Page<TransferListDTO> resultPage = swapMapper.queryTransfer(page, transferQuery);

        // 3. 转换为PageDTO
        PageDTO<TransferListDTO> pageDTO = PageDTO.create(resultPage);

        // 4. 返回封装结果
        return JsonVO.success(pageDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> addTransferListAchieve(TransferDetailListDTO transferDetailListDTO) {
        //用来记录插入的重复数量
        int temp =0;
        //1.先进行初始化，把传入的数据传入到各个实体类中(这个传参并不包括id等)
        SwapInfoDO info = insertSwapInfoValue(new SwapInfoDO(), transferDetailListDTO);
        SwapDO swap = insertSwapValue(new SwapDO(), transferDetailListDTO);
        GoodsDO goods = insertGoodsValue(new GoodsDO(), transferDetailListDTO);
        //2.然后把dto没有但是不能为空的字段加上默认值
        swap.setValue();
        goods.setValue();
        /*接着先查要传入的数据数据库中是否已经存在，有则返回其id
        * 接着判断返回值是否为空，空则没有要插入,
        * 并将自动生成的id返回传给info对应的字段(会自动执行)
        * 有则无需插入，将id传入Swap_info对应的字段,并将记录重复数据的数量+1*/
        String swapId = swapMapper.selectSwap(swap);
        String goodsId = goodsMapper.selectGoods(goods);
        String infoId = swapInfoMapper.selectValue(info);
        if(swapId == null){
            swapMapper.insertSwap(swap);
            info.setPid(swap.getId());
        }else {
            info.setPid(swapId);
            temp++;
        }
        if(goodsId == null){
            goodsMapper.insertGoods(goods);
            info.setGoods(goods.getId());
        }else {
            info.setGoods(goodsId);
            temp++;
        }
        if(infoId == null || temp !=2){
            //执行到这一步说明三者至少有一者不重复，执行插入，返回添加成功
            swapInfoMapper.insertSwapInfo(info);
            return JsonVO.success("添加成功");
        }else{
            //执行到这一步说明插入的数据全是重复的数据，则返回无需添加
            return JsonVO.success("重复数据，无需添加");
        }
    }

    //根据传入的参数给Swap_info实体类赋值
    public SwapInfoDO insertSwapInfoValue(SwapInfoDO info,TransferDetailListDTO transferDetailListDTO) {
        info.setAttr(transferDetailListDTO.getAttr());
        info.setUnit(transferDetailListDTO.getUnit());
        info.setWarehouse(transferDetailListDTO.getWarehouse());
        info.setStorehouse(transferDetailListDTO.getStorehouse());

        info.setBatch(transferDetailListDTO.getBatch());
        info.setMfd(transferDetailListDTO.getMfd());

        info.setPrice(transferDetailListDTO.getPrice());
        info.setNums(transferDetailListDTO.getNums());
        info.setTotal(transferDetailListDTO.getTotal());
        info.setData(transferDetailListDTO.getData());
        return info;
    }
    //根据传入的参数给Swap实体类赋值
    public SwapDO insertSwapValue(SwapDO swapDO, TransferDetailListDTO transferDetailListDTO) {
        swapDO.setTime(transferDetailListDTO.getTime());
        swapDO.setNumber(transferDetailListDTO.getSwapNumber());
        swapDO.setTotal(transferDetailListDTO.getSwapTotal());
        swapDO.setCost(transferDetailListDTO.getCost());
        swapDO.setPeople(transferDetailListDTO.getPeople());
        swapDO.setData(transferDetailListDTO.getSwapData());
        return swapDO;
    }
    //根据传入的参数给Goods实体类赋值
    public GoodsDO insertGoodsValue(GoodsDO goodsDO, TransferDetailListDTO transferDetailListDTO) {
        goodsDO.setName(transferDetailListDTO.getName());
        goodsDO.setNumber(transferDetailListDTO.getNumber());
        goodsDO.setSpec(transferDetailListDTO.getSpec());
        return goodsDO;
    }

    @Override
    @SentinelResource(value = "modifyTransfer",
            blockHandler = "handleModifyTransferBlock",
            fallback = "handleModifyTransferFallback")
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> modifyTransfer(TransferDetailDTO dto) {
        try {
            // 1.查询调拨单当前状态
            String pid = swapInfoMapper.getSwapById(dto.getInfo().getId());
            Integer currentStatus = swapMapper.getStatusById(pid);
            if (pid == null || currentStatus == null) {
                return JsonVO.fail("调拨单不存在");
            }

            // 2.检查状态是否为未审核
            if (currentStatus != 0) {
                return JsonVO.fail("只能修改草稿状态的调拨单");
            }

            // 3.数据合法性校验
            if (!validateData(dto)) {
                return JsonVO.fail("数据不合法");
            }

            // 4 检查调出仓库和调入仓库是否相同
            if (dto.getInfo().getWarehouse().equals(dto.getInfo().getStorehouse())) {
                return JsonVO.fail("调出仓库和调入仓库不能相同");
            }

            // 5.计算单据费用总和
            BigDecimal totalCost = BigDecimal.ZERO;
            if (dto.getCost() != null) {
                for (TransferDetailDTO.costInfo costInfo : dto.getCost()) {
                    if (costInfo.getMoney() != null) {
                        totalCost = totalCost.add(costInfo.getMoney());
                    }
                }
            }

            // 6.更新调拨单信息
            SwapInfoDO swapInfoDO = new SwapInfoDO();

            if (dto.getInfo() != null) {
                TransferDetailDTO.Info info = dto.getInfo();
                // 设置 swapInfoDO
                swapInfoDO.setId(info.getId());
                if (info.getWarehouse() != null) {
                    swapInfoDO.setWarehouse(info.getWarehouse());
                }
                if (info.getStorehouse() != null) {
                    swapInfoDO.setStorehouse(info.getStorehouse());
                }
                swapInfoDO.setBatch(info.getBatch());
                swapInfoDO.setMfd(info.getMfd());
                swapInfoDO.setPrice(info.getPrice());
                swapInfoDO.setNums(info.getNums());
                swapInfoDO.setTotal(info.getPrice().multiply(info.getNums()));
                swapInfoDO.setData(info.getData());
            }

            // 7.更新 swap 表
            SwapDO swapDO = new SwapDO();
            // 设置 ID
            swapDO.setId(pid);

            // 设置其他字段
            if (dto.getClassInfo() != null) {
                swapDO.setTime(dto.getClassInfo().getTime().atStartOfDay());
                swapDO.setNumber(dto.getClassInfo().getNumber());
                swapDO.setTotal(swapInfoDO.getTotal());
                swapDO.setCost(totalCost); // 设置计算出的费用总和
                swapDO.setPeople(dto.getClassInfo().getPeople());
                swapDO.setLogistics(dto.getClassInfo().getLogistics());
                swapDO.setFile(dto.getClassInfo().getFile());
                swapDO.setData(dto.getClassInfo().getData());
            }

            // 处理单据费用
            if (dto.getClassInfo() == null) {
                return JsonVO.fail("单据信息不能为空");
            }
            if (dto.getCost() != null) {
                handleTransferCosts(pid, dto.getCost(), dto.getClassInfo().getTime());
            }

            int updateCount1 = swapMapper.updateSwap(swapDO, dto.getInfo().getPrice().multiply(dto.getInfo().getNums()));
            int updateCount2 = swapInfoMapper.updateSwap(swapInfoDO);
            int updateCount = Math.max(updateCount1, updateCount2);

            log.info("更新影响行数: swap={}, swap_info={}, final={}", updateCount1, updateCount2, updateCount);

            if (updateCount == 0) {
                return JsonVO.fail("更新调拨单失败");
            }

            // 记录日志操作
            LogDO logDO = new LogDO();
            logDO.setId(generateUniqueId());
            logDO.setTime(LocalDateTime.now());
            // TODO 获取当前用户信息
            logDO.setUser("测试用户");
            logDO.setInfo("修改调拨单" + dto.getClassInfo().getNumber());
            logMapper.insert(logDO);

            return JsonVO.success(dto.getInfo().getId());

        } catch (Exception e) {
            log.error("修改调拨单失败", e);
            return JsonVO.fail("修改调拨单失败: " + e.getMessage());
        }
    }

    /**
     * 处理单据费用
     */
    private void handleTransferCosts(String transferId, List<TransferDetailDTO.costInfo> costInfos, LocalDate transferTime) {
        // 1. 获取现有的费用列表
        List<CostDO> existingCosts = costMapper.findByTransferId(transferId);

        // 2. 构建新费用ID集合
        Set<String> newCostIds = costInfos.stream()
                .map(TransferDetailDTO.costInfo::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3. 删除不在新列表中的费用
        for (CostDO existing : existingCosts) {
            if (!newCostIds.contains(existing.getId())) {
                costMapper.deleteById(existing.getId());
                log.info("删除费用记录: id={}", existing.getId());
            }
        }

        // 4. 新增或更新费用
        for (TransferDetailDTO.costInfo costInfo : costInfos) {
            CostDO costDO = new CostDO();
            costDO.setId(costInfo.getId());
            costDO.setType("swap");  // 类型为调拨单
            costDO.setCls(transferId);   // 关联调拨单ID
            costDO.setTime(transferTime.atStartOfDay());
            costDO.setIet(costInfo.getIet());  // 支出类别
            costDO.setMoney(costInfo.getMoney());  // 金额
            costDO.setData(costInfo.getData());    // 备注
            costDO.setSettle(BigDecimal.ZERO);     // 结算金额，默认0
            costDO.setState(0);                    // 状态，默认0

            if (!costMapper.isExist(costDO.getId())) {
                costMapper.insert(costDO);
                log.info("新增费用记录: iet={}, money={}", costInfo.getIet(), costInfo.getMoney());
            } else {
                // 更新
                costDO.setId(costInfo.getId());
                costMapper.updateById(costDO);
                log.info("更新费用记录: id={}", costInfo.getId());
            }
        }
    }

    @Override
    @SentinelResource(value = "batchAuditTransfer",
            blockHandler = "handleBatchAuditBlock",
            fallback = "handleBatchAuditFallback")
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> batchAuditTransfer(BatchAuditTransferDTO dto) {
        try {
            List<String> ids = dto.getIds();
            Integer operation = dto.getOperation();

            if (operation != 0 && operation != 1) {
                return JsonVO.fail("操作参数错误，0-反审核，1-审核");
            }
            if (ids == null || ids.isEmpty()) {
                return JsonVO.fail("请选择要" + (operation == 1 ? "审核" : "反审核") + "的调拨单");
            }

            // 1. 批量查询调拨单基础信息
            List<Map<String, Object>> statusList = swapInfoMapper.getBatchTransferStatus(ids);
            Map<String, TransferBatchData> transferMap = new HashMap<>();

            for (Map<String, Object> record : statusList) {
                String id = String.valueOf(record.get("id"));
                String pid = String.valueOf(record.get("pid"));
                Integer status = Integer.parseInt(record.get("status").toString());
                transferMap.put(id, new TransferBatchData(id, pid, status));
            }

            // 2. 验证调拨单是否存在
            int existsCount = swapInfoMapper.getExistsCountByIds(ids);
            if (existsCount != ids.size()) {
                List<String> notFoundIds = new ArrayList<>(ids);
                notFoundIds.removeAll(transferMap.keySet());
                return JsonVO.fail("以下调拨单不存在: " + String.join(", ", notFoundIds));
            }

            // 3. 批量查询调拨单的number
            List<String> numbers = swapInfoMapper.getBatchTransferNumbers(ids);
            if (numbers.isEmpty()) {
                return JsonVO.fail("调拨单不存在");
            }

            // 4. 状态验证
            List<String> statusErrorIds = new ArrayList<>();
            for (TransferBatchData data : transferMap.values()) {
                if ((data.getStatus() ^ operation) != 1) {
                    data.setValid(false);
                    statusErrorIds.add(data.getId());
                }
            }
            if (!statusErrorIds.isEmpty()) {
                return JsonVO.fail("以下调拨单状态异常: " + String.join(", ", statusErrorIds));
            }

            // 5. 仓库相同性检查
            List<String> warehouseErrorIds = new ArrayList<>();
            for (TransferBatchData data : transferMap.values()) {
                if (data.getDetail().getWarehouse().equals(data.getDetail().getStorehouse())) {
                    data.setValid(false);
                    warehouseErrorIds.add(data.getId());
                }
            }
            if (!warehouseErrorIds.isEmpty()) {
                return JsonVO.fail("以下调拨单仓库相同: " + String.join(", ", warehouseErrorIds));
            }

            // 6. 批次存在性检查
            List<String> batchNos = transferMap.values().stream()
                    .map(data -> data.getDetail().getBatch())
                    .distinct()
                    .collect(Collectors.toList());
            List<String> existingBatchNos = batchMapper.getExistingBatchNos(batchNos);
            Set<String> existingBatchSet = new HashSet<>(existingBatchNos);

            List<String> batchErrorIds = new ArrayList<>();
            for (TransferBatchData data : transferMap.values()) {
                if (!existingBatchSet.contains(data.getDetail().getBatch())) {
                    data.setValid(false);
                    batchErrorIds.add(data.getId());
                }
            }
            if (!batchErrorIds.isEmpty()) {
                return JsonVO.fail("以下调拨单批次不存在: " + String.join(", ", batchErrorIds));
            }

            // 7. 库存充足性检查（仅审核操作）
            if (operation == 1) {
                List<String> stockErrorIds = validateBatchStock(transferMap.values());
                if (!stockErrorIds.isEmpty()) {
                    return JsonVO.fail("以下调拨单库存不足: " + String.join(", ", stockErrorIds));
                }
            }

            // 8. 执行批量处理
            List<TransferBatchData> validTransfers = transferMap.values().stream()
                    .filter(TransferBatchData::isValid)
                    .collect(Collectors.toList());

            List<String> processedPids = new ArrayList<>();

            for (TransferBatchData data : validTransfers) {
                try {
                    boolean result = operation == 1 ?
                            processTransferAudit(data.getDetail()) :
                            processTransferUnaudit(data.getDetail());

                    if (result) {
                        if (!processedPids.contains(data.getPid())) {
                            processedPids.add(data.getPid());
                        }
                    }
                } catch (Exception e) {
                    log.error("处理调拨单失败，ID: {}", data.getId(), e);
                    return JsonVO.fail("处理调拨单失败，ID: " + data.getId());
                }
            }

            // 9. 批量更新状态
            if (!processedPids.isEmpty()) {
                swapMapper.auditBatchStatus(processedPids, operation);
            }

            // 批量成功日志
            if (!numbers.isEmpty()) {
                String operationType = operation == 1 ? "审核调拨单" : "反审核调拨单";
                String info = "[" + String.join("|", numbers) + "]";

                LogDO logDO = new LogDO();
                logDO.setId(generateUniqueId());
                logDO.setTime(LocalDateTime.now());
                // TODO 获取当前用户信息
                logDO.setUser("测试用户");
                logDO.setInfo(operationType + info);
                logMapper.insert(logDO);

                log.info("记录操作日志: {} {}", operationType, info);
            }

            return JsonVO.success(ids.toString());

        } catch (Exception e) {
            log.error("批量审核调拨单失败", e);
            return JsonVO.fail("批量审核调拨单失败: " + e.getMessage());
        }
    }

    /**
     * 批量验证库存是否充足
     */
    private List<String> validateBatchStock(Collection<TransferBatchData> transfers) {
        List<String> errorIds = new ArrayList<>();

        // 准备批量查询参数
        List<StockQueryParam> batchStockParams = new ArrayList<>();
        List<StockQueryParam> roomStockParams = new ArrayList<>();

        for (TransferBatchData data : transfers) {
            batchStockParams.add(new StockQueryParam(
                    data.getDetail().getBatch(),
                    data.getDetail().getGoods(),
                    data.getDetail().getWarehouse()));
            roomStockParams.add(new StockQueryParam(
                    data.getDetail().getGoods(),
                    data.getDetail().getWarehouse()));
        }

        // 批量查询批次库存
        List<Map<String, Object>> batchStocks = batchMapper.getBatchStocks(batchStockParams);
        Map<String, BigDecimal> batchStockMap = new HashMap<>();
        for (Map<String, Object> stock : batchStocks) {
            String key = stock.get("batchNo") + "_" + stock.get("goodsId") + "_" + stock.get("warehouseId");
            batchStockMap.put(key, (BigDecimal) stock.get("nums"));
        }

        // 批量查询仓库库存
        List<Map<String, Object>> roomStocks = roomMapper.getRoomStocks(roomStockParams);
        Map<String, BigDecimal> roomStockMap = new HashMap<>();
        for (Map<String, Object> stock : roomStocks) {
            String key = stock.get("goods") + "_" + stock.get("warehouse");
            roomStockMap.put(key, (BigDecimal) stock.get("nums"));
        }

        // 验证每个调拨单
        for (TransferBatchData data : transfers) {
            SwapInfoDO detail = data.getDetail();
            String batchKey = detail.getBatch() + "_" + detail.getGoods() + "_" + detail.getWarehouse();
            String roomKey = detail.getGoods() + "_" + detail.getWarehouse();

            BigDecimal batchStock = batchStockMap.get(batchKey);
            BigDecimal roomStock = roomStockMap.get(roomKey);

            if (batchStock == null || batchStock.compareTo(detail.getNums()) < 0 ||
                    roomStock == null || roomStock.compareTo(detail.getNums()) < 0) {
                errorIds.add(data.getId());
            }
        }

        return errorIds;
    }

    @Data
    public static class StockQueryParam {
        private String batchNo;
        private String goodsId;
        private String warehouseId;

        // 批次库存构造函数
        public StockQueryParam(String batchNo, String goods, String warehouse) {
            this.batchNo = batchNo;
            this.goodsId = goods;
            this.warehouseId = warehouse;
        }

        // 仓库库存构造函数
        public StockQueryParam(String goods, String warehouse) {
            this.goodsId = goods;
            this.warehouseId = warehouse;
        }
    }

    /**
     * 处理调拨单审核（库存调拨）
     */
    private boolean processTransferAudit(SwapInfoDO transfer) {
        String batchNo = transfer.getBatch();
        String goodsId = transfer.getGoods();
        String fromWarehouse = transfer.getWarehouse();  // 调出仓库
        String toWarehouse = transfer.getStorehouse(); // 调入仓库
        LocalDate mfd = transfer.getMfd();
        LocalDateTime transferTime = swapMapper.getTime(transfer.getPid());
        BigDecimal nums = transfer.getNums();
        BigDecimal price = transfer.getPrice() != null ? transfer.getPrice() : BigDecimal.ZERO;
        String swapInfoId = transfer.getId();

        try {
            // 1.处理批次库存调拨
            boolean batchSuccess = processBatchTransfer(batchNo, goodsId, fromWarehouse, toWarehouse, nums, swapInfoId, mfd);
            if (!batchSuccess) {
                return false;
            }

            // 2.处理仓库总库存调拨
            boolean roomSuccess = processRoomTransfer(goodsId, fromWarehouse, toWarehouse, nums, swapInfoId, price, transferTime);
            if (!roomSuccess) {
                // 回滚批次库存
                processBatchTransfer(batchNo, goodsId, toWarehouse, fromWarehouse, nums, swapInfoId, mfd);
                return false;
            }

            log.info("调拨单审核成功，批次: {}，商品: {}，从仓库{}调拨到仓库{}，数量: {}",
                    batchNo, goodsId, fromWarehouse, toWarehouse, nums);
            return true;

        } catch (Exception e) {
            log.error("调拨单审核处理异常", e);
            return false;
        }
    }

    /**
     * 处理批次库存调拨
     */
    private boolean processBatchTransfer(String batchNo, String goodsId, String fromWarehouse,
                                         String toWarehouse, BigDecimal nums, String swapInfoId, LocalDate mfd) {
        // 1.减少源仓库的批次库存
        int updateSource = batchMapper.updateBatchStock(batchNo, goodsId, fromWarehouse, nums.negate());
        if (updateSource < 0) {
            log.error("减少源仓库批次库存失败，批次: {}，商品: {}，仓库: {}", batchNo, goodsId, fromWarehouse);
            return false;
        }

        // 2.记录批次出库流水
        String fromBatchId = batchMapper.getBatchId(batchNo, goodsId, fromWarehouse);
        BatchInfoDO batchInfoOut = createBatchInfo(generateUniqueId(), fromBatchId, "swapOut", generateClassId(),
                swapInfoId, 0, nums);
        if (fromBatchId != null) {
            batchInfoMapper.insert(batchInfoOut);
        }

        // 3.检查目标仓库是否已存在该批次
        BigDecimal targetStock = batchMapper.getBatchStock(batchNo, goodsId, toWarehouse);
        if (targetStock != null) {
            // 目标仓库已存在该批次，增加库存
            int updateTarget = batchMapper.updateBatchStock(batchNo, goodsId, toWarehouse, nums);
            if (updateTarget < 0) {
                log.error("增加目标仓库批次库存失败，批次: {}，商品: {}，仓库: {}", batchNo, goodsId, toWarehouse);
                // 回滚源仓库的库存减少
                batchMapper.updateBatchStock(batchNo, goodsId, fromWarehouse, nums);
                // 回滚批次出库流水
                batchInfoMapper.deleteById(batchInfoOut.getId());
                return false;
            }
        } else {
            // 目标仓库不存在该批次，创建新的批次记录
            String roomId = roomMapper.getRoomId(goodsId, toWarehouse);
            // 修改 time 为 swapInfo 里的 mfd(生产日期)
            boolean createSuccess = batchMapper.createBatchInTargetWarehouse(generateUniqueId(), roomId, toWarehouse, goodsId, batchNo, mfd, nums) > 0;
            if (!createSuccess) {
                log.error("在目标仓库创建批次失败，批次: {}，商品: {}，仓库: {}", batchNo, goodsId, toWarehouse);
                // 回滚源仓库的库存减少
                batchMapper.updateBatchStock(batchNo, goodsId, fromWarehouse, nums);
                // 回滚批次出库流水
                batchInfoMapper.deleteById(batchInfoOut.getId());
                return false;
            }
        }

        // 4.记录批次入库流水
        String toBatchId = batchMapper.getBatchId(batchNo, goodsId, toWarehouse);
        BatchInfoDO batchInfoIn = createBatchInfo(generateUniqueId(), toBatchId, "swapEnter", generateClassId(),
                swapInfoId, 1, nums);
        if (toBatchId != null) {
            batchInfoMapper.insert(batchInfoIn);
        }

        return true;
    }

    /**
     * 处理仓库总库存调拨
     */
    private boolean processRoomTransfer(String goodsId, String fromWarehouse, String toWarehouse,
                                        BigDecimal nums, String swapInfoId, BigDecimal price, LocalDateTime transferTime) {
        // 1.减少源仓库总库存
        int updateSourceRoom = roomMapper.updateRoomStock(goodsId, fromWarehouse, nums.negate());
        if (updateSourceRoom < 0) {
            log.error("减少源仓库总库存失败，商品: {}，仓库: {}", goodsId, fromWarehouse);
            return false;
        }

        // 2.记录仓库出库流水
        String fromRoomId = roomMapper.getRoomId(goodsId, fromWarehouse);
        RoomInfoDO roomInfoOut = createRoomInfo(generateUniqueId(), fromRoomId, "swapOut", generateClassId(),
                swapInfoId, LocalDateTime.now(), 0, price, nums);
        if (fromRoomId != null) {
            roomInfoMapper.insert(roomInfoOut);
        }

        // 3.检查目标仓库是否已存在该商品
        RoomDO targetRoom = roomMapper.getRoomByGoodsAndWarehouse(goodsId, toWarehouse);
        if (targetRoom != null) {
            // 目标仓库已存在该商品，增加库存
            int updateTargetRoom = roomMapper.updateRoomStock(goodsId, toWarehouse, nums);
            if (updateTargetRoom < 0) {
                log.error("增加目标仓库总库存失败，商品: {}，仓库: {}", goodsId, toWarehouse);
                // 回滚源仓库的库存减少
                roomMapper.updateRoomStock(goodsId, fromWarehouse, nums);
                // 回滚仓库出库流水
                roomInfoMapper.deleteById(roomInfoOut.getId());
                return false;
            }
        } else {
            // 目标仓库不存在该商品，创建新的库存记录
            String attr = swapInfoMapper.getAttrById(swapInfoId);
            boolean createRoomSuccess = roomMapper.createRoomInTargetWarehouse(generateUniqueId(), goodsId, toWarehouse, attr, nums) > 0;
            if (!createRoomSuccess) {
                log.error("在目标仓库创建库存记录失败，商品: {}，仓库: {}", goodsId, toWarehouse);
                // 回滚源仓库的库存减少
                roomMapper.updateRoomStock(goodsId, fromWarehouse, nums);
                return false;
            }
        }

        // 4.记录仓库入库流水
        String toRoomId = roomMapper.getRoomId(goodsId, toWarehouse);
        // 修改 time 为 swap 表里的 time（单据时间）
        RoomInfoDO roomInfoIn = createRoomInfo(generateUniqueId(), toRoomId, "swapEnter", "1",
                swapInfoId, transferTime, 1, price, nums);
        if (toRoomId != null) {
            roomInfoMapper.insert(roomInfoIn);
        }

        return true;
    }

    /**
     * 处理调拨单反审核（库存还原）
     */
    private boolean processTransferUnaudit(SwapInfoDO transfer) {
        String batchNo = transfer.getBatch();
        String goodsId = transfer.getGoods();
        String fromWarehouse = transfer.getWarehouse();  // 原调出仓库（现在要加回去）
        String toWarehouse = transfer.getStorehouse();   // 原调入仓库（现在要减回去）
        BigDecimal nums = transfer.getNums();
        String swapInfoId = transfer.getId();

        try {
            // 1.处理批次库存还原（反向调拨 - 源仓库与目标仓库调换）
            boolean batchSuccess = processBatchUnaudit(batchNo, goodsId, toWarehouse, fromWarehouse, nums, swapInfoId);
            if (!batchSuccess) {
                return false;
            }

            // 2.处理仓库总库存还原（反向调拨 - 源仓库与目标仓库调换）
            boolean roomSuccess = processRoomUnaudit(goodsId, toWarehouse, fromWarehouse, nums, swapInfoId);
            if (!roomSuccess) {
                // 回滚批次库存
                processBatchUnaudit(batchNo, goodsId, fromWarehouse, toWarehouse, nums, swapInfoId);
                return false;
            }

            log.info("调拨单反审核成功，批次: {}，商品: {}，从仓库{}还原到仓库{}，数量: {}",
                    batchNo, goodsId, toWarehouse, fromWarehouse, nums);
            return true;

        } catch (Exception e) {
            log.error("调拨单反审核处理异常", e);
            return false;
        }
    }

    /**
     * 处理批次库存反审核
     */
    private boolean processBatchUnaudit(String batchNo, String goodsId, String fromWarehouse,
                                        String toWarehouse, BigDecimal nums, String swapInfoId) {
        // 源仓库与目标仓库已调换

        // 1.删除批次流水记录
        int deleteBatchInfoCount = batchInfoMapper.deleteBySwapInfoId(swapInfoId);
        log.info("删除批次流水记录，调拨单ID: {}，删除记录数: {}", swapInfoId, deleteBatchInfoCount);

        // 2.减少源仓库的批次库存（反审核时从原调入仓库减少）
        int updateSource = batchMapper.updateBatchStock(batchNo, goodsId, fromWarehouse, nums.negate());
        if (updateSource < 0) {
            log.error("减少源仓库批次库存失败，批次: {}，商品: {}，仓库: {}", batchNo, goodsId, fromWarehouse);
            return false;
        }

        // 3.增加目标仓库的批次库存（反审核时加到原调出仓库）
        int updateTarget = batchMapper.updateBatchStock(batchNo, goodsId, toWarehouse, nums);
        if (updateTarget < 0) {
            log.error("增加目标仓库批次库存失败，批次: {}，商品: {}，仓库: {}", batchNo, goodsId, toWarehouse);
            // 回滚源仓库的库存减少
            batchMapper.updateBatchStock(batchNo, goodsId, fromWarehouse, nums);
            return false;
        }

        return true;
    }

    /**
     * 处理仓库总库存反审核
     */
    private boolean processRoomUnaudit(String goodsId, String fromWarehouse, String toWarehouse,
                                       BigDecimal nums, String swapInfoId) {
        // 源仓库与目标仓库已调换

        // 1.删除仓库流水记录
        int deleteRoomInfoCount = roomInfoMapper.deleteBySwapInfoId(swapInfoId);
        log.info("删除仓库流水记录，调拨单ID: {}，删除记录数: {}", swapInfoId, deleteRoomInfoCount);

        // 2.减少源仓库总库存（反审核时从原调入仓库减少）
        int updateSourceRoom = roomMapper.updateRoomStock(goodsId, fromWarehouse, nums.negate());
        if (updateSourceRoom < 0) {
            log.error("减少源仓库总库存失败，商品: {}，仓库: {}", goodsId, fromWarehouse);
            return false;
        }

        // 3.增加目标仓库总库存（反审核时加到原调出仓库）
        int updateTargetRoom = roomMapper.updateRoomStock(goodsId, toWarehouse, nums);
        if (updateTargetRoom < 0) {
            log.error("增加目标仓库总库存失败，商品: {}，仓库: {}", goodsId, toWarehouse);
            // 回滚源仓库的库存减少
            roomMapper.updateRoomStock(goodsId, fromWarehouse, nums);
            return false;
        }

        return true;
    }

    /**
     * 创建批次流水记录
     */
    private BatchInfoDO createBatchInfo(String id, String pid, String type, String classId,
                                        String info, Integer direction, BigDecimal nums) {
        BatchInfoDO batchInfo = new BatchInfoDO();
        batchInfo.setId(id);
        batchInfo.setPid(pid);
        batchInfo.setType(type);
        batchInfo.setCls(classId);
        batchInfo.setInfo(info);
        batchInfo.setDirection(direction);
        batchInfo.setNums(nums);
        return batchInfo;
    }

    /**
     * 创建仓库流水记录
     */
    private RoomInfoDO createRoomInfo(String id, String pid, String type, String classId,
                                      String info, LocalDateTime time, Integer direction, BigDecimal price, BigDecimal nums) {
        RoomInfoDO roomInfo = new RoomInfoDO();
        roomInfo.setId(id);
        roomInfo.setPid(pid);
        roomInfo.setType(type);
        roomInfo.setCls(classId);
        roomInfo.setInfo(info);
        roomInfo.setTime(time);
        roomInfo.setDirection(direction);
        roomInfo.setPrice(price);
        roomInfo.setNums(nums);
        return roomInfo;
    }

    @Override
    @SentinelResource(value = "deleteTransfer",
            blockHandler = "handleDeleteTransferBlock",
            fallback = "handleDeleteTransferFallback")
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> deleteTransfer(RemoveTransferDTO dto) {
        try {
            List<Integer> ids = dto.getIds();
            if (ids == null || ids.isEmpty()) {
                return JsonVO.fail("请选择要删除的调拨单");
            }

            // 转换ID类型
            List<String> idStrs = ids.stream().map(String::valueOf).collect(Collectors.toList());

            // 批量获取主表 ID 和 number
            List<String> pidList = swapInfoMapper.getSwapByIds(idStrs);
            List<String> numbers = swapInfoMapper.getBatchTransferNumbers(idStrs);

            if (numbers.isEmpty()) {
                return JsonVO.fail("调拨单不存在");
            }

            // 批量查询状态
            List<Map<String, Object>> statusList = swapMapper.getStatusByIds(pidList);
            Map<String, Integer> statusMap = statusList.stream()
                    .collect(Collectors.toMap(
                            record -> String.valueOf(record.get("id")),
                            record -> Integer.parseInt(String.valueOf(record.get("status")))
                    ));

            // 检查状态
            List<String> cannotDeletePids = statusMap.entrySet().stream()
                    .filter(entry -> entry.getValue() != 0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (!cannotDeletePids.isEmpty()) {
                return JsonVO.fail("只能删除草稿状态的调拨单");
            }

            // 批量删除
            int deleteInfoCount = swapInfoMapper.deleteBatchIds(ids);
            int deleteMainCount = swapMapper.deleteBatchIds(pidList);

            // 删除费用记录
            for (String pid : pidList) {
                costMapper.deleteByTransferId(pid);
            }

            // 记录单条日志
            String info = "[" + String.join("|", numbers) + "]";

            LogDO logDO = new LogDO();
            logDO.setId(generateUniqueId());
            logDO.setTime(LocalDateTime.now());
            // TODO 获取当前用户信息
            logDO.setUser("测试用户");
            logDO.setInfo("删除调拨单" + info);
            logMapper.insert(logDO);

            log.info("记录删除日志: 删除调拨单{}", info);

            log.info("删除调拨单成功: 详情记录 {} 条, 主表记录 {} 条", deleteInfoCount, deleteMainCount);
            return JsonVO.success(ids.toString());

        } catch (Exception e) {
            log.error("删除调拨单失败", e);
            return JsonVO.fail("删除调拨单失败: " + e.getMessage());
        }
    }

    /**
     * 数据合法性校验
     */
    private boolean validateData(TransferDetailDTO dto) {
        if (dto == null || dto.getInfo() == null) {
            return false;
        }
        if (dto.getInfo().getId() == null || dto.getInfo().getId().trim().isEmpty()) {
            return false;
        }
        if (dto.getInfo().getPrice() != null && dto.getInfo().getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return dto.getInfo().getNums() == null || dto.getInfo().getNums().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Data
    private static class TransferBatchData {
        private String id;
        private String pid;
        private Integer status;
        private SwapInfoDO detail;
        private boolean valid = true;

        public TransferBatchData(String id, String pid, Integer status) {
            this.id = id;
            this.pid = pid;
            this.status = status;
        }
    }

    // 限流处理方法
    public JsonVO<String> handleModifyTransferBlock(TransferDetailDTO dto, BlockException ex) {
        log.warn("modifyTransfer接口被限流", ex);
        return JsonVO.fail("系统繁忙，请稍后再试");
    }

    public JsonVO<String> handleBatchAuditBlock(BatchAuditTransferDTO dto, BlockException ex) {
        log.warn("batchAuditTransfer接口被限流", ex);
        return JsonVO.fail("系统繁忙，请稍后再试");
    }

    public JsonVO<String> handleDeleteTransferBlock(RemoveTransferDTO dto, BlockException ex) {
        log.warn("deleteTransfer接口被限流", ex);
        return JsonVO.fail("系统繁忙，请稍后再试");
    }

    // 降级处理方法
    public JsonVO<String> handleModifyTransferFallback(TransferDetailDTO dto, Throwable ex) {
        log.error("modifyTransfer接口降级", ex);
        return JsonVO.fail("服务暂时不可用，请稍后再试");
    }

    public JsonVO<String> handleBatchAuditFallback(BatchAuditTransferDTO dto, Throwable ex) {
        log.error("batchAuditTransfer接口降级", ex);
        return JsonVO.fail("服务暂时不可用，请稍后再试");
    }

    public JsonVO<String> handleDeleteTransferFallback(RemoveTransferDTO dto, Throwable ex) {
        log.error("deleteTransfer接口降级", ex);
        return JsonVO.fail("服务暂时不可用，请稍后再试");
    }
}
