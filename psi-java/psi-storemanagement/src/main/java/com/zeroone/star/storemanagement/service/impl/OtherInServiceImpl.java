package com.zeroone.star.storemanagement.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.components.easyexcel.ExcelReadListener;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j2.store.*;
import com.zeroone.star.project.query.j2.store.OtherInQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.convertor.EntryConverter;
import com.zeroone.star.storemanagement.convertor.MsEntryMapper;
import com.zeroone.star.storemanagement.entity.*;
import com.zeroone.star.storemanagement.mapper.*;
import com.zeroone.star.storemanagement.service.IOtherInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class OtherInServiceImpl extends ServiceImpl<OtherInMapper, EntryDO>  implements IOtherInService {

    @Resource
    OtherInMapper otherInMapper;

    @Resource
    OtherInInfoMapper otherInInfoMapper;

    @Resource
    CostMapper costMapper;

    @Resource
    LogMapper logMapper;

    @Resource
    RecordMapper recordMapper;

    @Resource
    RoomMapper roomMapper;

    @Resource
    RoomInfoMapper roomInfoMapper;

    @Resource
    private MsEntryMapper msEntryMapper;

    @Resource
    SummaryMapper summaryMapper;

    @Resource
    ServeMapper serveMapper;

    @Resource
    ServeInfoMapper serveInfoMapper;

    @Resource
    OtherInListMapper otherInListMapper;

    @Resource
    private UserHolder userHolder;

    @Resource
    EntryConverter entryConverter;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private FrameMapper frameMapper;

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private BatchMapper batchMapper;

    @Resource
    private BatchInfoMapper batchInfoMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    //雪花算法
    private final Snowflake snowflake = IdUtil.getSnowflake();
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    @Transactional
    public void updateOtherInList(OtherInListDetailDTO otherInListDetailDTO) {
        //1.判断入库单是否存在
        Integer examine = otherInMapper.getExamineById(otherInListDetailDTO.getId());
        if (examine == null) {
            throw new RuntimeException("入库单不存在");
        }
        if (examine == 1) {
            throw new RuntimeException("入库单已审核,无法修改");
        }
        //2.更新入库单
        EntryDO entry = msEntryMapper.otherInListDetailDtoToEntry(otherInListDetailDTO);
        otherInMapper.updateById(entry);


        //3.获取入库单详情列表
        List<OtherInListDetailInfoDTO> otherInListInfoDTOList = otherInListDetailDTO.getOtherInListDetailInfoDTOList();
        //判断是否为空
        if (otherInListInfoDTOList == null || otherInListInfoDTOList.isEmpty()) {
            throw new RuntimeException("入库单详情列表不能为空");
        }
        //4.删除原先的入库单详情数据
        otherInInfoMapper.deleteByPid(otherInListDetailDTO.getId());
        //5.插入新的入库单详情数据
        List<EntryInfoDO> entryInfoList = new ArrayList<>();
        for (OtherInListDetailInfoDTO otherInListInfoDTO : otherInListInfoDTOList) {
            EntryInfoDO entryInfo = new EntryInfoDO();
            BeanUtils.copyProperties(otherInListInfoDTO, entryInfo);
            entryInfo.setPid(otherInListDetailDTO.getId());
            entryInfoList.add(entryInfo);
            entryInfo.setId(snowflake.nextIdStr());
        }
        otherInInfoMapper.insertBatch(entryInfoList);


        //6.获取单据花费列表
        List<CostDTO> costDTOList = otherInListDetailDTO.getCostDTOList();
        //7.删除原先的单据花费数据
        costMapper.deleteBycls(otherInListDetailDTO.getId());
        //8.插入新的花费单据数据
        List<CostDO> costList = new ArrayList<>();
        for (CostDTO costDTO : costDTOList) {
            CostDO cost = new CostDO();
            BeanUtils.copyProperties(costDTO, cost);
            cost.setCls(otherInListDetailDTO.getId());
            cost.setType("entry");
            cost.setTime(entry.getTime());
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
        log.setTime(entry.getTime());
        log.setInfo("更新其他入库单"+"["+otherInListDetailDTO.getNumber()+"]");
        log.setId(snowflake.nextIdStr());
        logMapper.insert(log);
        RecordDO record = new RecordDO();
        record.setUser(userDTO.getUsername());
        record.setType("entry");
        record.setSource(otherInListDetailDTO.getId());
        record.setTime(entry.getTime());
        record.setInfo("更新单据");
        record.setId(snowflake.nextIdStr());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void examine(List<Integer> ids) {
        //1.判断入库单是否存在
        //查询id在ids中的入库单总数
        List<Integer> exmineStatusList = otherInMapper.getExamineByIds(ids);
        //判断入库单总数是否等于ids的长度
        if (exmineStatusList.size() != ids.size()) {
            throw new RuntimeException("有入库单不存在");
        }
        int status = exmineStatusList.get(0)^1;

        //获取所有id对应的entry表和entry_info表信息
        List<EntryDO> entryList = otherInMapper.getByIds(ids);
        Map<String, EntryDO> entryMap = entryList.stream()
                .collect(Collectors.toMap(EntryDO::getId, entryDO -> entryDO));
        List<EntryInfoDO> entryInfoList = otherInInfoMapper.getByPids(ids);
        //2.审核入库单
        if(status==1) {
            for (EntryInfoDO entryInfoDO : entryInfoList) {
                GoodsDO goodsDO = goodsMapper.selectById(entryInfoDO.getGoods());
                //3.审核服务
                if (goodsDO.getType().equals(1)) {
                    //4.获取服务，若服务不存在，则插入，否则更新
                    ServeDO serveDO = serveMapper.getByGoodsAndAttr(entryInfoDO.getGoods(), entryInfoDO.getAttr());
                    if (serveDO == null) {
                        serveDO = new ServeDO();
                        serveDO.setId(snowflake.nextIdStr());
                        serveDO.setGoods(entryInfoDO.getGoods());
                        serveDO.setAttr(entryInfoDO.getAttr());
                        serveDO.setNums(entryInfoDO.getNums());
                        serveMapper.insert(serveDO);
                    } else {
                        serveDO.setNums(serveDO.getNums().add(entryInfoDO.getNums()));
                        serveMapper.updateById(serveDO);
                    }
                    //5.插入服务详细信息
                    ServeInfoDO serveInfoDO = new ServeInfoDO();
                    serveInfoDO.setId(snowflake.nextIdStr());
                    serveInfoDO.setPid(serveDO.getId());
                    serveInfoDO.setType("entry");
                    serveInfoDO.setCls(entryInfoDO.getPid());
                    serveInfoDO.setInfo(entryInfoDO.getId());
                    serveInfoDO.setTime(entryMap.get(entryInfoDO.getPid()).getTime());
                    serveInfoDO.setPrice(entryInfoDO.getPrice());
                    serveInfoDO.setNums(entryInfoDO.getNums());
                    serveInfoMapper.insert(serveInfoDO);
                } else {//6.审核库存
                    //7.获取库存，若库存不存在，则插入，否则更新
                    RoomDO roomDO = roomMapper.getByGoodsAndWarehouseAndAttr(entryInfoDO.getWarehouse(), entryInfoDO.getGoods(), entryInfoDO.getAttr());
                    if (roomDO == null) {
                        roomDO = new RoomDO();
                        roomDO.setId(snowflake.nextIdStr());
                        roomDO.setWarehouse(entryInfoDO.getWarehouse());
                        roomDO.setGoods(entryInfoDO.getGoods());
                        roomDO.setAttr(entryInfoDO.getAttr());
                        roomDO.setNums(entryInfoDO.getNums());
                        roomMapper.insert(roomDO);
                    } else {
                        roomDO.setNums(roomDO.getNums().add(entryInfoDO.getNums()));
                        roomMapper.updateById(roomDO);
                    }
                    //8.插入库存详细信息
                    RoomInfoDO roomInfoDO = new RoomInfoDO();
                    roomInfoDO.setId(snowflake.nextIdStr());
                    roomInfoDO.setPid(String.valueOf(roomDO.getId()));
                    roomInfoDO.setType("entry");
                    roomInfoDO.setCls(entryInfoDO.getPid());
                    roomInfoDO.setInfo(entryInfoDO.getId());
                    roomInfoDO.setTime(entryMap.get(entryInfoDO.getPid()).getTime());
                    roomInfoDO.setDirection(1);
                    roomInfoDO.setPrice(entryInfoDO.getPrice());
                    roomInfoDO.setNums(entryInfoDO.getNums());
                    roomInfoMapper.insert(roomInfoDO);

                    if(goodsDO.getBatch().equals(1)){
                        BatchDO batchDO = batchMapper.getByRoomAndNumber(roomDO.getId(), entryInfoDO.getBatch());
                        if (batchDO == null) {
                            batchDO = new BatchDO();
                            batchDO.setId(snowflake.nextIdStr());
                            batchDO.setRoom(roomDO.getId());
                            batchDO.setWarehouse(roomDO.getWarehouse());
                            batchDO.setGoods(roomDO.getGoods());
                            batchDO.setNumber(entryInfoDO.getBatch());
                            batchDO.setTime(entryMap.get(entryInfoDO.getPid()).getTime());
                            batchDO.setNums(entryInfoDO.getNums());
                            batchMapper.insert(batchDO);
                        }else{
                            batchDO.setNums(batchDO.getNums().add(entryInfoDO.getNums()));
                            batchMapper.updateById(batchDO);
                        }
                        BatchInfoDO batchInfoDO = new BatchInfoDO();
                        batchInfoDO.setPid(batchDO.getId());
                        batchInfoDO.setType("entry");
                        batchInfoDO.setCls(entryInfoDO.getPid());
                        batchInfoDO.setInfo(entryInfoDO.getId());
                        batchInfoDO.setDirection(1);
                        batchInfoDO.setNums(entryInfoDO.getNums());
                        batchInfoDO.setId(snowflake.nextIdStr());
                        batchInfoMapper.insert(batchInfoDO);
                    }

                    //9.插入收发统计信息
                    SummaryDO summaryDO = new SummaryDO();
                    BeanUtils.copyProperties(roomInfoDO, summaryDO);
                    summaryDO.setId(snowflake.nextIdStr());
                    summaryDO.setPid(summaryDO.getId());
                    summaryDO.setGoods(String.valueOf(roomDO.getGoods()));
                    summaryDO.setAttr(roomDO.getAttr());
                    summaryDO.setWarehouse(String.valueOf(roomDO.getWarehouse()));
                    summaryDO.setBatch(entryInfoDO.getBatch());
                    summaryDO.setMfd(entryInfoDO.getMfd());
                    summaryDO.setUct(summaryDO.getPrice());
                    summaryDO.setBct(summaryDO.getPrice().multiply(summaryDO.getNums()));
                    summaryDO.setExist("[" + roomDO.getNums() +","+ roomDO.getNums() +","+ roomDO.getNums() +","+ roomDO.getNums() + "]");
                    int temp = roomDO.getNums().multiply(roomInfoDO.getPrice()).intValue();
                    summaryDO.setBalance("[" + temp+"," + temp +","+ temp +","+ temp + "]");
                    summaryDO.setHandle("0.0000");
                    summaryMapper.insert(summaryDO);
                }
            }
        }else{//10.反审核入库单
            for(EntryInfoDO entryInfoDO : entryInfoList){
                GoodsDO goodsDO = goodsMapper.selectById(entryInfoDO.getGoods());
                //11.获取服务并更新
                if(goodsDO.getType().equals(1)){
                    ServeDO serveDO = serveMapper.getByGoodsAndAttr(entryInfoDO.getGoods(), entryInfoDO.getAttr());
                    if(serveDO.getNums().equals(entryInfoDO.getNums())){
                        serveMapper.deleteById(serveDO.getId());
                    }else{
                        serveDO.setNums(serveDO.getNums().subtract(entryInfoDO.getNums()));
                        serveMapper.updateById(serveDO);
                    }
                    //12.删除服务详细信息
                    serveInfoMapper.deleteByInfo(entryInfoDO.getId());
                }else{
                    //13.获取库存并更新
                    RoomDO roomDO = roomMapper.getByGoodsAndWarehouseAndAttr(entryInfoDO.getWarehouse(), entryInfoDO.getGoods(), entryInfoDO.getAttr());
                    if(goodsDO.getBatch().equals(1)){
                        BatchDO batchDO = batchMapper.getByRoomAndNumber(roomDO.getId(), entryInfoDO.getBatch());
                        if(batchDO.getNums().equals(entryInfoDO.getNums())){
                            batchMapper.deleteById(batchDO.getId());
                        }else{
                            batchDO.setNums(batchDO.getNums().subtract(entryInfoDO.getNums()));
                            batchMapper.updateById(batchDO);
                        }
                        batchInfoMapper.deleteByInfo(entryInfoDO.getId());
                    }
                    if(roomDO.getNums().equals(entryInfoDO.getNums())){
                        roomMapper.deleteById(roomDO.getId());
                    }else{
                        roomDO.setNums(roomDO.getNums().subtract(entryInfoDO.getNums()));
                        roomMapper.updateById(roomDO);
                    }
                    //14.删除库存详细信息
                    roomInfoMapper.deleteByInfo(entryInfoDO.getId());
                    //15.删除收发统计信息
                    summaryMapper.deleteByInfo(entryInfoDO.getId());
                }
            }
        }
        //16.更新审核状态
        otherInMapper.updateExamine(ids,status);
        //17.更新日志表和单据记录表
        UserDTO userDTO = null;
        try {
            userDTO = userHolder.getCurrentUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<LogDO> logList = new ArrayList<>();
        for(EntryDO entry : entryList){
            LogDO log = new LogDO();
            log.setId(snowflake.nextIdStr());
            log.setUser(userDTO.getUsername());
            log.setTime(entry.getTime());
            log.setInfo("审核其他入库单"+"["+entry.getNumber()+"]");
            logList.add(log);
        }
        logMapper.insertBatch(logList);
        List<RecordDO> recordList = new ArrayList<>();
        for(EntryDO entry : entryList){
            RecordDO record = new RecordDO();
            record.setId(snowflake.nextIdStr());
            record.setUser(userDTO.getUsername());
            record.setType("entry");
            record.setSource(entry.getId());
            record.setTime(entry.getTime());
            record.setInfo("审核单据");
            recordList.add(record);
        }
        recordMapper.insertBatch(recordList);
    }

    @Override
    @Transactional
    public void check(List<Integer> ids) {
        //1.判断入库单是否存在并获取核对状态
        List<Integer> checkStatusList = otherInMapper.getCheckByIds(ids);
        if (checkStatusList.size() != ids.size()) {
            throw new RuntimeException("有入库单不存在");
        }
        int status = checkStatusList.get(0);
        //2.更新核对状态
        otherInMapper.updateCheck(ids, status ^ 1);

        //3.更新日志表和单据记录表
        UserDTO userDTO = null;
        try {
            userDTO = userHolder.getCurrentUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<EntryDO> entryList = otherInMapper.getByIds(ids);
        List<LogDO> logList = new ArrayList<>();
        for(EntryDO entry : entryList){
            LogDO log = new LogDO();
            log.setId(snowflake.nextIdStr());
            log.setUser(userDTO.getUsername());
            log.setTime(entry.getTime());
            log.setInfo("核对其他入库单"+"["+entry.getNumber()+"]");
            logList.add(log);
        }
        logMapper.insertBatch(logList);
        List<RecordDO> recordList = new ArrayList<>();
        for(EntryDO entry : entryList){
            RecordDO record = new RecordDO();
            record.setId(snowflake.nextIdStr());
            record.setUser(userDTO.getUsername());
            record.setType("entry");
            record.setSource(entry.getId());
            record.setTime(entry.getTime());
            record.setInfo("核对单据");
            recordList.add(record);
        }
        recordMapper.insertBatch(recordList);
    }

    @Override
    @Transactional
    public OtherInListDetailDTO getOtherInListDetail(String id) {
        // TODO 单个查询操作
        // 1.检查用户权限 权限校验可以定义AOP切面实现
        if (!checkPermission()) {
            log.info("用户无操作权限");
        }
        // 2.判断入库单是否存在
        EntryDO exist =  otherInMapper.selectById(id);
        if (exist == null) {
            log.info("入库单不存在");
        }
        // 3.查询入库单详细
        OtherInListDetailDTO dto = msEntryMapper.entryToOtherInListDetailDTO(exist);
        // 4.记录操作日志
        logOperation(dto.getId(), "查询入库单详细");
        return dto;
    }

    @Override
    @Transactional
    public JsonVO<String> saveOtherInList(OtherInListAddDTO dto) {
        // 1.参数校验
        if (validate(dto) != null) { // 没有返回错误信息即通过校验
            return JsonVO.fail("参数不合法");
        }
        // 2.检查用户权限
        if (!checkPermission()) {
            return JsonVO.fail("用户无操作权限");
        }
        // 3.新增入库单
        EntryDO entryDO = msEntryMapper.addDtoToEntry(dto);
        otherInMapper.insert(entryDO);

        String entryId = entryDO.getId();
        System.out.println("生成的主表ID: " + entryId);

        // 检查是否有详情数据
        if (dto.getOtherInListDetailInfoDTOList() == null || dto.getOtherInListDetailInfoDTOList().isEmpty()) {
            return JsonVO.fail("入库单详情不能为空");
        }

        // 遍历所有详情项并插入
        for (OtherInListDetailInfoDTO detail : dto.getOtherInListDetailInfoDTOList()) {
            EntryInfoDO entryInfoDO = msEntryMapper.addDetailDtoToEntryInfo(detail); // 使用详情DTO转换
            entryInfoDO.setPid(entryId);

            // 调试输出
            System.out.println("插入详情: goods=" + entryInfoDO.getGoods() +
                    ", nums=" + entryInfoDO.getNums() +
                    ", price=" + entryInfoDO.getPrice());

            otherInInfoMapper.insertByOne(entryInfoDO);
        }

        // 4.记录操作日志
        logOperation(String.valueOf(entryDO.getId()), "新增入库单");
        return JsonVO.success("新增入库单成功");
    }

    @Override
    @Transactional
    public List<String> removeOtherInList(List<Integer> ids) {
        System.out.println(ids);
        // 用于记录成功删除的入库单编号
        List<String> deletedList = new ArrayList<>();

        // 1.判断入库单是否存在
        List<EntryDO> existingLists = otherInMapper.selectBatchIds(ids);
        if (existingLists.size() != ids.size()) {
            log.info("部分入库单不存在");
            return deletedList;
        }

        // 2.检查用户权限
        if (!checkPermission()) {
            log.info("用户无操作权限");
            return deletedList;
        }

        // 3.判断入库单是否审核并删除未审核的
        int count = 0;
        for (EntryDO entryDO : existingLists) {
            if (entryDO.getExamine() == 1) {
                log.info("入库单已审核，不能删除，单号: {}", entryDO.getNumber());
            } else {
                // 4.删除入库单
                otherInInfoMapper.deleteById(entryDO.getId());
                // 5.删除入库单详细
                otherInMapper.deleteById(entryDO.getId());
                count++;
                // 记录成功删除的入库单编号
                deletedList.add(entryDO.getNumber());
            }
        }
        // 5.记录删除结果
        if (count != ids.size()) {
            log.info("删除入库单完成，预期删除{}条，实际删除{}条", ids.size(), count);
        } else {
            log.info("成功删除{}条入库单", count);
        }
        return deletedList;
    }


    /**
     * 获取其他入库单列表（条件查询+分页）
     * @param query 查询参数对象
     * @return 包含分页数据的JsonVO对象
     */
    @Override
    public JsonVO<PageDTO<OtherInListDTO>> getOtherInList(OtherInQuery query) {
        // 创建分页对象
        Page<EntryDO> doPage = new Page<>(query.getPageIndex(), query.getPageSize());
        // 调用mapper进行分页查询
        Page<EntryDO> doResult = otherInListMapper.selectOtherInListPage(doPage, query);
        // 使用MapStruct 转换器进行批量转换
        List<OtherInListDTO> dtoList = entryConverter.toDTOList(doResult.getRecords());
        // 构建返回的分页数据对象
        PageDTO<OtherInListDTO> pageDTO = new PageDTO<>();
        pageDTO.setTotal(doResult.getTotal());
        pageDTO.setRows(dtoList);
        pageDTO.setPageSize(doResult.getSize());
        log.info("查询其他入库单列表成功, 共{}条记录", doResult.getTotal());
        // 返回成功响应
        return JsonVO.success(pageDTO);
    }

    /**
     * 数据合法性校验
     */
    private String validate(OtherInListAddDTO dto) {
        if (dto == null) {
            return "参数不能为空";
        }

        // 必填字段校验
        if (dto.getTime() == null) {
            return "单据日期不能为空";
        }
        if (StringUtils.isBlank(dto.getNumber())) {
            return "单据编号不能为空";
        }
        if (dto.getType() == null) {
            return "单据类型不能为空";
        }
        if (dto.getTotal() == null) {
            return "单据成本不能为空";
        }
        if (dto.getCost() == null) {
            return "单据费用不能为空";
        }
        if (dto.getExamine() == null) {
            return "审核状态不能为空";
        }
        if (dto.getCse() == null) {
            return "费用状态不能为空";
        }
        if (dto.getCheck() == null) {
            return "核对状态不能为空";
        }
        if (dto.getUser() == null) {
            return "制单人不能为空";
        }

        // 数值范围校验
        if (dto.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            return "单据成本不能为负数";
        }
        if (dto.getCost().compareTo(BigDecimal.ZERO) < 0) {
            return "单据费用不能为负数";
        }
        if (dto.getType() != 0 && dto.getType() != 1) {
            return "单据类型只能是0(其它入库单)或1(盘盈单)";
        }
        if (dto.getExamine() != 0 && dto.getExamine() != 1) {
            return "审核状态只能是0(未审核)或1(已审核)";
        }
        if (dto.getCse() < 0 || dto.getCse() > 3) {
            return "费用状态范围错误";
        }
        if (dto.getCheck() != 0 && dto.getCheck() != 1) {
            return "核对状态只能是0(未核对)或1(已核对)";
        }

        // 业务逻辑校验
        if (dto.getTime().isAfter(LocalDateTime.now())) {
            return "单据日期不能晚于当前时间";
        }

//        OtherInListInfoDTO otherInListInfoDTO = (OtherInListInfoDTO) dto.getOtherInListDetailInfoDTOList();
//        // 关联数据校验
//        if (validateOtherInListInfo(otherInListInfoDTO) == null) {
//            return "入库单详细信息不能为空";
//        }
//        if (validateCost((CostDTO) dto.getCostDTOList()) == null) {
//            return "单据费用列表不能为空";
//        }

        return null; // 返回null表示校验通过
    }

    /**
     * 入库单详细信息校验
     */
    private String validateOtherInListInfo(OtherInListInfoDTO info) {
        if (info == null) {
            return "入库单详细信息不能为空";
        }

        // 必填字段校验
//        if (StringUtils.isBlank(info.getName())) {
//            return "商品名称不能为空";
//        }
//        if (StringUtils.isBlank(info.getNumber())) {
//            return "商品编号不能为空";
//        }
        if (Integer.parseInt(info.getGoods()) <= 0) {
            return "所属商品ID无效";
        }

        if (StringUtils.isBlank(info.getUnit())) {
            return "单位不能为空";
        }
        if (Integer.parseInt(info.getWarehouse()) <= 0) {
            return "仓库ID无效";
        }

        // 数值范围校验
        if (info.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return "商品成本不能为负数";
        }
        if (info.getNums().compareTo(BigDecimal.ZERO) <= 0) {
            return "商品数量必须大于0";
        }
        if (info.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            return "商品总成本不能为负数";
        }

        // 业务逻辑校验
        if (info.getMfd() != null && info.getMfd().isAfter(LocalDateTime.now())) {
            return "生产日期不能晚于当前时间";
        }

        // 计算一致性校验
        BigDecimal expectedTotal = info.getPrice().multiply(info.getNums());
        if (info.getTotal().subtract(expectedTotal).abs().compareTo(BigDecimal.valueOf(0.01)) > 0) {
            return String.format("商品总成本计算错误，应为%.4f", expectedTotal);
        }

        return null; // 返回null表示校验通过
    }

    /**
     * 费用信息校验
     */
    private String validateCost(CostDTO cost) {
        if (cost == null) {
            return "费用信息不能为空";
        }
        if (cost.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            return "费用金额不能为负数";
        }
        return null;
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

    @Override
    @Transactional
    public JsonVO<String> importExcel(MultipartFile file) {
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            return JsonVO.fail(e.toString());
        }
        ExcelReadListener<OtherInImportExcelDTO> listener = new ExcelReadListener<>();
        EasyExcel.read(is, OtherInImportExcelDTO.class, listener).sheet().headRowNumber(2).doRead();
        List<OtherInImportExcelDTO> dataList = listener.getDataList();
        if(dataList == null){
            return JsonVO.fail("excel表中没有数据");
        }
        EntryDO entryDO = msEntryMapper.otherInImportExcelDTOToEntryDO(dataList.get(0));

        // 数据校验
        // 供应商不存在
        QueryWrapper<SupplierDO> wrapper2 = new QueryWrapper<>();
        wrapper2.lambda().eq(SupplierDO::getName, entryDO.getSupplier());
        SupplierDO supplierDO = supplierMapper.selectOne(wrapper2);
        if(supplierDO == null){
            return JsonVO.fail("供应商数据异常: " + entryDO.getSupplier());
        }
        // 日期校验
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(dataList.get(0).getTime());
        } catch (Exception e) {
            return JsonVO.fail("单据日期异常: " + dataList.get(0).getTime());
        }
        LocalDateTime localDateTime = localDate.atTime(0, 0, 0);

        //people数据库中不存在
        QueryWrapper<PeopleDO> wrapper1 = new QueryWrapper<>();
        wrapper1.lambda().eq(PeopleDO::getName, dataList.get(0).getPeople());
        PeopleDO peopleDO = peopleMapper.selectOne(wrapper1);
        if(peopleDO==null){
            if(dataList.get(0).getPeople()!=null) return JsonVO.fail("关联人员在库中不存在");
            else peopleDO = new PeopleDO();
        }
        // 单据编号不能重复或为空
        if(dataList.get(0).getNumber().isEmpty()){
            return JsonVO.fail("单据编号未填写");
        }
        List<EntryDO> entryDOList = this.lambdaQuery().eq(EntryDO::getNumber, dataList.get(0).getNumber()).list();
        if(!entryDOList.isEmpty()){
            return JsonVO.fail("单据编号已存在: " + entryDO.getNumber());
        }
        // excel标红字段不能为空
        if(entryDO.getType()==-1){
            return JsonVO.fail("单据类型异常: " + dataList.get(0).getType());
        }

        // 数据填充
        entryDO.setSupplier(supplierDO.getId());
        entryDO.setPeople(peopleDO.getId());
        entryDO.setTime(localDateTime);
        entryDO.setCost(new BigDecimal(0));
        entryDO.setFile(null);
        entryDO.setMore(null);
        entryDO.setExamine(0);
        entryDO.setCse(0); //TODO: 未确定
        entryDO.setCheck(0);
        try {
//            String frameId = userHolder.getCurrentUser().getFrameId();
//            String userId = userHolder.getCurrentUser().getId();
            String frameId = "F001";
            String userId = "1";
            entryDO.setFrame(frameId);
            entryDO.setUser(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        otherInMapper.insert(entryDO);

        List<EntryInfoDO> entryInfoDOList = msEntryMapper.otherInImportExcelDTOListToEntryInfoDOList(dataList);
        for(int i=0; i<entryInfoDOList.size(); i++) {
            EntryInfoDO entryInfoDO = entryInfoDOList.get(i);
            // 数据校验
            // 商品不在库中
            QueryWrapper<GoodsDO> wrapper3 = new QueryWrapper<>();
            wrapper3.lambda().eq(GoodsDO::getName, dataList.get(i).getGoods());
            GoodsDO goodsDO = goodsMapper.selectOne(wrapper3);
            if(goodsDO == null){
                throw new RuntimeException("商品不存在: " + dataList.get(i).getGoods());
//                return JsonVO.fail("商品不存在: " + entryInfoDO.getGoods());
            }
            //仓库不在数据库中
            QueryWrapper<WarehouseDO> wrapper4 = new QueryWrapper<>();
            wrapper4.lambda().eq(WarehouseDO::getName, dataList.get(i).getWarehouse());
            WarehouseDO warehouseDO = warehouseMapper.selectOne(wrapper4);
            if(warehouseDO == null){
                throw new RuntimeException("仓库不存在: " + dataList.get(i).getWarehouse());
//                return JsonVO.fail("仓库不存在: " + entryInfoDO.getWarehouse());
            }
            // 校验日期格式
            LocalDate time = null;
            try {
                time = LocalDate.parse(dataList.get(i).getMfd());
            } catch (Exception e) {
                throw new RuntimeException("生产日期格式错误: " + dataList.get(i).getMfd());
//                return JsonVO.fail("生产日期格式错误: " + dataList.get(i).getMfd());
            }
            // 成本
            if(entryInfoDO.getPrice()==null){
                throw new RuntimeException("成本为空");
//                return JsonVO.fail("必填数据缺失");
            }
            // 数量
            if(entryInfoDO.getNums()==null){
                throw new RuntimeException("数量为空");
            }

            // 填充数据
            entryInfoDO.setPid(entryDO.getId());
            entryInfoDO.setGoods(goodsDO.getId());
            entryInfoDO.setWarehouse(warehouseDO.getId());
            entryInfoDO.setMfd(time);
        }
        otherInInfoMapper.insertBatch(entryInfoDOList);

        return JsonVO.success(entryDO.getId());
    }

    @Override
    public JsonVO<ResponseEntity<byte[]>> exportEasyExcel(String ids) {
        List<String> idList = Arrays.asList(ids.split("\\+"));
        if(idList.isEmpty()){
            throw new RuntimeException("未传入有效id");
        }
        List<EntryDO> entryDOList = this.lambdaQuery()
                .in(EntryDO::getId, idList)
                .list();
        List<OtherInEasyExportExcelDTO> otherInEasyExportExcelDTOList = msEntryMapper.entryDOListToOtherInEasyExportExcelDTOList(entryDOList);
        Integer sumNum = otherInEasyExportExcelDTOList.size();
        BigDecimal sumTotal = new BigDecimal(0);
        BigDecimal sumCost = new BigDecimal(0);

        for(int i=0; i<sumNum; i++){
            //frame
            FrameDO frameDO = frameMapper.selectById(entryDOList.get(i).getFrame());
            if(frameDO==null){
                frameDO = new FrameDO();
            }
            String frameCv = frameDO.getName();
            otherInEasyExportExcelDTOList.get(i).setFrameCv(frameCv);

            // supplier
            SupplierDO supplierDO = supplierMapper.selectById(entryDOList.get(i).getSupplier());
            if(supplierDO == null){
                supplierDO = new SupplierDO();
            }
            otherInEasyExportExcelDTOList.get(i).setSupplierCv(supplierDO.getName());

            //type
            String typeCv = null;
            if(entryDOList.get(i).getType()==0) typeCv = "其他入库单";
            else if(entryDOList.get(i).getType()==1) typeCv = "盘盈单";
            else {
                throw new RuntimeException("单据类型异常");
            }
            otherInEasyExportExcelDTOList.get(i).setTypeCv(typeCv);

            // time
            String timeCv = entryDOList.get(i).getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            otherInEasyExportExcelDTOList.get(i).setTimeCv(timeCv);

            // people
            PeopleDO peopleDO = peopleMapper.selectById(entryDOList.get(i).getPeople());
            if(peopleDO == null) peopleDO = new PeopleDO();
            String peopleCv = peopleDO.getName();
            otherInEasyExportExcelDTOList.get(i).setPeopleCv(peopleCv);

            // examine
            String examineCv = null;
            if(entryDOList.get(i).getExamine() == 0) examineCv = "未审核";
            else if(entryDOList.get(i).getExamine() == 1) examineCv = "已审核";
            else {
                throw new RuntimeException("审核状态异常");
            }
            otherInEasyExportExcelDTOList.get(i).setExamineCv(examineCv);

            // cse
            String cseCv = null;
            if(entryDOList.get(i).getCse() == 0) cseCv = "未结算";
            else if(entryDOList.get(i).getCse() == 1) cseCv = "部分结算";
            else if(entryDOList.get(i).getCse() == 2) cseCv = "已结算";
            else if(entryDOList.get(i).getCse() == 3) cseCv = "无需结算";
            else {
                throw new RuntimeException("费用状态异常");
            }
            otherInEasyExportExcelDTOList.get(i).setCseCv(cseCv);

            // check
            String checkCv = null;
            if(entryDOList.get(i).getCheck() == 0) checkCv = "未核对";
            else if(entryDOList.get(i).getCheck() == 1) checkCv = "已核对";
            else {
                throw new RuntimeException("核对状态异常");
            }
            otherInEasyExportExcelDTOList.get(i).setCheckCv(checkCv);

            // user
            UserDO userDO = userMapper.selectById(entryDOList.get(i).getUser());
            if(userDO==null) userDO = new UserDO();
            String userCv = userDO.getName();
            otherInEasyExportExcelDTOList.get(i).setUserCv(userCv);

            sumTotal = sumTotal.add(entryDOList.get(i).getTotal());
            sumCost = sumCost.add(entryDOList.get(i).getCost());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new ClassPathResource("templates/easyExcelTemplate.xlsx").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ExcelWriter workBook = EasyExcel.write(baos, OtherInEasyExportExcelDTO.class).withTemplate(is).build();
        WriteSheet sheet = EasyExcel.writerSheet().build();
        Map<String, String> map = new HashMap<>();
        FillConfig build = FillConfig.builder().forceNewRow(true).build();

        map.put("sumNum", sumNum.toString());
        map.put("sumTotal", sumTotal.toString());
        map.put("sumCost", sumCost.toString());

        workBook.fill(otherInEasyExportExcelDTOList, build, sheet);
        workBook.fill(map, build, sheet);
        workBook.finish();
        byte[] byteArray = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        String filename = "OtherIn_"+ DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return JsonVO.success(new ResponseEntity<>(byteArray, headers, HttpStatus.OK));
    }

    @Override
    public JsonVO<ResponseEntity<byte[]>> exportDetailExcel(String ids) {
        List<String> idList = Arrays.asList(ids.split("\\+"));
        if(idList==null){
            throw new RuntimeException("id传入异常");
        }
        List<EntryDO> entryDOList = this.lambdaQuery()
                .in(EntryDO::getId, idList)
                .list();

        // 字节流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 压缩包
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.setLevel(Deflater.BEST_COMPRESSION);

        // 遍历Entry表，生成zip
        for (EntryDO entryDO : entryDOList) {
            // 查找对应的entry_info和goods表，组装成otherInDetailExportExcelList
            QueryWrapper<EntryInfoDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(EntryInfoDO::getPid, entryDO.getId());
            List<EntryInfoDO> entryInfoDOList = otherInInfoMapper.selectList(wrapper);
            List<OtherInDetailExportExcelDTO> otherInDetailExportExcelList = entryInfoDOList.stream()
                    .map(entryInfoDO -> {
                        OtherInDetailExportExcelDTO otherInDetailExportExcel = msEntryMapper.entryInfoDOToOtherInDetailExportExcel(entryInfoDO);
                        GoodsDO goodsDO = goodsMapper.selectById(entryInfoDO.getGoods());
                        if(goodsDO==null) goodsDO = new GoodsDO();
                        otherInDetailExportExcel.setGoodName(goodsDO.getName());
                        otherInDetailExportExcel.setSpec(goodsDO.getSpec());
                        return otherInDetailExportExcel;
                    })
                    .collect(Collectors.toList());

            try {
                zos.putNextEntry(new ZipEntry(entryDO.getId().toString()+".xlsx"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 模板
            InputStream is = null;
            try {
                is = new ClassPathResource("templates/detailExcelTemplate.xlsx").getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ExcelWriter workBook = EasyExcel.write(zos, OtherInDetailExportExcelDTO.class).autoCloseStream(false).withTemplate(is).build();
            WriteSheet sheet = EasyExcel.writerSheet().build();
            Map<String, String> map = new HashMap<>();
            FillConfig build = FillConfig.builder().forceNewRow(true).build();

            SupplierDO supplierDO = supplierMapper.selectById(entryDO.getSupplier());
            if(supplierDO==null) supplierDO = new SupplierDO();
            PeopleDO peopleDO = peopleMapper.selectById(entryDO.getPeople());
            if(peopleDO==null) peopleDO = new PeopleDO();

            map.put("supplier", supplierDO.getName());
            map.put("time", entryDO.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put("number", entryDO.getNumber());
            map.put("type", entryDO.getType()==0? "其他入库单":"盘盈单");
            map.put("total1", entryDO.getTotal().toString());
            map.put("cost", entryDO.getCost().toString());
            map.put("people", peopleDO.getName());
            map.put("logistics", JSON.parseObject(entryDO.getLogistics()).getString("number"));
            map.put("data1", entryDO.getData());

            workBook.fill(otherInDetailExportExcelList, build, sheet);
            workBook.fill(map, build, sheet);

            workBook.finish();
            try {
                is.close();
                zos.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        try {

            zos.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        String filename = "OtherIn_"+ DateTime.now().toString("yyyyMMddHHmmssS") + ".zip";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] byteArray = baos.toByteArray();

        try {
            zos.close();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return JsonVO.success(new ResponseEntity<>(byteArray, headers, HttpStatus.OK));
    }
}
