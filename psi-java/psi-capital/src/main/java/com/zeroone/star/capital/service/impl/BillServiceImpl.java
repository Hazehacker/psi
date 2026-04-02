package com.zeroone.star.capital.service.impl;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.EasyExcel;
import com.zeroone.star.capital.DO.BillDO;
import com.zeroone.star.capital.DO.ImyDO;
import com.zeroone.star.capital.mapper.BillMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.zeroone.star.capital.service.IBillService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetBillWrittenDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetPendingBillOffDTO;
import com.zeroone.star.project.query.j3.capital.VerificationBillWrittenQuery;
import com.zeroone.star.project.query.j3.capital.VerificationPendingBillQuery;
import com.zeroone.star.project.vo.JsonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import com.zeroone.star.project.dto.j3.capital.VerificationOperationDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationBatchDeleteDTO;
import com.zeroone.star.project.query.j3.capital.VerificationImportQuery;
import com.zeroone.star.project.vo.j3.capital.BatchOperateResultVO;
import com.zeroone.star.project.vo.j3.capital.ImportResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.capital.DO.*;
import com.zeroone.star.capital.mapper.*;

import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetAddDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetAddDetailDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetModifyDTO;
import com.zeroone.star.project.dto.j3.capital.VerificationSheetModifyDetailDTO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetDetailVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetAddVO;
import com.zeroone.star.project.vo.j3.capital.VerificationSheetModifyVO;
 


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 核销单 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Slf4j
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, BillDO> implements IBillService {
    @Resource
    private UserHolder userHolder;

    @Resource
    private BillMapper billMapper;
    @Resource
    private BillInfoMapper billInfoMapper;
    @Resource
    private ImyMapper imyMapper;
    @Resource
    private OmyMapper omyMapper;
    @Resource
    private SellMapper sellMapper;
    @Resource
    private SreMapper sreMapper;
    @Resource
    private IceMapper iceMapper;
    @Resource
    private BuyMapper buyMapper;
    @Resource
    private BreMapper breMapper;
    @Resource
    private OceMapper oceMapper;

    @Resource
    private ImyBillMapper imyBillMapper;
    @Resource
    private OmyBillMapper omyBillMapper;
    @Resource
    private SellBillMapper sellBillMapper;
    @Resource
    private SreBillMapper sreBillMapper;
    @Resource
    private IceBillMapper iceBillMapper;
    @Resource
    private BuyBillMapper buyBillMapper;
    @Resource
    private BreBillMapper breBillMapper;
    @Resource
    private OceBillMapper oceBillMapper;
    // 统一管理 *Mapper,*BillMapper
    private Map<String, BaseMapper<?>> mapperMap;
    private Map<String, BaseMapper<?>> billMapperMap;
    @PostConstruct
    public void init() {
        mapperMap = new HashMap<>();
        mapperMap.put("imy", imyMapper);
        mapperMap.put("omy", omyMapper);
        mapperMap.put("sell", sellMapper);
        mapperMap.put("sre", sreMapper);
        mapperMap.put("ice", iceMapper);
        mapperMap.put("buy", buyMapper);
        mapperMap.put("bre", breMapper);
        mapperMap.put("oce", oceMapper);


        billMapperMap = new HashMap<>();
        billMapperMap.put("imy", imyBillMapper);
        billMapperMap.put("omy", omyBillMapper);
        billMapperMap.put("sell", sellBillMapper);
        billMapperMap.put("sre", sreBillMapper);
        billMapperMap.put("ice", iceBillMapper);
        billMapperMap.put("buy", buyBillMapper);
        billMapperMap.put("bre", breBillMapper);
        billMapperMap.put("oce", oceBillMapper);
    }



    /**
     * 获取核销单详情
     *
     * @param id 核销单id
     * @return JsonVO<VerificationSheetDTO>
     * @author: a
     * @date: 2025/10/28
     */

    @Override
    public VerificationSheetVO getVSDetailById(String id) {
        VerificationSheetVO vsVO = new VerificationSheetVO();
        // 1. 查询核销单主表
        BillDO billDO = billMapper.selectById(id);
        if(billDO == null){
            throw new RuntimeException("未找到核销单，id=" + id);
        }
        // 2. 查询核销单详情
        List<BillInfoDO> billInfoDOS = billInfoMapper.selectList(
                new QueryWrapper<BillInfoDO>().eq("pid", id)
        );
        List<VerificationSheetDetailVO> details = new ArrayList<>();
        if (billInfoDOS == null || billInfoDOS.isEmpty()) {
            vsVO.setDetail(details);
            return vsVO;
        }
        // 查询关联单据数据
        for (BillInfoDO billInfoDO : billInfoDOS) {
            String mold = billInfoDO.getMold();
            String source = billInfoDO.getSource();

            // 3. 根据单据类型(mold)和关联单据id(source)查询关联单据数据，如bre
            Object data = getBillByMoldAndId(mold, source);
            if (data == null) continue;


            BigDecimal money = billInfoDO.getMoney(); // 核销金额
            BigDecimal total; // 单据金额
            BigDecimal amount = BigDecimal.ZERO; // 已核销金额
            BigDecimal anwo; // 未核销金额
            LocalDateTime time;
            switch (mold) {
                case "imy": {
                    ImyDO imy = (ImyDO) data;
                    total = imy.getTotal();
                    time = imy.getTime();
                    String pid = imy.getId();
                    ImyBillDO imyBillDO = imyBillMapper.selectOne(
                            new QueryWrapper<ImyBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (imyBillDO != null && imyBillDO.getMoney() != null) {
                        amount = imyBillDO.getMoney();
                    }
                    break;
                }
                case "omy": {
                    OmyDO omy = (OmyDO) data;
                    total = omy.getTotal();
                    time = omy.getTime();
                    String pid = omy.getId();
                    OmyBillDO omyBillDO = omyBillMapper.selectOne(
                            new QueryWrapper<OmyBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (omyBillDO != null && omyBillDO.getMoney() != null) {
                        amount = omyBillDO.getMoney();
                    }
                    break;
                }
                case "sell": {
                    SellDO sell = (SellDO) data;
                    total = sell.getTotal();
                    time = sell.getTime();
                    String pid = sell.getId();
                    SellBillDO sellBillDO = sellBillMapper.selectOne(
                            new QueryWrapper<SellBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (sellBillDO != null && sellBillDO.getMoney() != null) {
                        amount = sellBillDO.getMoney();
                    }
                    break;
                }
                case "sre": {
                    SreDO sre = (SreDO) data;
                    total = sre.getTotal();
                    time = sre.getTime();
                    String pid = sre.getId();
                    SreBillDO sreBillDO = sreBillMapper.selectOne(
                            new QueryWrapper<SreBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (sreBillDO != null && sreBillDO.getMoney() != null) {
                        amount = sreBillDO.getMoney();
                    }
                    break;
                }
                case "ice": {
                    IceDO ice = (IceDO) data;
                    total = ice.getTotal();
                    time = ice.getTime();
                    String pid = ice.getId();
                    IceBillDO iceBillDO = iceBillMapper.selectOne(
                            new QueryWrapper<IceBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (iceBillDO != null && iceBillDO.getMoney() != null) {
                        amount = iceBillDO.getMoney();
                    }
                    break;
                }
                case "buy": {
                    BuyDO buy = (BuyDO) data;
                    total = buy.getTotal();
                    time = buy.getTime();
                    String pid = buy.getId();
                    BuyBillDO buyBillDO = buyBillMapper.selectOne(
                            new QueryWrapper<BuyBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (buyBillDO != null && buyBillDO.getMoney() != null) {
                        amount = buyBillDO.getMoney();
                    }
                    break;
                }
                case "bre": {
                    BreDO bre = (BreDO) data;
                    total = bre.getTotal();
                    time = LocalDateTime.parse(bre.getTime());
                    String pid = bre.getId();
                    BreBillDO breBillDO = breBillMapper.selectOne(
                            new QueryWrapper<BreBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (breBillDO != null && breBillDO.getMoney() != null) {
                        amount = breBillDO.getMoney();
                    }
                    break;
                }
                case "oce": {
                    OceDO oce = (OceDO) data;
                    total = oce.getTotal();
                    time = oce.getTime();
                    String pid = oce.getId();
                    OceBillDO oceBillDO = oceBillMapper.selectOne(
                            new QueryWrapper<OceBillDO>().eq("pid", pid).eq("source", billInfoDO.getId())
                    );
                    if (oceBillDO != null && oceBillDO.getMoney() != null) {
                        amount = oceBillDO.getMoney();
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("不支持的单据类型：" + mold);
            }
            anwo = total.subtract(amount);
            VerificationSheetDetailVO  vsDetailVO = new VerificationSheetDetailVO();
            vsDetailVO.setBill(billInfoDO.getBill());
            vsDetailVO.setMold(billInfoDO.getMold());
            vsDetailVO.setTime(time);
            vsDetailVO.setNumber(billInfoDO.getSource());
            vsDetailVO.setTotal(total);
            vsDetailVO.setAmount(amount);
            vsDetailVO.setAnwo(anwo);
            vsDetailVO.setMoney(money);

            details.add(vsDetailVO);
        }

        // 7. 封装主表数据
        BeanUtil.copyProperties(billDO, vsVO);
        vsVO.setDetail(details);
        return vsVO;
    }

    /**
     * 添加核销单
     *
     * @param vsAddDTO 核销单数据
     * @return JsonVO<VerificationSheetAddVO>
     * @author: a
     * @date: 2025/10/30
     */
    @Override
    public VerificationSheetAddVO addVS(VerificationSheetAddDTO vsAddDTO, String token) {
        // 1.添加到核销单主表(bill)
        // 1.1 vsAddDTO 转换为 BillDO
        BillDO billDO = new BillDO();
        BeanUtil.copyProperties(vsAddDTO, billDO);
        // 获取制单人
        try {
            UserDTO currentUser = userHolder.getCurrentUser();
            System.out.println(currentUser);
            billDO.setUser(currentUser.getUsername());
        } catch (Exception e) {
            log.warn("获取当前用户失败{}", e);
            return VerificationSheetAddVO.builder()
                    .msg("获取当前用户失败")
                    .id(null)
                    .build();
        }
        // billDO.setUser("admin");
        // 1.2 添加到核销单主表
        billMapper.insert(billDO);
        // 1.3 获取核销单id
        String billId = billDO.getId();

        for(VerificationSheetAddDetailDTO detail: vsAddDTO.getDetail()){
            // 2.添加到核销单详情表bill_info
            BillInfoDO billInfoDO = new BillInfoDO();
            billInfoDO.setBill(detail.getBill());
            billInfoDO.setMold(detail.getMold());
            billInfoDO.setMoney(detail.getMoney());
            billInfoDO.setPid(billId);
            billInfoDO.setSource(detail.getSource());

            billInfoMapper.insert(billInfoDO);

            // 3.添加关联单据核销单，如bre_bill
            String mold = detail.getMold();

            switch (mold) {
                case "imy": {
                    ImyBillDO imyBillDO = new ImyBillDO();

                    imyBillDO.setPid(detail.getSource());
                    imyBillDO.setType(detail.getBill());
                    imyBillDO.setSource(billInfoDO.getId());
                    imyBillDO.setTime(detail.getTime());
                    imyBillDO.setMoney(detail.getMoney());
                    imyBillMapper.insert(imyBillDO);

                    break;
                }
                case "omy": {
                    OmyBillDO omyBillDO = new OmyBillDO();

                    omyBillDO.setPid(detail.getSource());
                    omyBillDO.setType(detail.getBill());
                    omyBillDO.setSource(billInfoDO.getId());
                    omyBillDO.setTime(detail.getTime());
                    omyBillDO.setMoney(detail.getMoney());

                    omyBillMapper.insert(omyBillDO);
                    break;
                }
                case "sell": {
                    SellBillDO sellBillDO = new SellBillDO();

                    sellBillDO.setPid(detail.getSource());
                    sellBillDO.setType(detail.getBill());
                    sellBillDO.setSource(billInfoDO.getId());
                    sellBillDO.setTime(detail.getTime());
                    sellBillDO.setMoney(detail.getMoney());
                    sellBillMapper.insert(sellBillDO);
                    break;
                }
                case "sre": {
                    SreBillDO sreBillDO = new SreBillDO();
                    sreBillDO.setPid(detail.getSource());
                    sreBillDO.setType(detail.getBill());
                    sreBillDO.setSource(billInfoDO.getId());
                    sreBillDO.setTime(detail.getTime());
                    sreBillDO.setMoney(detail.getMoney());
                    sreBillMapper.insert(sreBillDO);
                    break;
                }
                case "ice": {
                    IceBillDO iceBillDO = new IceBillDO();
                    iceBillDO.setPid(detail.getSource());
                    iceBillDO.setType(detail.getBill());
                    iceBillDO.setSource(billInfoDO.getId());
                    iceBillDO.setTime(detail.getTime());
                    iceBillDO.setMoney(detail.getMoney());
                    iceBillMapper.insert(iceBillDO);
                    break;
                }
                case "buy": {
                    BuyBillDO buyBillDO = new BuyBillDO();
                    buyBillDO.setPid(detail.getSource());
                    buyBillDO.setType(detail.getBill());
                    buyBillDO.setSource(billInfoDO.getId());
                    buyBillDO.setTime(detail.getTime());
                    buyBillDO.setMoney(detail.getMoney());
                    buyBillMapper.insert(buyBillDO);
                    break;
                }
                case "bre": {
                    BreBillDO breBillDO = new BreBillDO();
                    breBillDO.setPid(detail.getSource());
                    breBillDO.setType(detail.getBill());
                    breBillDO.setSource(billInfoDO.getId());
                    breBillDO.setTime(String.valueOf(detail.getTime()));
                    breBillDO.setMoney(detail.getMoney());
                    breBillMapper.insert(breBillDO);
                    break;
                }
                case "oce": {
                    OceBillDO oceBillDO = new OceBillDO();
                    oceBillDO.setPid(detail.getSource());
                    oceBillDO.setType(detail.getBill());
                    oceBillDO.setSource(billInfoDO.getId());
                    oceBillDO.setTime(detail.getTime());
                    oceBillDO.setMoney(detail.getMoney());
                    oceBillMapper.insert(oceBillDO);
                    break;
                }
                default:
                    throw new IllegalArgumentException("不支持的单据类型：" + mold);
            }
        }

        // 4.返回
        return VerificationSheetAddVO.builder()
                .msg("success")
                .id(billId)
                .build();
    }

    /**
     * 修改核销单
     *
     * @param vsModifyDTO 修改核销单数据
     * @return JsonVO<VerificationSheetModifyVO>
     * @author: a
     * @date: 2025/10/30
     */
    @Override
    public VerificationSheetModifyVO updateVS(VerificationSheetModifyDTO vsModifyDTO, String token) {
        // 1. 更新主表
        BillDO billDO = new BillDO();
        BeanUtil.copyProperties(vsModifyDTO, billDO);

        try {
            UserDTO currentUser = userHolder.getCurrentUser();
            billDO.setUser(currentUser.getUsername());
        } catch (Exception e) {
            log.warn("获取当前用户失败", e);
            return VerificationSheetModifyVO.builder()
                    .msg("获取当前用户失败")
                    .id(null)
                    .build();
        }
        // billDO.setUser("admin");

        int i = billMapper.updateById(billDO);
        if (i == 0) {
            return VerificationSheetModifyVO.builder()
                    .msg("主表更新失败：id=" + billDO.getId())
                    .id(null)
                    .build();
        }

        String billId = billDO.getId();

        // ===============================
        // 2. 删除旧的明细和关联单据
        // ===============================
        List<BillInfoDO> oldDetails = billInfoMapper.selectList(
                new QueryWrapper<BillInfoDO>().eq("pid", billId)
        );

        for (BillInfoDO old : oldDetails) {
            String mold = old.getMold();

            // 删除对应 *_bill 表中的记录（根据 source = billInfo.id）
            BaseMapper billMapper = billMapperMap.get(mold);
            if (billMapper != null) {
                billMapper.delete(new QueryWrapper<>().eq("source", old.getId()));
            }
        }

        // 删除 bill_info 记录
        billInfoMapper.delete(new QueryWrapper<BillInfoDO>().eq("pid", billId));
        log.info("删除核销单 [{}] 旧的明细记录 {} 条", billId, oldDetails.size());

        // ===============================
        // 3. 重新插入新的明细与关联单据
        // ===============================
        for (VerificationSheetModifyDetailDTO detail : vsModifyDTO.getDetail()) {
            // 插入新的 bill_info
            BillInfoDO billInfoDO = new BillInfoDO();
            billInfoDO.setBill(detail.getBill());
            billInfoDO.setMold(detail.getMold());
            billInfoDO.setMoney(detail.getMoney());
            billInfoDO.setPid(billId);
            billInfoDO.setSource(detail.getSource());
            billInfoMapper.insert(billInfoDO);

            // 插入新的关联单据（根据 mold）
            String mold = detail.getMold();
            switch (mold) {
                case "imy": {
                    ImyBillDO imyBillDO = new ImyBillDO();
                    imyBillDO.setPid(detail.getSource());
                    imyBillDO.setType(detail.getBill());
                    imyBillDO.setSource(billInfoDO.getId());
                    imyBillDO.setTime(detail.getTime());
                    imyBillDO.setMoney(detail.getMoney());
                    imyBillMapper.insert(imyBillDO);
                    break;
                }
                case "omy": {
                    OmyBillDO omyBillDO = new OmyBillDO();
                    omyBillDO.setPid(detail.getSource());
                    omyBillDO.setType(detail.getBill());
                    omyBillDO.setSource(billInfoDO.getId());
                    omyBillDO.setTime(detail.getTime());
                    omyBillDO.setMoney(detail.getMoney());
                    omyBillMapper.insert(omyBillDO);
                    break;
                }
                case "sell": {
                    SellBillDO sellBillDO = new SellBillDO();
                    sellBillDO.setPid(detail.getSource());
                    sellBillDO.setType(detail.getBill());
                    sellBillDO.setSource(billInfoDO.getId());
                    sellBillDO.setTime(detail.getTime());
                    sellBillDO.setMoney(detail.getMoney());
                    sellBillMapper.insert(sellBillDO);
                    break;
                }
                case "sre": {
                    SreBillDO sreBillDO = new SreBillDO();
                    sreBillDO.setPid(detail.getSource());
                    sreBillDO.setType(detail.getBill());
                    sreBillDO.setSource(billInfoDO.getId());
                    sreBillDO.setTime(detail.getTime());
                    sreBillDO.setMoney(detail.getMoney());
                    sreBillMapper.insert(sreBillDO);
                    break;
                }
                case "ice": {
                    IceBillDO iceBillDO = new IceBillDO();
                    iceBillDO.setPid(detail.getSource());
                    iceBillDO.setType(detail.getBill());
                    iceBillDO.setSource(billInfoDO.getId());
                    iceBillDO.setTime(detail.getTime());
                    iceBillDO.setMoney(detail.getMoney());
                    iceBillMapper.insert(iceBillDO);
                    break;
                }
                case "buy": {
                    BuyBillDO buyBillDO = new BuyBillDO();
                    buyBillDO.setPid(detail.getSource());
                    buyBillDO.setType(detail.getBill());
                    buyBillDO.setSource(billInfoDO.getId());
                    buyBillDO.setTime(detail.getTime());
                    buyBillDO.setMoney(detail.getMoney());
                    buyBillMapper.insert(buyBillDO);
                    break;
                }
                case "bre": {
                    BreBillDO breBillDO = new BreBillDO();
                    breBillDO.setPid(detail.getSource());
                    breBillDO.setType(detail.getBill());
                    breBillDO.setSource(billInfoDO.getId());
                    breBillDO.setTime(String.valueOf(detail.getTime()));
                    breBillDO.setMoney(detail.getMoney());
                    breBillMapper.insert(breBillDO);
                    break;
                }
                case "oce": {
                    OceBillDO oceBillDO = new OceBillDO();
                    oceBillDO.setPid(detail.getSource());
                    oceBillDO.setType(detail.getBill());
                    oceBillDO.setSource(billInfoDO.getId());
                    oceBillDO.setTime(detail.getTime());
                    oceBillDO.setMoney(detail.getMoney());
                    oceBillMapper.insert(oceBillDO);
                    break;
                }
                default:
                    throw new IllegalArgumentException("不支持的单据类型：" + mold);
            }
        }

        // ===============================
        // 4. 返回
        // ===============================
        return VerificationSheetModifyVO.builder()
                .msg("success")
                .id(billId)
                .build();
    }

    /**
     * 根据单据类型和id查询单据信息
     *
     * @param mold 单据类型
     * @param id   单据id
     * @return 单据信息
     * @author: a
     * @date: 2025/10/28
     */
    public <T> T getBillByMoldAndId(String mold, String id) {
        BaseMapper<T> mapper = (BaseMapper<T>) mapperMap.get(mold);
        if (mapper == null) {
            throw new IllegalArgumentException("不支持的单据类型：" + mold);
        }
        return mapper.selectById(id);
    }

    /**
     * 获取需要核销的单据列表
     * @param query 查询参数
     * @return 核销单列表
     * @author: 简单点
     * @date: 2025/10/19
     */
    @Override
    @Transactional
    public JsonVO<PageDTO<VerificationSheetPendingBillOffDTO>> listPendingWriteOff(VerificationPendingBillQuery query) {
        try {
            log.info("需要核销的单据列表参数：{}", query);
            log.info(query.getData());
            //创建分页对象
            Page<VerificationSheetPendingBillOffDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
            //执行查询
            Page<VerificationSheetPendingBillOffDTO> resultPage = chooseType2(page, query);
            PageDTO<VerificationSheetPendingBillOffDTO> pageDTO = PageDTO.create(resultPage);
            log.info("查询结果：{}", pageDTO);
            return JsonVO.success(pageDTO);
        } catch (Exception e) {
            log.error("查询核销单列表异常：{}", e.getMessage());
            return JsonVO.fail(null);

        }
    }
    /**
     * 核销单操作（核销、反核销）
     * @param dto 操作参数，包含type和ids
     * @return 批量操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchOperateResultVO approve(VerificationOperationDTO dto) {
        List<String> ids = dto.getIds();
        String type = dto.getType();
        int totalCount = ids.size();
        List<String> failIds = new ArrayList<>();
        List<String> failReasons = new ArrayList<>();

        // 判断操作类型
        boolean isVerify = "VERIFY".equalsIgnoreCase(type);
        boolean isUnverify = "UNVERIFY".equalsIgnoreCase(type);
        
        if (!isVerify && !isUnverify) {
            throw new RuntimeException("操作类型错误，type只能是VERIFY（核销）或UNVERIFY（反核销）");
        }

        try {
            // 使用 MyBatis Plus 查询所有核销单的当前状态
            List<BillDO> bills = this.lambdaQuery()
                    .in(BillDO::getId, ids)
                    .list();
            
            Map<String, BillDO> billMap = new HashMap<>();
            for (BillDO bill : bills) {
                billMap.put(bill.getId(), bill);
            }

            // 检查每个核销单的状态并更新
            int successCount = 0;
            for (String id : ids) {
                BillDO bill = billMap.get(id);
                if (bill == null) {
                    failIds.add(id);
                    failReasons.add("核销单不存在");
                    continue;
                }

                if (isVerify) {
                    // 核销操作：只能核销未核销的单据
                    if (bill.getExamine() != null && bill.getExamine() == 1) {
                        failIds.add(id);
                        failReasons.add("该核销单已核销，无法重复核销");
                        continue;
                    }
                    // 使用 MyBatis Plus 更新
                    boolean updated = this.lambdaUpdate()
                            .eq(BillDO::getId, id)
                            .set(BillDO::getExamine, 1)
                            .update();
                    if (updated) {
                        successCount++;
                    }
                } else {
                    // 反核销操作：只能反核销已核销的单据
                    if (bill.getExamine() == null || bill.getExamine() == 0) {
                        failIds.add(id);
                        failReasons.add("该核销单未核销，无法反核销");
                        continue;
                    }
                    // 使用 MyBatis Plus 更新
                    boolean updated = this.lambdaUpdate()
                            .eq(BillDO::getId, id)
                            .set(BillDO::getExamine, 0)
                            .update();
                    if (updated) {
                        successCount++;
                    }
                }
            }

            return BatchOperateResultVO.builder()
                    .successCount(successCount)
                    .failCount(failIds.size())
                    .totalCount(totalCount)
                    .failIds(failIds)
                    .failReasons(failReasons)
                    .build();

        } catch (Exception e) {
            log.error("核销/反核销操作失败", e);
            throw new RuntimeException("核销/反核销操作失败: " + e.getMessage());
        }
    }

    /**
     * 选择核销单类型  对应   imy_bill  sell_bill  sre_bill  ice_bill
     * @param page 分页对象
     * @param query 查询参数
     * @author: 简单点
     * @date: 2025/10/19
     * @return 查询结果
     */
    private Page<VerificationSheetPendingBillOffDTO> chooseType2(Page<VerificationSheetPendingBillOffDTO> page,
                                                                 VerificationPendingBillQuery query){
        switch (query.getType2()){
            case 0:return billMapper.listPendingWriteOff00(page, query);
            case 1:return billMapper.listPendingWriteOff01(page, query);
            case 2:return billMapper.listPendingWriteOff02(page, query);
            case 3:return billMapper.listPendingWriteOff03(page, query);
        }
        return null;
    }

    /**
     * 查询核销单列表
     * @param query 查询参数
     * @return 查询结
     * @author: 简单点
     * @date: 2025/10/19
     */
    @Override
    @Transactional
    public JsonVO<PageDTO<VerificationSheetBillWrittenDTO>> listWrittenOff(VerificationBillWrittenQuery query) {
        try{
            log.info("查询核销单参数：{}", query);
            //创建分页对象
            Page<VerificationSheetBillWrittenDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
            //执行查询
            Page<VerificationSheetBillWrittenDTO> resultPage = billMapper.listWrittenOff(page, query);
            if(resultPage.getRecords()==null||resultPage.getRecords().size()==0){
                return JsonVO.fail(null);
            }
            PageDTO<VerificationSheetBillWrittenDTO> pageDTO = PageDTO.create(resultPage);
            log.info("查询结果；{}", pageDTO);
            return JsonVO.success(pageDTO);
        }catch (Exception e){
            log.error("查询核销单列表异常：{}", e.getMessage());
            return JsonVO.fail(null);
        }
    }



    /**
     * 批量删除核销单
     * @param dto 批量删除参数
     * @return 批量操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchOperateResultVO batchDelete(VerificationBatchDeleteDTO dto) {
        List<String> ids = dto.getIds();
        int totalCount = ids.size();
        List<String> failIds = new ArrayList<>();
        List<String> failReasons = new ArrayList<>();

        try {
            // 使用 MyBatis Plus 查询所有核销单的当前状态
            List<BillDO> bills = this.lambdaQuery()
                    .in(BillDO::getId, ids)
                    .list();
            
            Map<String, BillDO> billMap = new HashMap<>();
            for (BillDO bill : bills) {
                billMap.put(bill.getId(), bill);
            }

            // 检查每个核销单是否可以删除（只能删除未审核的）
            int successCount = 0;
            for (String id : ids) {
                BillDO bill = billMap.get(id);
                if (bill == null) {
                    failIds.add(id);
                    failReasons.add("核销单不存在");
                    continue;
                }

                if (bill.getExamine() != null && bill.getExamine() == 1) {
                    failIds.add(id);
                    failReasons.add("已审核的核销单不能删除");
                    continue;
                }
                
                // 使用 MyBatis Plus 删除
                boolean deleted = this.lambdaUpdate()
                        .eq(BillDO::getId, id)
                        .eq(BillDO::getExamine, 0)
                        .remove();
                if (deleted) {
                    successCount++;
                }
            }

            return BatchOperateResultVO.builder()
                    .successCount(successCount)
                    .failCount(failIds.size())
                    .totalCount(totalCount)
                    .failIds(failIds)
                    .failReasons(failReasons)
                    .build();

        } catch (Exception e) {
            log.error("批量删除核销单失败", e);
            throw new RuntimeException("批量删除核销单失败: " + e.getMessage());
        }
    }

    /**
     * 导入核销单数据
     * @param file 上传的Excel文件
     * @param query 导入查询参数
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importData(MultipartFile file, VerificationImportQuery query) {
        long startTime = System.currentTimeMillis();
        List<Integer> failRows = new ArrayList<>();
        List<String> failReasons = new ArrayList<>();
        int successCount = 0;
        int totalCount = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            totalCount = sheet.getLastRowNum(); // 不包括表头

            // 跳过表头，从第二行开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    BillDO bill = parseBillFromRow(row, query);
                    
                    // 检查是否覆盖已有数据
                    if (query.getOverwrite() != null && query.getOverwrite()) {
                        // 根据编号查询是否存在
                        BillDO existingBill = this.lambdaQuery()
                                .eq(BillDO::getNumber, bill.getNumber())
                                .one();
                        if (existingBill != null) {
                            bill.setId(existingBill.getId());
                            this.updateById(bill);
                        } else {
                            this.save(bill);
                        }
                    } else {
                        this.save(bill);
                    }
                    successCount++;

                } catch (Exception e) {
                    log.error("导入第{}行数据失败: {}", i + 1, e.getMessage());
                    failRows.add(i + 1);
                    failReasons.add(e.getMessage());

                    // 如果不跳过错误，则抛出异常回滚
                    if (query.getSkipError() == null || !query.getSkipError()) {
                        throw new RuntimeException("导入第" + (i + 1) + "行失败: " + e.getMessage());
                    }
                }
            }

            long duration = System.currentTimeMillis() - startTime;

            return ImportResultVO.builder()
                    .successCount(successCount)
                    .failCount(failRows.size())
                    .totalCount(totalCount)
                    .failRows(failRows)
                    .failReasons(failReasons)
                    .duration(duration)
                    .build();

        } catch (Exception e) {
            log.error("导入核销单数据失败", e);
            throw new RuntimeException("导入核销单数据失败: " + e.getMessage());
        }
    }

    /**
     * 从Excel行解析核销单数据
     * @param row Excel行
     * @param query 导入查询参数
     * @return 核销单对象
     */
    private BillDO parseBillFromRow(Row row, VerificationImportQuery query) {
        BillDO bill = new BillDO();
        
        try {
            // 生成ID
            bill.setId(UUID.randomUUID().toString().replace("-", ""));
            
            // 设置所属组织
            bill.setFrame(query.getFrame() != null ? query.getFrame() : "0");
            
            // 解析Excel列数据（根据实际Excel模板调整列索引）
            // 列0: 单据编号
            bill.setNumber(getCellValueAsString(row.getCell(0)));
            
            // 列1: 客户
            bill.setCustomer(getCellValueAsString(row.getCell(1)));
            
            // 列2: 供应商
            bill.setSupplier(getCellValueAsString(row.getCell(2)));
            
            // 列3: 单据时间
            Cell timeCell = row.getCell(3);
            if (timeCell != null && timeCell.getCellType() == CellType.NUMERIC) {
                Date date = DateUtil.getJavaDate(timeCell.getNumericCellValue());
                bill.setTime(date);
            }
            
            // 列4: 核销类型
            Cell typeCell = row.getCell(4);
            if (typeCell != null) {
                bill.setType((int) typeCell.getNumericCellValue());
            }
            
            // 列5: 总核金额
            Cell pmyCell = row.getCell(5);
            if (pmyCell != null) {
                bill.setPmy(new BigDecimal(pmyCell.getNumericCellValue()));
            }
            
            // 列6: 总销金额
            Cell smpCell = row.getCell(6);
            if (smpCell != null) {
                bill.setSmp(new BigDecimal(smpCell.getNumericCellValue()));
            }
            
            // 列7: 关联人员
            bill.setPeople(getCellValueAsString(row.getCell(7)));
            
            // 列8: 备注信息
            bill.setData(getCellValueAsString(row.getCell(8)));
            
            // 设置默认值
            bill.setExamine(0); // 默认未审核
            bill.setUser("import"); // 默认导入用户
            
            return bill;
            
        } catch (Exception e) {
            throw new RuntimeException("解析Excel行数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取单元格值为字符串
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;

        }
    }


    /**
     * 导出简单报表
     * @author 小王
     * @param ids 核销单ID列表
     * @return
     */
    @Override
    public ResponseEntity<byte[]> exportBill(List<String> ids) {
        try {
            // 1. 准备数据
            List<BillDO> billDOS = billMapper.selectBatchIds(ids);
            if (billDOS == null || billDOS.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("未找到对应的核销单数据".getBytes());
            }

            // 2. 定义表头
            List<List<String>> headList = Arrays.asList(
                    Collections.singletonList("所属组织"),
                    Collections.singletonList("客户"),
                    Collections.singletonList("供应商"),
                    Collections.singletonList("单据时间"),
                    Collections.singletonList("单据编号"),
                    Collections.singletonList("核销类型"),
                    Collections.singletonList("核销金额"),
                    Collections.singletonList("关联人员"),
                    Collections.singletonList("审核状态"),
                    Collections.singletonList("制单人"),
                    Collections.singletonList("备注信息")
            );

            // 3. 创建日期格式化器
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

            // 4. 转换为二维行数据
            List<List<Object>> rowList = new ArrayList<>();
            for (BillDO bill : billDOS) {
                // 核销类型转换（Integer类型：0-4）
                Integer type = bill.getType();
                String typeDesc = "";
                if (type != null) {
                    switch (type) {
                        case 0: typeDesc = "预收冲应收"; break;
                        case 1: typeDesc = "预付冲应付"; break;
                        case 2: typeDesc = "应收冲应付"; break;
                        case 3: typeDesc = "销退冲销售"; break;
                        case 4: typeDesc = "购退冲采购"; break;
                        default: typeDesc = "未知";
                    }
                }

                // 审核状态转换（Integer类型：0-1）
                String examineDesc = "";
                if (bill.getExamine() != null) {
                    examineDesc = bill.getExamine() == 1 ? "已审核" : "未审核";
                }

                // 日期格式化
                String timeStr = "";
                if (bill.getTime() != null) {
                    timeStr = dateFormatter.format(bill.getTime());
                }

                rowList.add(Arrays.asList(
                        bill.getFrame() != null ? bill.getFrame() : "",
                        bill.getCustomer() != null ? bill.getCustomer() : "",
                        bill.getSupplier() != null ? bill.getSupplier() : "",
                        timeStr,
                        bill.getNumber() != null ? bill.getNumber() : "",
                        typeDesc,
                        bill.getPmy() != null ? bill.getPmy().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() : "",
                        bill.getPeople() != null ? bill.getPeople() : "",
                        examineDesc,
                        bill.getUser() != null ? bill.getUser() : "",
                        bill.getData() != null ? bill.getData() : ""
                ));
            }

            // 5. 计算统计信息
            int totalCount = billDOS.size();
            BigDecimal totalAmount = billDOS.stream()
                    .map(BillDO::getPmy)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 6. 添加统计行
            List<Object> statsRow = new ArrayList<>();
            statsRow.add("总数:" + totalCount);
            statsRow.add("总核销金额:" + totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            // 第3-11列：空字符串
            for (int i = 2; i < headList.size(); i++) {
                statsRow.add("");
            }
            rowList.add(statsRow);

            // 7. 导出为 Excel
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                EasyExcel.write(out)
                        .head(headList)
                        .sheet("核销单列表")
                        .doWrite(rowList);

                byte[] bytes = out.toByteArray();

                String filename = "核销单列表-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.builder("attachment")
                        .filename(filename, StandardCharsets.UTF_8)
                        .build());
                headers.setContentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentLength(bytes.length);
                headers.setCacheControl("no-cache, no-store, must-revalidate");

                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("导出核销单简易报表失败", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("导出失败：" + e.getMessage()).getBytes());
        }
    }
}
