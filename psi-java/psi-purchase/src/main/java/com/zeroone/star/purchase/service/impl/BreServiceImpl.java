package com.zeroone.star.purchase.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.common.constant.CacheConstants;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnDetailReportDTO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.user.UserDTO;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnAddDTO;
import com.zeroone.star.project.query.j3.purchase.PurchaseReturnQuery;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnDetailVO;
import com.zeroone.star.project.vo.j3.purchase.PurchaseReturnListVO;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeroone.star.project.dto.j3.purchase.CheckOutReturnGoodsDTO;
import com.zeroone.star.project.dto.j3.purchase.PurchaseUpdateDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.DO.BreDO;
import com.zeroone.star.purchase.DO.BreInfoDO;
import com.zeroone.star.purchase.mapper.BreMapper;
import com.zeroone.star.purchase.service.IBreBillService;
import com.zeroone.star.purchase.service.IBreInfoService;
import com.zeroone.star.purchase.service.IBreService;


import com.zeroone.star.purchase.utils.ExcelHelper;
import com.zeroone.star.purchase.utils.PurchaseExcelExportDetailUtil;
import com.zeroone.star.common.constant.CacheConstants;
import com.zeroone.star.common.utils.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;

import com.zeroone.star.purchase.service.IGoodsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.excel.CursorPageQuery;
import com.zeroone.star.project.components.excel.ExportResult;
import com.zeroone.star.project.components.user.UserHolder;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnBreAuditDTO;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.purchase.DO.*;
import com.zeroone.star.purchase.excel.PurchaseReturnExportExcel;
import com.zeroone.star.purchase.excel.PurchaseReturnImportExcel;
import com.zeroone.star.purchase.mapper.BreMapper;
import com.zeroone.star.purchase.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 采购退货单 服务实现类
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BreServiceImpl extends ServiceImpl<BreMapper, BreDO> implements IBreService {
    private final BreMapper breMapper;

    @Resource
    private PurchaseExcelExportDetailUtil exportDetailUtil;


    /**
     * 批量导出采购退货单详细报表
     * @param ids 采购退货单id列表
     * @return 响应实体
     * @author Hazenix 
     * @since 2025/10/27
     */
//    @Override
//    public ResponseEntity<byte[]> exportBreInfo(List<String> ids) {
//        // 1. 检查采购退货单id列表是否为空
//        if (ids == null || ids.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        // 2. 封装查询结果
//        List<PurchaseReturnDetailReportDTO> reportDTOList = breMapper.exportBreInfo(ids);
//        // 3. 判断查询结果是否为空
//        if (reportDTOList == null || reportDTOList.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        log.info("查询到 {} 条记录", reportDTOList.size());
//        try {
//            // 4. 创建ZIP文件
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ZipOutputStream zos = new ZipOutputStream(baos);
//            // 5. 按单据分组（同一个单据编号可能有多个商品）
//            Map<String, List<PurchaseReturnDetailReportDTO>> groupedByNumber = reportDTOList.stream()
//                    .collect(Collectors.groupingBy(dto ->
//                            dto.getNumber() != null ? dto.getNumber() : "unknown"));
//            // 6. 为每个单据创建Excel文件
//            for (Map.Entry<String, List<PurchaseReturnDetailReportDTO>> entry : groupedByNumber.entrySet()) {
//                String number = entry.getKey();
//                List<PurchaseReturnDetailReportDTO> items = entry.getValue();
//                if (!items.isEmpty()) {
//                    PurchaseReturnDetailReportDTO firstItem = items.get(0);
//                    String fileName = String.format("采购退货单_%s_%s.xlsx",
//                            getSafeString(firstItem.getSupplier()),
//                            number);
//                    // 使用EasyExcel生成Excel文件
//                    byte[] excelBytes = exportDetailUtil.createExcelForBre(
//                            firstItem,
//                            getSafeString(firstItem.getSupplier()),
//                            getSafeString(firstItem.getTime()),
//                            number
//                    );
//                    // 将Excel文件写入ZIP
//                    ZipEntry zipEntry = new ZipEntry(fileName);
//                    zos.putNextEntry(zipEntry);
//                    zos.write(excelBytes);
//                    zos.closeEntry();
//                    log.info("生成Excel文件: {}", fileName);
//                }
//            }
//            zos.finish();
//            zos.close();
//            // 7. 构建响应
//            byte[] zipBytes = baos.toByteArray();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            String timestamp = String.valueOf(System.currentTimeMillis());
//            String originalFileName = "采购退货单详细报表_" + timestamp + ".zip";
//            String zipFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString());
//            headers.setContentDispositionFormData("attachment", zipFileName);
//            headers.setContentLength(zipBytes.length);
//            log.info("ZIP文件生成成功，大小: {} bytes", zipBytes.length);
//            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("导出Excel失败", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(("导出失败: " + e.getMessage()).getBytes());
//        }
//
//    }


    /**
     * 获取安全的字符串
     * @param value 待处理的字符串
     * @return 安全的字符串
     * @author Hazenix 
     * @since 2025/10/27
     */
    private String getSafeString(String value) {
        return value == null ? "" : value;
    }

    /**
     * @author Hazenix 
     * 注入IGoodsService
     */
    @Resource
    private IGoodsService iGoodsService;

    /**
     * @author Hazenix 
     * 注入IGoodsService
     */

    @Lazy
    @Resource
    private IBreInfoService iBreInfoService;

    /**
     * @author Hazenix 
     * 注入IGoodsService
     */
    @Resource
    private IBreBillService iBreBillService;

    @Resource
    UserHolder userHolder;

    @Resource
    CacheUtil cacheUtil;

    /**
     * @author Hazenix 
     * 获取获取采购退货单列表VO
     * @param purchaseReturnQuery
     * @return
     */
    @Override
    public PageDTO<PurchaseReturnListVO> getPurchaseReturnPageDATO(PurchaseReturnQuery purchaseReturnQuery) {
        // 构建缓存key：前缀 + 查询条件MD5 + 分页
        String cacheKey = CacheConstants.PURCHASE_BRE_LIST + ":" +
                JSONUtil.toJsonStr(purchaseReturnQuery).hashCode() + ":" +
                purchaseReturnQuery.getPageIndex() + ":" + purchaseReturnQuery.getPageSize();

        try {
            return cacheUtil.queryWithMutex(
                    cacheKey,
                    PageDTO.class,
                    () -> queryBreData(purchaseReturnQuery),
                    CacheConstants.DEFAULT_TTL,
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            log.warn("缓存获取失败，降级查数据库", e);
            Thread.currentThread().interrupt();
        }

        // 降级查询
        return queryBreData(purchaseReturnQuery);
    }

    /**
     * 查询采购退货单数据
     */
    private PageDTO<PurchaseReturnListVO> queryBreData(PurchaseReturnQuery purchaseReturnQuery) {
        // 1.查询主表bre：获取BreDO列表
        // 1.1获取查询构造器
        QueryWrapper<BreDO> queryWrapper = this.getQueryWrapper(purchaseReturnQuery);
        // 1.2查询数据
        List<BreDO> breDOList = this.list(queryWrapper);

        // 2.查询核销金额 并风封装VO：bre_bill
        List<PurchaseReturnListVO> purchaseReturnListVOList = iBreBillService.getBreVOList(breDOList);

        // 3.封装为MP的Page对象
        long pageIndex = purchaseReturnQuery.getPageIndex();
        long pageSize = purchaseReturnQuery.getPageSize();
        Page<PurchaseReturnListVO> breVOPage = new Page<>(pageIndex, pageSize);
        breVOPage.setRecords(purchaseReturnListVOList);

        // 4.Page转PageDTO并返回数据
        return PageDTO.create(breVOPage);
    }

    /**
     * @author Hazenix 
     * 新增采购退货单
     * @param purchaseReturnAddDTO
     * @return
     */
    @Override
    @Transactional  //涉及多表操作，开始事务
    public String addPurchaseReturn(PurchaseReturnAddDTO purchaseReturnAddDTO) {
        // 1.保存主表
        BreDO breDO = new BreDO();
        BeanUtils.copyProperties(purchaseReturnAddDTO, breDO);
        // 1.2设置默认业务字段
        breDO.setExamine(0);   // 审核状态
        breDO.setNucleus(0);   // 核销状态
        breDO.setCse(0);       // 费用状态
        breDO.setInvoice(0);   // 发票状态
        breDO.setCheck(0);     // 核对状态
        breDO.setSource("BUY"); // 来源
        // 1.3获取当前登录用户
        try {
            // TODO 测试阶段，采用模拟数据
            //UserDTO currentUser = userHolder.getCurrentUser();
            breDO.setUser("xiaoliu"); // 制单人（当前登录用户）
            UserDTO currentUser = userHolder.getCurrentUser();
            // 设置制单人（当前登录用户）
            breDO.setUser(currentUser.getUsername());
            // 设置所属组织
            breDO.setFrame(currentUser.getFrameName());

            // 1.4 判断单据编号是否已存在
            Long count = this.lambdaQuery()
                    .eq(BreDO::getNumber, breDO.getNumber())
                    .count();
            if(count > 0){
                throw new RuntimeException("新增采购退货单失败，采购退货单已存在！！");
            }

            // 1.5保存数据到bre表
            this.save(breDO);

            // 3.处理明细
            List<BreInfoDO> breInfoList = purchaseReturnAddDTO.getDetails().stream().map(detail -> {
                BreInfoDO breInfoDO = new BreInfoDO();

                //拷贝相同字段
                BeanUtils.copyProperties(detail, breInfoDO);

                // 从goods表查出商品id
                String goodsId = iGoodsService.findGoodsId(detail.getGoods(), detail.getNumber(), detail.getSpec());
                breInfoDO.setGoods(goodsId);
                breInfoDO.setPid(breDO.getId());
                return breInfoDO;
            }).collect(Collectors.toList());

            // 4.批量插入bre_info表
            iBreInfoService.saveBatch(breInfoList);

            // 5.返回数据
            return breDO.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Hazenix 
     * @param purchaseReturnQuery
     * @return QueryWrapper<BreDO>
     * 获取采购退货单查询包装类（用户根据哪些字段查询，根据前端用户传来的请求对象，得到 mybatis 框架支持的查询类）
     */
    //@Override
    public QueryWrapper<BreDO> getQueryWrapper(PurchaseReturnQuery purchaseReturnQuery) {
        QueryWrapper<BreDO> queryWrapper = new QueryWrapper<>();
        if (purchaseReturnQuery == null) {
            // 查询条件为空，返回空wrapper
            return queryWrapper;
        }

        // 获取请求中的各个字段
        String name = purchaseReturnQuery.getName();
        String number = purchaseReturnQuery.getNumber();
        String supplier = purchaseReturnQuery.getSupplier();
        String createUser = purchaseReturnQuery.getCreateUser();
        LocalDateTime timeStart = purchaseReturnQuery.getTimeStart();
        LocalDateTime timeEnd = purchaseReturnQuery.getTimeEnd();
        String people = purchaseReturnQuery.getPeople();
        Integer examine = purchaseReturnQuery.getExamine();
        Integer nucleus = purchaseReturnQuery.getNucleus();
        Integer cse = purchaseReturnQuery.getCse();
        Integer invoice = purchaseReturnQuery.getInvoice();
        Integer checkStatus = purchaseReturnQuery.getCheckStatus();
        String data = purchaseReturnQuery.getData();

        // 根据请求中的各个字段，进行条件拼接
        queryWrapper
                // 单据编号精确查询
                .eq(StringUtils.isNotBlank(number), "number", number)
                // 供应商
                .eq(supplier != null, "supplier", supplier)
                // 关联人员
                .eq(people != null, "people", people)
                // 制单人
                .eq(createUser != null, "`user`", createUser)
                // 审核状态
                .eq(examine != null, "examine", examine)
                // 核销状态
                .eq(nucleus != null, "nucleus", nucleus)
                // 费用状态
                .eq(cse != null, "cse", cse)
                // 发票状态
                .eq(invoice != null, "invoice", invoice)
                // 核对状态
                .eq(checkStatus != null, "`check`", checkStatus)
                // 备注模糊查询
                .like(StringUtils.isNotBlank(data), "data", data);

        // 商品名称需联表模糊查询
        // TODO 后期做性能优化
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.inSql("id",
                    String.format(
                            "SELECT DISTINCT bi.pid FROM bre_info bi " +
                                    "JOIN goods g ON bi.goods = g.id " +
                                    "WHERE g.name LIKE '%%%s%%'",
                            name.replace("'", "''") // 防止注入
                    )
            );
        }

        // 单据时间区间
        if (timeStart != null && timeEnd != null) {
            queryWrapper.between("time", timeStart, timeEnd);
        } else if (timeStart != null) {
            queryWrapper.ge("time", timeStart);
        } else if (timeEnd != null) {
            queryWrapper.le("time", timeEnd);
        }

        // 默认排序（按创建时间降序）
        queryWrapper.orderByDesc("time");

        return queryWrapper;
    }

    /**
     * 更新 bre 表
     */
    public void updateBreDo(PurchaseUpdateDTO purchaseUpdateDTO) {
        BreDO breDO = new BreDO();
        String id = purchaseUpdateDTO.getPid();
        try {
            BeanUtils.copyProperties(purchaseUpdateDTO, breDO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.update(breDO, new LambdaQueryWrapper<BreDO>().eq(BreDO::getId, id));
    }

    /**
     * 审核和反审核的实现类
     */
    @Override
    @Transactional
    public JsonVO<Integer> checkOut(CheckOutReturnGoodsDTO checkOutReturnGoodsDTO) {
        String[] pids = checkOutReturnGoodsDTO.getPids();
        /*将所有订单核对*/
        if (checkOutReturnGoodsDTO.getType() == 0) {
            for (String pid : pids) {
                try {
                    if (StrUtil.isNotBlank(pid)) {
                        breMapper.checkOut(pid);
                    } else {
                        return JsonVO.fail(0);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("核对失败");
                }
            }
        }
        /*将所有订单反核对*/
        if (checkOutReturnGoodsDTO.getType() == 1) {
            for (String pid : pids) {
                try {
                    if (StrUtil.isNotBlank(pid)) {
                        breMapper.checkOut(pid);
                    } else {
                        return JsonVO.fail(0);
                    }
                    breMapper.notCheckOut(pid);
                } catch (Exception e) {
                    throw new RuntimeException("反核对失败");
                }
            }
        }
        return JsonVO.success(1);

    }

    @Lazy
    @Autowired
    private IBreInfoService breInfoService;
    @Autowired
    private IBatchService batchService;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IPeopleService peopleService;
    @Autowired
    private IFrameService frameService;
    @Autowired
    private IUserService userService;
    @Autowired
    private EasyExcelComponent excel;
    /**
     * @param purchaseReturnBreAuditDTO 采购退货单审核DTO
     * @return 审核结果
     * @author Hazenix 
     * 批量审核/反审核
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> approve(PurchaseReturnBreAuditDTO purchaseReturnBreAuditDTO) {
        // 获取审核列表
        List<String> ids = purchaseReturnBreAuditDTO.getIds();
        // 获取审核类型
        int type = purchaseReturnBreAuditDTO.getType();
        // 影响行数
        int row = 0;


        for (String id : ids) {

            // 获取当前采购退货单实体
            BreDO breDO = breMapper.selectById(id);
            // 获取当前审核状态
            Integer examine = breDO.getExamine();
            // 若当前审核状态和审核类型一致则无需操作
            if (examine.equals(type)) {
                continue;
            }

            // 获取采购退货单详情列表
            QueryWrapper<BreInfoDO> breInfoWrapper = new QueryWrapper<>();
            breInfoWrapper.eq("pid", id);
            List<BreInfoDO> breInfoDOList = breInfoService.list(breInfoWrapper);

            // 记录商品行数
            int i = 0;

            for (BreInfoDO breInfoDO : breInfoDOList) {
                i++;

                String goods = breInfoDO.getGoods();    //商品id
                String warehouse = breInfoDO.getWarehouse();    // 仓库id
                String attr = breInfoDO.getAttr();      //辅助属性
                BigDecimal nums = breInfoDO.getNums();  //数量
                String batch = breInfoDO.getBatch();    //批次号
                String mfd = breInfoDO.getMfd();        //生产日期

                // 根据仓库id、商品id和辅助属性查询库存信息
                QueryWrapper<RoomDO> roomWrapper = new QueryWrapper<>();
                roomWrapper.eq("warehouse", warehouse)
                        .eq("goods", goods)
                        .eq("attr", attr);
                RoomDO roomDO = roomService.getOne(roomWrapper);

                // 根据批次号、商品id和生产日期查询批次信息
                QueryWrapper<BatchDO> batchWrapper = new QueryWrapper<>();
                batchWrapper.eq("number", batch)
                        .eq("goods", goods)
                        .eq("time", mfd);
                BatchDO batchDO = batchService.getOne(batchWrapper);

                // 审核
                if (type == 1) {
                    // 批次商品
                    if (StringUtils.isNotBlank(batch)) {
                        // 没有该批次信息
                        if (batchDO == null) {
                            return JsonVO.fail("审核单据[ " + breDO.getNumber() + " ]失败,原因:第" + i + "行批次信息无效!");
                        } else {
                            // 退货数量大于批次库存
                            if (nums.compareTo(batchDO.getNums()) > 0) {
                                return JsonVO.fail("审核单据[ " + breDO.getNumber() + " ]失败,原因:第" + i + "行批次库存不足!");
                            }
                        }
                    } else { //非批次商品
                        // 仓储信息不存在
                        if (roomDO == null) {
                            return JsonVO.fail("审核单据[ " + breDO.getNumber() + " ]失败,原因:第" + i + "行仓储信息不存在!");
                        }

                        // 退货数量大于库存数量
                        if (nums.compareTo(roomDO.getNums()) > 0) {
                            return JsonVO.fail("审核单据[ " + breDO.getNumber() + " ]失败,原因:第" + i + "行仓储库存不足!");
                        }
                    }
                }   //审核通过

                // 修改库存
                if (type == 1) {
                    // 审核，库存减少
                    roomDO.setNums(roomDO.getNums().subtract(nums));
                } else {
                    // 反审核，库存增加
                    roomDO.setNums(roomDO.getNums().add(nums));
                }
                if (!roomService.updateById(roomDO)) {
                    return JsonVO.fail("由单据[ " + breDO.getNumber() + " ]修改库存失败");
                }

                // 修改批次库存
                if (StringUtils.isNotBlank(batch)) {
                    if (type == 1) {
                        // 审核，库存减少
                        batchDO.setNums(batchDO.getNums().subtract(nums));
                    } else {
                        // 反审核，库存增加
                        batchDO.setNums(batchDO.getNums().add(nums));
                    }
                    if (!batchService.updateById(batchDO)) {
                        return JsonVO.fail("由单据[ " + breDO.getNumber() + " ]修改批次库存失败");
                    }
                }
            }

            // 修改资金账户
            BigDecimal money = breDO.getMoney();    //金额
            String account = breDO.getAccount();    //账户id
            QueryWrapper<AccountDO> accountWrapper = new QueryWrapper<>();
            // 根据账户id查询账户余额
            accountWrapper.select("id", "balance").eq("id", account);
            AccountDO accountDO = accountService.getOne(accountWrapper);
            if (type == 1) {
                // 审核，资金增加
                accountDO.setBalance(accountDO.getBalance().add(money));
            } else {
                // 反审核，资金减少
                accountDO.setBalance(accountDO.getBalance().subtract(money));
            }
            if (!accountService.updateById(accountDO)) {
                return JsonVO.fail("由单据[ " + breDO.getNumber() + " ]修改资金账户失败");
            }

            // 修改审核状态
            BreDO updateExamineType = new BreDO();
            updateExamineType.setId(id);
            updateExamineType.setExamine(type);
            if (breMapper.updateById(updateExamineType) == 0) {
                return JsonVO.fail("修改单据[ " + breDO.getNumber() + " ]审核状态失败");
            }

            // 影响行数增加
            row++;
        }

        // 影响行数为0
        if (row == 0) {
            return JsonVO.fail("无可操作单据！");
        } else {
            if (type == 1) {
                return JsonVO.success("审核成功");
            } else {
                return JsonVO.success("反审核成功");
            }
        }
    }

    /**
     * @param file 导入文件
     * @return 导入结果
     * @author Hazenix 
     * 导入数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonVO<String> importData(MultipartFile file) {
        try {
            // 读取Excel数据
            List<PurchaseReturnImportExcel> importList = EasyExcel.read(file.getInputStream())
                    .head(PurchaseReturnImportExcel.class)
                    .sheet()
                    .headRowNumber(2) // 从第3行开始读取（跳过前两行表头）
                    .doReadSync();

            // 数据校验和处理
            // 获取文件名
            String filename = file.getOriginalFilename();
            // 检查单据资料部分
            // 获取第一行数据
            PurchaseReturnImportExcel first = importList.get(0);
            //供应商
            String supplier = first.getSupplier();
            SupplierDO supplierDO = supplierService.getOne(new QueryWrapper<SupplierDO>().select("id").eq("name", supplier));
            if (supplierDO == null) {
                return JsonVO.fail("[ " + filename + " ]供应商[ " + supplier + " ]未匹配!");
            }
            //单据日期
            String time = first.getTime();
            if (time == null) {
                return JsonVO.fail("[ " + filename + " ]单据日期不可为空!");
            }
            //单据编号
            String number = first.getNumber();
            if (first.getNumber() == null) {
                return JsonVO.fail("[ " + filename + " ]单据编号不可为空!");
            }
            long breCount = breMapper.selectCount(new QueryWrapper<BreDO>().eq("number", number));
            if (breCount != 0) {
                return JsonVO.fail("[ " + filename + " ]单据编号重复!");
            }
            //实际金额
            BigDecimal actual = first.getActual();
            if (actual == null) {
                return JsonVO.fail("[ " + filename + " ]实际金额不可为空!");
            }
            //实收金额
            BigDecimal money = first.getMoney();
            if (money == null) {
                return JsonVO.fail("[ " + filename + " ]实收金额不可为空!");
            }
            //结算账户
            String account = first.getAccount();
            AccountDO accountDO = accountService.getOne(new QueryWrapper<AccountDO>().select("id").eq("name", account));
            if (accountDO == null) {
                return JsonVO.fail("[ " + filename + " ]结算账户[ " + account + " ]不正确!");
            }
            //关联人员
            String people = first.getPeople();
            PeopleDO peopleDO = new PeopleDO();
            if (people != null) {
                peopleDO = peopleService.getOne(new QueryWrapper<PeopleDO>().select("id").eq("name", people));
                if (peopleDO == null) {
                    return JsonVO.fail("[ " + filename + " ]关联人员[ " + people + " ]未匹配!");
                }
            }
            // 检查商品资料部分
            int i = 0; //记录行数
            BigDecimal total = new BigDecimal("0"); //记录单据金额
            for (PurchaseReturnImportExcel line : importList) {
                i++;
                //商品名称
                String goods = line.getGoods();
                GoodsDO goodsDO = goodsService.getOne(new QueryWrapper<GoodsDO>().select("id").eq("name", goods));
                if (goodsDO == null) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行商品名称[ " + goods + " ]未匹配!");
                }
                line.setGoods(goodsDO.getId()); //将商品名称更新为商品id方便后续导入
                //仓库
                String warehouse = line.getGoods();
                WarehouseDO warehouseDO = warehouseService.getOne(new QueryWrapper<WarehouseDO>().select("id").eq("name", warehouse));
                if (warehouseDO == null) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行仓库[ " + warehouse + " ]未匹配!");
                }
                line.setWarehouse(warehouseDO.getId()); //将仓库名称更新为仓库id方便后续导入
                //单价
                BigDecimal price = line.getPrice();
                if (price == null || price.compareTo(new BigDecimal("0")) < 0) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行单价不正确!");
                }
                //数量
                BigDecimal nums = line.getNums();
                if (nums == null || nums.compareTo(new BigDecimal("0")) < 0) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行数量不正确!");
                }
                //折扣率
                BigDecimal discount = line.getDiscount();
                if (discount == null) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行折扣率不可为空!");
                }
                if (discount.compareTo(new BigDecimal("0")) < 0 || discount.compareTo(new BigDecimal("100")) > 0) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行折扣率不正确!");
                }
                //税率
                BigDecimal tax = line.getTax();
                if (tax == null) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行税率不可为空!");
                }
                if (tax.compareTo(new BigDecimal("0")) < 0 || tax.compareTo(new BigDecimal("100")) > 0) {
                    return JsonVO.fail("[ " + filename + " ]模板文件第" + i + "行税率不正确!");
                }
                //折扣额
                BigDecimal dsc = price.multiply(nums).multiply(discount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                line.setDsc(dsc);
                //金额
                BigDecimal total_goods = price.multiply(nums).subtract(dsc);
                line.setTotal_goods(total_goods);
                //税额
                BigDecimal tat = total_goods.multiply(tax).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                line.setTat(tat);
                //价税合计
                BigDecimal tpt = total_goods.add(tat);
                line.setTpt(tpt);
                //更新单据金额
                total = total.add(tpt);
            }
            //实际金额
            if (actual.compareTo(total) > 0) {
                return JsonVO.fail("[ " + filename + " ]实际金额不可大于单据金额[ " + total + " ]!");
            }
            //实收金额
            if (money.compareTo(actual) > 0) {
                return JsonVO.fail("[ " + filename + " ]实收金额不可大于实际金额[ " + actual + " ]!");
            }

            //导入采购退货单
            BreDO importBre = new BreDO();
            importBre.setFrame("sy0");
            importBre.setSupplier(supplierDO.getId());
            importBre.setTime(time);
            importBre.setNumber(number);
            importBre.setTotal(total);
            importBre.setActual(actual);
            importBre.setMoney(money);
            importBre.setAccount(accountDO.getId());
            if (peopleDO != null) {
                importBre.setPeople(peopleDO.getId());
            }
            importBre.setLogistics(first.getLogistics());
            importBre.setData(first.getData());
            importBre.setExamine(0);
            importBre.setNucleus(0);
            importBre.setCse(0);
            importBre.setInvoice(0);
            importBre.setCheck(0);
            importBre.setUser(userHolder.getCurrentUser().getId()); //获取当前用户id
            breMapper.insert(importBre);
            //获取自动生成的id
            String id = importBre.getId();

            //导入采购退货单详情
            for (PurchaseReturnImportExcel line : importList) {
                BreInfoDO importBreInfo = new BreInfoDO();
                importBreInfo.setPid(id);
                importBreInfo.setGoods(line.getGoods());
                importBreInfo.setAttr(line.getAttr());
                importBreInfo.setUnit(line.getUnit());
                importBreInfo.setWarehouse(line.getWarehouse());
                importBreInfo.setBatch(line.getBatch());
                importBreInfo.setMfd(line.getMfd());
                importBreInfo.setPrice(line.getPrice());
                importBreInfo.setNums(line.getNums());
                importBreInfo.setSerial(line.getSerial());
                importBreInfo.setDiscount(line.getDiscount());
                importBreInfo.setDsc(line.getDsc());
                importBreInfo.setTotal(line.getTotal_goods());
                importBreInfo.setTax(line.getTax());
                importBreInfo.setTat(line.getTat());
                importBreInfo.setTpt(line.getTpt());
                importBreInfo.setData(line.getData());
                breInfoService.save(importBreInfo);
            }
            return JsonVO.success("");
        } catch (IOException e) {
            log.error("文件读取失败", e);
            return JsonVO.fail("文件读取失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("导入数据异常", e);
            return JsonVO.fail("导入数据异常：" + e.getMessage());
        }
    }

    /**
     * @param ids 导出采购退货单简单报表id
     * @return 带有导出报表信息的响应实体
     * @author Hazenix 
     * 导出简单报表
     */
    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> exportBre(List<String> ids) {
        List<PurchaseReturnExportExcel> exportList = new ArrayList<>();
        //统计总数
        int nums = 0;
        //统计总单据金额
        BigDecimal total = new BigDecimal("0");
        //统计总实际金额
        BigDecimal actual = new BigDecimal("0");
        //统计总单据收款
        BigDecimal money = new BigDecimal("0");
        //统计总核销金额
        BigDecimal cancel = new BigDecimal("0");
        //统计总单据费用
        BigDecimal cost = new BigDecimal("0");
        for (String id : ids) {
            //通过id获得BreDO实体
            BreDO breDO = breMapper.selectById(id);
            //添加到列表中
            PurchaseReturnExportExcel line = new PurchaseReturnExportExcel();
            //所属组织
            FrameDO frameDO = frameService.getById(breDO.getFrame());
            if (frameDO != null) {
                line.setFrame(frameDO.getName());
            }
            //供应商
            line.setSupplier(supplierService.getById(breDO.getSupplier()).getName());
            //单据时间
            line.setTime(breDO.getTime());
            //单据编号
            line.setNumber(breDO.getNumber());
            //单据金额
            line.setTotal(breDO.getTotal().toString());
            //实际金额
            line.setActual(breDO.getActual().toString());
            //单据收款
            line.setMoney(breDO.getMoney().toString());
            //核销金额
            if (breDO.getNucleus() == 2) { //已核销
                line.setCancel(breDO.getMoney().toString());
            } else { //未核销
                line.setCancel("0");
            }
            //单据费用
            line.setCost(breDO.getCost().toString());
            //关联人员
            if (breDO.getPeople() != null) {
                PeopleDO peopleDO = peopleService.getById(breDO.getPeople());
                if (peopleDO != null){
                    line.setPeople(peopleDO.getName());
                }
            }
            //审核状态
            switch (breDO.getExamine()) {
                case 0:
                    line.setExamine("未审核");
                    break;
                case 1:
                    line.setExamine("已审核");
                    break;
            }
            //核销状态
            switch (breDO.getNucleus()) {
                case 0:
                    line.setNucleus("未核销");
                    break;
                case 1:
                    line.setNucleus("部分核销");
                    break;
                case 2:
                    line.setNucleus("已核销");
                    break;
            }
            //费用状态
            switch (breDO.getCse()) {
                case 0:
                    line.setCse("未结算");
                    break;
                case 1:
                    line.setCse("部分结算");
                    break;
                case 2:
                    line.setCse("已结算");
                    break;
                case 3:
                    line.setCse("无需结算");
                    break;
            }
            //发票状态
            switch (breDO.getInvoice()) {
                case 0:
                    line.setInvoice("未开票");
                    break;
                case 1:
                    line.setInvoice("部分开票");
                    break;
                case 2:
                    line.setInvoice("已开票");
                    break;
                case 3:
                    line.setInvoice("无需开票");
                    break;
            }
            //核对状态
            switch (breDO.getCheck()) {
                case 0:
                    line.setCheck("未核对");
                    break;
                case 1:
                    line.setCheck("已核对");
                    break;
            }
            //制单人
            UserDO userDO = userService.getById(breDO.getUser());
            if (userDO != null) {
                line.setUser(userDO.getName());
            } else {
                line.setUser("默认");
            }
            //备注信息
            line.setData(breDO.getData());
            exportList.add(line);
            //更新总计信息
            nums++;
            total = total.add(breDO.getTotal());
            //统计总实际金额
            actual = actual.add(breDO.getActual());
            //统计总单据收款
            money = money.add(breDO.getMoney());
            //统计总核销金额
            if (breDO.getNucleus() == 2) {
                cancel = cancel.add(breDO.getMoney());
            }
            //统计总单据费用
            cost = cost.add(breDO.getCost());
        }

        //添加总计信息
        PurchaseReturnExportExcel totalLine = new PurchaseReturnExportExcel();
        totalLine.setFrame("总数:" + nums);
        totalLine.setSupplier("总单据金额:" + total);
        totalLine.setTime("总实际金额:" + actual);
        totalLine.setNumber("总单据收款:" + money);
        totalLine.setTotal("总核销金额:" + cancel);
        totalLine.setActual("总单据费用:" + cost);
        exportList.add(totalLine);
        // 定义输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 生成Excel
        excel.export("采购退货单列表", out, PurchaseReturnExportExcel.class, exportList);
        // 响应到前端
        HttpHeaders headers = new HttpHeaders();
        String filename = "采购退货单列表-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx";

        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        ResponseEntity<byte[]> res = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        out.close();
        return res;
    }

    /**
     * 流式导出采购退货单（优化版）
     * 使用游标分页，避免大数据量导致的 OOM
     * @param ids 采购退货单ID列表
     * @return 带有导出数据的响应实体
     */
    @SneakyThrows
    public ResponseEntity<byte[]> exportBreStreaming(List<String> ids) {
        // 创建游标分页查询器
        CursorPageQuery<PurchaseReturnExportExcel> pageQuery = new CursorPageQuery<PurchaseReturnExportExcel>() {
            private int currentIndex = 0;
            private Integer lastId = 0;

            @Override
            public List<PurchaseReturnExportExcel> nextPage(Object lastId, int pageSize) {
                if (currentIndex >= ids.size()) {
                    return Collections.emptyList();
                }

                List<PurchaseReturnExportExcel> page = new ArrayList<>();
                int endIndex = Math.min(currentIndex + pageSize, ids.size());

                for (int i = currentIndex; i < endIndex; i++) {
                    String id = ids.get(i);
                    BreDO breDO = breMapper.selectById(id);
                    if (breDO == null) {
                        continue;
                    }

                    PurchaseReturnExportExcel line = buildExportLine(breDO);
                    page.add(line);
                    lastId = i;
                }

                currentIndex = endIndex;
                this.lastId = (Integer) lastId;
                return page;
            }

            @Override
            public boolean hasMore(Object lastId) {
                return currentIndex < ids.size();
            }
        };

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        excel.exportStreaming("采购退货单列表", out,
                PurchaseReturnExportExcel.class, pageQuery, 5000, true);
        // (TODO 这里可以接受返回的`ExportResult`,然后将导出过程中的数据分析持久化到数据库)

        HttpHeaders headers = new HttpHeaders();
        String filename = "采购退货单列表-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx.gz";
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        // 设置 GZIP 压缩响应头，浏览器会自动解压
        headers.set("Content-Encoding", "gzip");

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    /**
     * 构建导出行数据（从原有exportBre方法提取）
     */
    private PurchaseReturnExportExcel buildExportLine(BreDO breDO) {
        PurchaseReturnExportExcel line = new PurchaseReturnExportExcel();

        // 所属组织
        FrameDO frameDO = frameService.getById(breDO.getFrame());
        if (frameDO != null) {
            line.setFrame(frameDO.getName());
        }

        // 供应商
        line.setSupplier(supplierService.getById(breDO.getSupplier()).getName());

        // 其他字段赋值...
        line.setTime(breDO.getTime());
        line.setNumber(breDO.getNumber());
        line.setTotal(breDO.getTotal() != null ? breDO.getTotal().toString() : "0");
        line.setActual(breDO.getActual() != null ? breDO.getActual().toString() : "0");
        line.setMoney(breDO.getMoney() != null ? breDO.getMoney().toString() : "0");

        if (breDO.getNucleus() == 2) {
            line.setCancel(breDO.getMoney() != null ? breDO.getMoney().toString() : "0");
        } else {
            line.setCancel("0");
        }

        line.setCost(breDO.getCost() != null ? breDO.getCost().toString() : "0");

        if (breDO.getPeople() != null) {
            PeopleDO peopleDO = peopleService.getById(breDO.getPeople());
            if (peopleDO != null) {
                line.setPeople(peopleDO.getName());
            }
        }

        // 状态字段转换
        line.setExamine(breDO.getExamine() == 0 ? "未审核" : "已审核");
        line.setNucleus(breDO.getNucleus() == 0 ? "未核销" : breDO.getNucleus() == 1 ? "部分核销" : "已核销");
        line.setCse(breDO.getCse() == 0 ? "未结算" : breDO.getCse() == 1 ? "部分结算" : breDO.getCse() == 2 ? "已结算" : "无需结算");
        line.setInvoice(breDO.getInvoice() == 0 ? "未开票" : breDO.getInvoice() == 1 ? "部分开票" : breDO.getInvoice() == 2 ? "已开票" : "无需开票");
        line.setCheck(breDO.getCheck() == 0 ? "未核对" : "已核对");

        UserDO userDO = userService.getById(breDO.getUser());
        line.setUser(userDO != null ? userDO.getName() : "默认");
        line.setData(breDO.getData());

        return line;
    }

    /**
     * @param ids 导出采购退货单详细报表id
     * @return 带有导出详细报表信息的响应实体
     * @author Hazenix 
     * 导出详细报表
     */
    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> exportBreInfo(List<String> ids) {
        // 创建zip输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        // 定义输出流用于生成Excel
        ByteArrayOutputStream excelOut = new ByteArrayOutputStream();

        for (String id : ids) {
            // 获取BreDO
            BreDO breDO = breMapper.selectById(id);

            // 表头
            List<List<String>> excelHead = new ArrayList<>();
            // 第一行：合并A1到A11为"采购退货单"
            for (int i = 0; i < 11; i++) {
                excelHead.add(Collections.singletonList("采购退货单"));
            }

            // 表体
            List<List<Object>> excelBody = new ArrayList<>();

            // 第二行：供应商信息
            List<Object> row2 = new ArrayList<>();

            row2.add("供应商:" + supplierService.getById(breDO.getSupplier()).getName()); //A2,由供应商id获取供应商名称
            row2.add("单据日期:" + breDO.getTime());  // B2
            row2.add("单据编号:" + breDO.getNumber());  // C2
            for (int i = 3; i < 11; i++) {
                row2.add("");  // D2-K2为空
            }
            excelBody.add(row2);

            // 第三行数据：采购退货单详情表头
            List<Object> row3 = new ArrayList<>();
            row3.add("商品名称");   // A3
            row3.add("规格型号");   // B3
            row3.add("辅助属性");   // C3
            row3.add("单位");      // D3
            row3.add("仓库");      // E3
            row3.add("单价");      // F3
            row3.add("数量");      // G3
            row3.add("折扣率");    // H3
            row3.add("折扣额");    // I3
            row3.add("金额");      // J3
            row3.add("备注信息");   // K3
            excelBody.add(row3);

            // 商品明细行
            List<BreInfoDO> breInfoDOList = breInfoService.list(new QueryWrapper<BreInfoDO>().eq("pid", id));
            for (BreInfoDO breInfoDO : breInfoDOList) {
                List<Object> breInfoRow = new ArrayList<>();
                breInfoRow.add(goodsService.getById(breInfoDO.getGoods()).getName()); //由商品id获取商品名称
                breInfoRow.add(null); //规格型号
                breInfoRow.add(breInfoDO.getAttr()); //辅助属性
                breInfoRow.add(breInfoDO.getUnit()); //单位
                breInfoRow.add(warehouseService.getById(breInfoDO.getWarehouse()).getName()); //由仓库id获取仓库名称
                breInfoRow.add(breInfoDO.getPrice()); //单价
                breInfoRow.add(breInfoDO.getNums()); //数量
                breInfoRow.add(breInfoDO.getDiscount()); //折扣率
                breInfoRow.add(breInfoDO.getDsc()); //折扣额
                breInfoRow.add(breInfoDO.getTotal()); //金额
                breInfoRow.add(breInfoDO.getData()); //备注信息
                excelBody.add(breInfoRow);
            }

            // 底部汇总信息行
            List<Object> summaryRow = new ArrayList<>();
            summaryRow.add("单据金额:" + breDO.getTotal()); // A列
            summaryRow.add("单据费用:" + breDO.getCost()); // B列
            summaryRow.add("实际金额:" + breDO.getActual()); // C列
            // D列
            if (breDO.getNucleus() == 2) {
                summaryRow.add("核销金额:" + breDO.getMoney());
            } else {
                summaryRow.add("核销金额:0");
            }
            summaryRow.add("结算账户:" + accountService.getById(breDO.getAccount()).getName()); // E列
            summaryRow.add("发票信息:" + breDO.getInvoice()); // F列
            // G列
            if (breDO.getPeople() != null) {
                PeopleDO peopleDO = peopleService.getById(breDO.getPeople());
                if (peopleDO != null) {
                    summaryRow.add("关联人员:" + peopleDO.getName());
                } else {
                    summaryRow.add("关联人员:");
                }
            } else {
                summaryRow.add("关联人员:");
            }
            summaryRow.add("物流信息:" + breDO.getLogistics()); // H列
            summaryRow.add("备注信息:" + breDO.getData()); // I列
            summaryRow.add(""); // J列空
            summaryRow.add(""); // K列空
            excelBody.add(summaryRow);

            // 生成Excel
            EasyExcel.write(excelOut)
                    .head(excelHead)  // 手动指定表头
                    .sheet("采购退货单详情")
                    .doWrite(excelBody);

            // 添加到ZIP
            String filename = breDO.getNumber() + ".xlsx";
            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);
            zos.write(excelOut.toByteArray());
            zos.closeEntry();
        }
        excelOut.close();
        zos.finish();

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        String zipName = "采购退货单_" + Instant.now().getEpochSecond() + ".zip";
        // 设置文件名编码方式
        String encodedFilename = URLEncoder.encode(zipName, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");  // 将+号替换为%20
        headers.setContentDispositionFormData("attachment", encodedFilename);
        headers.setContentType(MediaType.valueOf("application/zip"));
        ResponseEntity<byte[]> res = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        baos.close();
        return res;
    }
}
