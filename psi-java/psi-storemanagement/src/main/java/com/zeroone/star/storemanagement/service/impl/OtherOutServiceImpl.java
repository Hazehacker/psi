package com.zeroone.star.storemanagement.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.CostDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListDTO;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.project.query.j2.store.OtherOutQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.convertor.ExtryConverter;
import com.zeroone.star.storemanagement.convertor.MsExtryMapper;
import com.zeroone.star.storemanagement.entity.*;
import com.zeroone.star.storemanagement.mapper.*;
import com.zeroone.star.storemanagement.service.IOtherOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class  OtherOutServiceImpl extends ServiceImpl<OtherOutMapper, ExtryDO> implements IOtherOutService {


    @Resource
    private OtherOutMapper otherOutMapper;
    @Resource
    private OtherOutInfoMapper otherOutInfoMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private CostMapper costMapper;
    @Resource
    private BatchMapper batchMapper;
    @Resource
    private BatchInfoMapper batchInfoMapper;
    @Resource
    private LogMapper logMapper;
    @Resource
    private SummaryMapper summaryMapper;
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private ServeMapper serveMapper;
    @Resource
    private ServeInfoMapper serveInfoMapper;
    @Resource
    private UserHolder userHolder;
    @Resource
    ExtryConverter extryConverter;

    @Resource
    MsExtryMapper ms;
    private final Snowflake snowflake = IdUtil.getSnowflake();
    @Override
    @Transactional
    public void examine(List<Integer> ids) {
        //开发表业务版本1
        List<ExtryDO> extryDOS = otherOutMapper.selectBatchIds(ids);
        for (ExtryDO extryDO : extryDOS) {
            //1.取出出库单ID
           String extryId = extryDO.getId();
            //2.判断该出库单是否已经审核
            boolean flag = true;
            if (extryDO.getExamine() == 0) {
                //3.执行审核
                //4.根据Id查询该出库单每一个商品的类型，常规商品需要查询库存
                QueryWrapper<ExtryInfoDO> extryInfoDOQueryWrapper = new QueryWrapper<>();
                extryInfoDOQueryWrapper.eq("pid", extryId);
                List<ExtryInfoDO> extryInfoDOList = otherOutInfoMapper.selectList(extryInfoDOQueryWrapper);
                if (extryInfoDOList == null || extryInfoDOList.isEmpty()) {
                    throw new RuntimeException("出库单信息有误!");
                }
                for (ExtryInfoDO extryInfoDO : extryInfoDOList) {
                   String goodsId = extryInfoDO.getGoods();
                    QueryWrapper<GoodsDO> goodsQueryWrapper = new QueryWrapper<>();
                    goodsQueryWrapper.select("type").eq("id", goodsId);
                    GoodsDO goodsDO = goodsMapper.selectOne(goodsQueryWrapper);
                    if (goodsDO.getType() == 0) {
                        //.常规商品查询库存
                        String warehouse = extryInfoDO.getWarehouse();
                        QueryWrapper<RoomDO> roomDOQueryWrapper = new QueryWrapper<>();
                        List<RoomDO> roomDOS = roomMapper.selectList(
                                roomDOQueryWrapper
                                        .select("goods", "nums")
                                        .eq("warehouse", warehouse)
                                        .eq("goods", goodsId)
                                        .eq("attr", extryInfoDO.getAttr())
                        );
                        if (roomDOS.size() == 0) {
                            throw new RuntimeException("仓储信息不存在!");
                        }
                        //初始化库存
                        BigDecimal stock = BigDecimal.ZERO;
                        //获取该仓库中所有该类型商品的库存
                        for (RoomDO roomDO : roomDOS) {
                            stock = stock.add(roomDO.getNums());
                        }
                        if (stock.compareTo(extryInfoDO.getNums()) < 0) {
                            throw new RuntimeException("库存不足!");
                        }
                        //5.检查该出库单商品是否满足批次要求，如满足批次要求，则继续审核批次
                        if (extryInfoDO.getBatch() != null && !extryInfoDO.getBatch().isEmpty()) {
                            QueryWrapper<BatchDO> batchDOQueryWrapper = new QueryWrapper<>();
                            batchDOQueryWrapper.eq("warehouse", extryInfoDO.getWarehouse()).eq("number", extryInfoDO.getBatch());
                            BatchDO batchDO = batchMapper.selectOne(batchDOQueryWrapper);
                            if (batchDO == null) {
                                throw new RuntimeException("批次信息有误!");
                            }
                            boolean batchMatch = extryInfoDO.getBatch().equals(batchDO.getNumber()) &&
                                    Objects.equals(extryInfoDO.getMfd(), batchDO.getTime()) &&
                                    batchDO.getWarehouse().equals(extryInfoDO.getWarehouse()) ;

                            if (!batchMatch) {
                                throw new RuntimeException("批次信息不匹配!");
                            }
                            //批次审核完毕，减少批次库存，创建批次信息
                            batchDO.setNums(batchDO.getNums().subtract(extryInfoDO.getNums()));
                            batchMapper.updateById(batchDO);
                            BatchInfoDO batchInfoDO = new BatchInfoDO();
                            batchInfoDO.setPid(batchDO.getId());
                            batchInfoDO.setType("extry");
                            batchInfoDO.setCls(extryInfoDO.getPid());
                            batchInfoDO.setInfo(extryInfoDO.getId());
                            batchInfoDO.setDirection(0);
                            batchInfoDO.setNums(extryInfoDO.getNums());
                            batchInfoMapper.insert(batchInfoDO);
                        }
                        //6.常规商品出库后减少对应仓库中该商品库存
                        RoomDO roomDO = roomMapper.selectOne(new QueryWrapper<RoomDO>().eq("warehouse", extryInfoDO.getWarehouse())
                                .eq("goods", goodsId)
                                .eq("attr", extryInfoDO.getAttr())
                        );
                        BigDecimal roomStock = roomDO.getNums().subtract(extryInfoDO.getNums());
                        roomDO.setNums(roomStock);
                        roomMapper.updateById(roomDO);

                        //7.生成库存信息
                        String roomId = roomMapper.selectOne(new QueryWrapper<RoomDO>().eq("warehouse", extryInfoDO.getWarehouse())
                                .eq("goods", goodsId)
                                .eq("attr", extryInfoDO.getAttr())
                        ).getId();
                        RoomInfoDO roomInfoDO = new RoomInfoDO();
                        roomInfoDO.setPid(roomId);
                        roomInfoDO.setType("extry");
                        roomInfoDO.setCls(extryInfoDO.getPid());
                        roomInfoDO.setInfo(extryInfoDO.getId());
                        roomInfoDO.setTime(extryDO.getTime());
                        roomInfoDO.setDirection(0);
                        roomInfoDO.setPrice(extryInfoDO.getPrice());
                        roomInfoDO.setNums(extryInfoDO.getNums());
                        roomInfoMapper.insert(roomInfoDO);
                        //获取回调id
                        // 通过查询刚插入的记录获取 ID
                        RoomInfoDO insertedRoomInfo = roomInfoMapper.selectOne(new QueryWrapper<RoomInfoDO>()
                                .eq("pid", roomInfoDO.getPid())
                                .eq("info", roomInfoDO.getInfo())
                                .orderByDesc("id")
                                .last("LIMIT 1"));
                        String result = insertedRoomInfo.getId();
                        //8.生成收支单
                        SummaryDO summaryDO = new SummaryDO();
                        summaryDO.setId(result);
                        summaryDO.setPid(result);
                        summaryDO.setType("extry");
                        summaryDO.setCls(extryInfoDO.getPid());
                        summaryDO.setInfo(extryInfoDO.getId());
                        summaryDO.setTime(extryDO.getTime());
                        summaryDO.setGoods(goodsId);
                        summaryDO.setAttr(extryInfoDO.getAttr());
                        summaryDO.setWarehouse(extryInfoDO.getWarehouse());
                        summaryDO.setBatch(extryInfoDO.getBatch());
                        summaryDO.setMfd(LocalDate.from(extryInfoDO.getMfd()));
                        summaryDO.setDirection(0);
                        summaryDO.setPrice(extryInfoDO.getPrice());
                        summaryDO.setNums(extryInfoDO.getNums());
                        summaryDO.setUct(summaryDO.getPrice());
                        summaryDO.setBct(summaryDO.getPrice().multiply(summaryDO.getNums()));
                        summaryDO.setExist("[" + stock + "," + roomStock + "," + stock + "," + roomStock + "]");
                        summaryDO.setBalance("[" + stock.multiply(extryInfoDO.getPrice()) + ","
                                + roomStock.multiply(extryInfoDO.getPrice()) + ","
                                + stock.multiply(extryInfoDO.getPrice()) + ","
                                + roomStock.multiply(extryInfoDO.getPrice()) + "]"
                        );
                        summaryDO.setHandle("00.0000");
                        summaryMapper.insert(summaryDO);
                    }
                    //9.服务商品执行服务业务
                    else {
                        //为空时创建，否则更新
                        ServeDO serveDO = serveMapper.selectOne(new QueryWrapper<ServeDO>().eq("goods", goodsId));
                        if (serveDO == null) {
                            serveDO = new ServeDO();
                            serveDO.setGoods(goodsDO.getId());
                            serveDO.setAttr(extryInfoDO.getAttr());
                            serveDO.setNums(extryInfoDO.getNums());
                        } else {
                            serveDO.setNums(serveDO.getNums().add(extryInfoDO.getNums()));
                        }
                        serveMapper.updateById(serveDO);
                        ServeInfoDO serveInfoDO = new ServeInfoDO();
                        serveInfoDO.setPid(serveDO.getId());
                        serveInfoDO.setType("extry");
                        serveInfoDO.setCls(extryInfoDO.getPid());
                        serveInfoDO.setInfo(extryInfoDO.getId());
                        serveInfoDO.setPrice(extryInfoDO.getPrice());
                        serveInfoDO.setNums(extryInfoDO.getNums());
                        serveInfoDO.setTime(extryDO.getTime());
                        serveInfoMapper.insert(serveInfoDO);
                    }
                }
            } else {
                //10.执行反审核，返还库存，删除记录
                //定义审核状态
                flag = false;
                QueryWrapper<ExtryInfoDO> extryInfoDOQueryWrapper = new QueryWrapper<>();
                extryInfoDOQueryWrapper.eq("pid", extryId);
                List<ExtryInfoDO> extryInfoDOList = otherOutInfoMapper.selectList(extryInfoDOQueryWrapper);
                if (extryInfoDOList == null || extryInfoDOList.isEmpty()) {
                    throw new RuntimeException("出库单信息有误!");
                }
                for (ExtryInfoDO extryInfoDO : extryInfoDOList) {
                    GoodsDO goodsDO = goodsMapper.selectById(extryInfoDO.getGoods());
                    // 0=商品类型，返还仓储库存，如果是批次产品，返还批次库存
                    if (goodsDO.getType() == 0) {
                        RoomDO roomDO = roomMapper.selectOne(new QueryWrapper<RoomDO>().eq("warehouse", extryInfoDO.getWarehouse())
                                .eq("goods", extryInfoDO.getGoods())
                                .eq("attr", extryInfoDO.getAttr())
                        );
                        BigDecimal stock = roomDO.getNums();
                        roomDO.setNums(stock.add(extryInfoDO.getNums()));
                        roomMapper.updateById(roomDO);
                        RoomInfoDO roomInfoDO = roomInfoMapper.selectOne(new QueryWrapper<RoomInfoDO>().eq("pid", roomDO.getId()).eq("info",extryInfoDO.getId()));
                        roomInfoMapper.deleteById(roomInfoDO);
                        SummaryDO summaryDO = summaryMapper.selectOne(new QueryWrapper<SummaryDO>().eq("id", roomInfoDO.getId()));
                        summaryMapper.deleteById(summaryDO);
                        if (extryInfoDO.getBatch() != null && !extryInfoDO.getBatch().isEmpty()) {
                            QueryWrapper<BatchDO> batchDOQueryWrapper = new QueryWrapper<>();
                            batchDOQueryWrapper.eq("warehouse", extryInfoDO.getWarehouse()).eq("number", extryInfoDO.getBatch());
                            BatchDO batchDO = batchMapper.selectOne(batchDOQueryWrapper);
                            batchDO.setNums(batchDO.getNums().add(extryInfoDO.getNums()));
                            batchMapper.updateById(batchDO);
                            batchInfoMapper.delete(new QueryWrapper<BatchInfoDO>().eq("pid",batchDO.getId())
                                    .eq("type", "extry")
                                    .eq("info", extryInfoDO.getId())
                                    .eq("direction", 0)
                                    .eq("`class`",extryInfoDO.getPid())
                            );
                        }
                    } else {
                        ServeDO serveDO = serveMapper.selectOne(new QueryWrapper<ServeDO>().eq("goods", extryInfoDO.getGoods()));
                        serveDO.setNums(serveDO.getNums().subtract(extryInfoDO.getNums()));
                        serveMapper.updateById(serveDO);
                        ServeInfoDO serveInfoDO = serveInfoMapper.selectOne(new QueryWrapper<ServeInfoDO>().eq("info", extryInfoDO.getId()).eq("pid",serveDO.getId()));
                        serveInfoMapper.deleteById(serveInfoDO);
                    }
                }
            }
            //11.生成操作与日志记录
            update().set("examine", extryDO.getExamine() == 0 ? 1 : 0).eq("id", extryId).update();
            //添加记录和日志
            RecordDO recordDO = new RecordDO();
            recordDO.setId(String.valueOf(System.currentTimeMillis()));
            recordDO.setType("extry");
            recordDO.setSource(extryId);
            recordDO.setTime(LocalDateTime.now());
         /*   try {
                recordDO.setUser(userHolder.getCurrentUser().getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/
            recordDO.setUser("1");
            recordDO.setInfo(flag ? "审核单据" : "反审核单据");
            recordMapper.insert(recordDO);

            LogDO logDO = new LogDO();
            logDO.setId(String.valueOf(System.currentTimeMillis()));
            logDO.setTime(LocalDateTime.now());
           /* try {
                logDO.setUser(userHolder.getCurrentUser().getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/
            logDO.setUser("1");
            logDO.setInfo((flag ? "审核其他出库单" : "反审核其他出库单") + "[" + extryDO.getNumber() + "]");
            logMapper.insert(logDO);
        }
    }

    @Override
    @Transactional
    public void check(List<Integer> ids) {
        //开发表业务版本1
        List<ExtryDO> extryDOS = otherOutMapper.selectBatchIds(ids);
        for (ExtryDO extryDO : extryDOS) {
            if (extryDO != null) {
                String extryId = extryDO.getId();
                boolean flag = extryDO.getCheck() == 1;
                update().set("`check`", flag? 0:1).eq("id", extryId).update();
                //添加记录和日志
                RecordDO recordDO = new RecordDO();
                recordDO.setId(String.valueOf(System.currentTimeMillis()));
                recordDO.setType("extry");
                recordDO.setSource(String.valueOf(extryId));
                recordDO.setTime(LocalDateTime.now());
               /* try {
                    recordDO.setUser(userHolder.getCurrentUser().getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                recordDO.setUser("1");
                recordDO.setInfo(flag ? "核对单据" : "反核对单据");
                recordMapper.insert(recordDO);

                LogDO logDO = new LogDO();
                logDO.setId(String.valueOf(System.currentTimeMillis()));
                logDO.setTime(LocalDateTime.now());
             /*   try {
                    recordDO.setUser(userHolder.getCurrentUser().getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                logDO.setUser("1");
                logDO.setInfo((flag ? "核对其他出库单" : "反核对其他出库单") + "[" + extryDO.getNumber() + "]");
                logMapper.insert(logDO);
            }
        }
    }

    @Override
    public byte[] exportOrderList(List<String> ids) {
        // 判断传入ID列表是否为空
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("导出数据ID列表不能为空");
        }
        List<OtherOutListDTO> dataList = otherOutMapper.selectMainIds(ids);

        // 判断查询结果是否为空
        if (dataList == null || dataList.isEmpty()) {
            throw new RuntimeException("未查询到相关数据");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            EasyExcel.write(outputStream, OtherOutListDTO.class)
                    .sheet("其他出库单")
                    .doWrite(dataList);
            byte[] result = outputStream.toByteArray();
            log.info("生成Excel文件大小: {} 字节", result.length);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }


    @Override
    public byte[] exportOrderDetails(List<String> ids) {
        // 判断传入ID列表是否为空
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("导出数据ID列表不能为空");
        }
        List<OtherOutListInfoDTO> detailList = otherOutInfoMapper.selectByMainIds(ids);
        // 判断查询结果是否为空
        if (detailList == null || detailList.isEmpty()) {
            throw new RuntimeException("未查询到相关明细数据");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            EasyExcel.write(outputStream, OtherOutListInfoDTO.class)
                    .sheet("其他出库单明细")
                    .doWrite(detailList);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    @Override
    @Transactional
    public void importOrders(InputStream inputStream) throws Exception {
        List<ExtryDO> dataList = EasyExcel.read(inputStream)
                .head(ExtryDO.class)
                .sheet()
                .doReadSync();

        for (ExtryDO dto : dataList) {
            // 转换并保存入库
            ExtryDO entity = convertToEntity(dto);
            otherOutMapper.insert(entity);
        }
    }

    @Transactional
    @Override
    public JsonVO<String> addOtherOutList(OtherOutListDTO dto) {
        if (!checkPermission()) {
            return JsonVO.fail("用户无操作权限");
        }
        // 更新出库单
        ExtryDO extryDO = convertToExtryDO(dto);
        otherOutMapper.insert(extryDO);

        String extryId = extryDO.getId();
        System.out.println("生成的主表ID: " + extryId);

        // 检查是否有详情数据
        if (dto.getOtherOutListInfoDTOList() == null || dto.getOtherOutListInfoDTOList().isEmpty()) {
            return JsonVO.fail("出库单详情不能为空");
        }

        // 遍历所有详情项并插入
        for (OtherOutListInfoDTO detail : dto.getOtherOutListInfoDTOList()) {
            ExtryInfoDO extryInfoDO = ms.addDetailDtoToExtryInfo(detail); // 使用详情DTO转换
            extryInfoDO.setId(String.valueOf(snowflake.nextId()%100000000));
            extryInfoDO.setPid(extryId);

            // 调试输出
            System.out.println("插入详情: goods=" + extryInfoDO.getGoods() +
                    ", nums=" + extryInfoDO.getNums() +
                    ", price=" + extryInfoDO.getPrice());

            otherOutInfoMapper.insertByOne(extryInfoDO);
        }

        logOperation(String.valueOf(extryDO.getId()), "新增入库单");
        return JsonVO.success("添加成功");
    }

    @Override
    public JsonVO<OtherOutListInfoDTO> getOtherOutListInfo(String id) {
        // 1.检查用户权限 权限校验可以定义AOP切面实现
        if (!checkPermission()) {
            log.info("用户无操作权限");
        }
        // 2.判断入库单是否存在
        ExtryDO exist = otherOutMapper.selectById(id);
        if (exist == null) {
            log.info("出库单不存在");
        }
        ExtryInfoDO extryInfoDO = otherOutInfoMapper.selectById(id);
        // 3.查询入库单详细
        OtherOutListInfoDTO dto = ms.extryInfoToOtherOutListInfoDTO(extryInfoDO);
        // 4.记录操作日志
        logOperation(dto.getId(), "查询出库单详细");
        return JsonVO.success(dto);
    }

    @Override
    public JsonVO<PageDTO<OtherOutListDTO>> listOtherOut(OtherOutQuery query) {
        // 创建分页对象
        Page<ExtryDO> doPage = new Page<>(query.getPageIndex(), query.getPageSize());

        // 调用mapper进行分页查询
        Page<ExtryDO> doResult = otherOutMapper.selectOtherOutListPage(doPage, query);
        // 使用MapStruct 转换器进行批量转换
        List<OtherOutListDTO> dtoList = extryConverter.toDTOList(doResult.getRecords());
        // 构建返回的分页数据对象
        PageDTO<OtherOutListDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageSize(doResult.getSize());
        pageDTO.setTotal(doResult.getTotal());
        pageDTO.setRows(dtoList);
        log.info("查询其他入库单列表成功, 共{}条记录", doResult.getTotal());
        return JsonVO.success(pageDTO);
    }

    @Override
    @Transactional
    public List<String> deleteOtherOutList(List<Integer> ids) {
        // 用于记录成功删除的出库单编号
        List<String> deletedList = new ArrayList<>();

        // 1.判断出库单是否存在
        List<ExtryDO> existingLists = otherOutMapper.selectBatchIds(ids);
        if (existingLists.size() != ids.size()) {
            log.info("部分出库单不存在");
            return deletedList;
        }

        // 2.检查用户权限
        if (!checkPermission()) {
            log.info("用户无操作权限");
            return deletedList;
        }

        //3.判断出库单是否审核并删除未审核的
        int count = 0;
        for (ExtryDO extryDO : existingLists) {
            if (extryDO.getExamine() == 1) {
                log.info("出库单已审核，不能删除，单号: {}", extryDO.getNumber());
            } else {
                // 4.删除出库单
                otherOutMapper.deleteById(extryDO.getId());
                // 5.删除出库单详细
                otherOutInfoMapper.deleteById(extryDO.getId());
                count++;
                // 记录成功删除的出库单编号
                deletedList.add(extryDO.getNumber());
            }
        }

        // 5.记录删除结果
        if (count != ids.size()) {
            log.info("删除出库单完成，预期删除{}条，实际删除{}条", ids.size(), count);
        } else {
            log.info("成功删除{}条出库单", count);
        }
        return deletedList;
    }

    @Override
    public void updateOtherOutList(OtherOutListDTO otherOutListDTO) {
        // 1.判断出库单是否存在
        Integer examine = otherOutMapper.getExamineById(otherOutListDTO.getId());
        if (examine == null) {
            throw new RuntimeException("出库单不存在");
        }
        if (examine == 1) {
            throw new RuntimeException("出库单已审核,无法修改");
        }
        // 2.更新出库单
        ExtryDO extry = ms.otherOutListDtoToExtry(otherOutListDTO);
        otherOutMapper.updateById(extry);

        //3.获取出库单详情列表
        List<OtherOutListInfoDTO> otherOutListInfoDTOList = otherOutListDTO.getOtherOutListInfoDTOList();
        //判断是否为空
        if (otherOutListInfoDTOList == null || otherOutListInfoDTOList.isEmpty()) {
            throw new RuntimeException("出库单详情列表不能为空");
        }
        //4.删除原先的出库单详情数据
        otherOutInfoMapper.deleteById(otherOutListDTO.getId());
        //5.插入新的出库单详情数据
        List<ExtryInfoDO> extryInfoList = new ArrayList<>();
        for (OtherOutListInfoDTO otherOutListInfoDTO : otherOutListInfoDTOList) {
            ExtryInfoDO extryInfo = new ExtryInfoDO();
            BeanUtils.copyProperties(otherOutListInfoDTO, extryInfo);
            extryInfo.setPid(otherOutListDTO.getId());
            extryInfoList.add(extryInfo);
            extryInfo.setId(snowflake.nextIdStr());
        }
        otherOutInfoMapper.insertBatch(extryInfoList);

        //6.获取单据花费列表
        List<CostDTO> costDTOList = otherOutListDTO.getCostDTOList();
        //7.删除原先的单据花费数据
        costMapper.deleteBycls(otherOutListDTO.getId());

        //8.插入新的花费单据数据
        List<CostDO> costList = new ArrayList<>();
        for (CostDTO costDTO : costDTOList) {
            CostDO cost = new CostDO();
            BeanUtils.copyProperties(costDTO, cost);
            cost.setCls(otherOutListDTO.getId());
            cost.setType("extry");
            cost.setTime(extry.getTime());
            cost.setSettle(BigDecimal.valueOf(0.0000));
            cost.setState(0);
            cost.setId(snowflake.nextIdStr());
            costList.add(cost);
        }
        costMapper.insertBatch(costList);

        //9.更新操作日志表和单据记录表
        UserDTO userDTO = null;
        try {
            userDTO = userHolder.getCurrentUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LogDO log = new LogDO();
        log.setUser(userDTO.getUsername());
        log.setTime(extry.getTime());
        log.setInfo("更新其他出库单"+"["+otherOutListDTO.getNumber()+"]");
        log.setId(snowflake.nextIdStr());
        logMapper.insert(log);
        RecordDO record = new RecordDO();
        record.setUser(userDTO.getUsername());
        record.setType("extry");
        record.setSource(otherOutListDTO.getId());
        record.setTime(extry.getTime());
        record.setInfo("更新单据");
        record.setId(snowflake.nextIdStr());
        recordMapper.insert(record);

    }

    private ExtryDO convertToEntity(ExtryDO dto) {
        ExtryDO entity = new ExtryDO();
        entity.setId(dto.getId());
        entity.setCustomer(dto.getCustomer());
        entity.setFrame(dto.getFrame());
        entity.setTime(dto.getTime());
        entity.setNumber(dto.getNumber());
        entity.setType(dto.getType());
        entity.setTotal(dto.getTotal());
        entity.setCost(dto.getCost());
        entity.setPeople(dto.getPeople());
        entity.setLogistics(dto.getLogistics());
        entity.setFile(dto.getFile());
        entity.setData(dto.getData());
        entity.setMore(dto.getMore());
        entity.setExamine(dto.getExamine());
        entity.setCse(dto.getCse());
        entity.setCheck(dto.getCheck());
        entity.setUser(dto.getUser());
        return entity;
    }
    private ExtryDO convertToExtryDO(OtherOutListDTO dto) {
        ExtryDO entity = new ExtryDO();
        entity.setId(dto.getId());
        entity.setCustomer(dto.getCustomer());
        entity.setFrame(dto.getFrame());
        entity.setTime(dto.getTime());
        entity.setNumber(dto.getNumber());
        entity.setType(dto.getType());
        entity.setTotal(dto.getTotal());
        entity.setCost(dto.getCost());
        entity.setPeople(dto.getPeople());
        entity.setLogistics(dto.getLogistics());
        entity.setFile(dto.getFile());
        entity.setData(dto.getData());
        entity.setMore(dto.getMore());
        entity.setExamine(dto.getExamine());
        entity.setCse(dto.getCse());
        entity.setCheck(dto.getCheck());
        entity.setUser(dto.getUser());
        return entity;
    }
    private boolean checkPermission() {
        return true;
    }
    /**
     * 记录操作日志
     */
    private void logOperation(String transferId, String operation) {
        // TODO: 实现操作日志记录
        log.info("操作日志：入库单ID: {}, 操作: {}", transferId, operation);
    }


}



