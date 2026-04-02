package com.zeroone.star.sale.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDTO;
import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.project.dto.j4.sale.SaleListDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnGenerateDTO;
import com.zeroone.star.project.query.j4.sale.SaleQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import com.zeroone.star.sale.entity.SaleDO;
import com.zeroone.star.sale.entity.SaleForm;
import com.zeroone.star.sale.entity.SaleInfoDO;
import com.zeroone.star.sale.mapper.SaleFormInfoMapper;
import com.zeroone.star.sale.mapper.SaleFormMapper;
import com.zeroone.star.sale.mapper.SaleInfoMapper;
import com.zeroone.star.sale.mapper.SaleMapper;
import com.zeroone.star.sale.service.SaleFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaleFormServiceImpl implements SaleFormService {

    @Resource
    private SaleFormMapper saleFormMapper;

    @Resource
    private SaleFormInfoMapper saleFormInfoMapper;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private SaleInfoMapper saleInfoMapper;

    @Override
    public JsonVO<PageDTO<SaleListDTO>> getSaleList(SaleQuery query) {
        try {
            // 防止非法分页参数
            long pageIndex = query.getPageIndex() < 1 ? 1 : query.getPageIndex();
            long pageSize = query.getPageSize() < 1 ? 10 : query.getPageSize();

            Page<SaleListDTO> mpPage = new Page<>(pageIndex, pageSize);
            Page<SaleListDTO> resultPage = saleFormMapper.selectSaleList(mpPage, query);

            PageDTO<SaleListDTO> pageDTO = PageDTO.create(resultPage);
            return JsonVO.success(pageDTO);

        } catch (Exception e) {
            log.error("获取销售单列表失败", e);
            return JsonVO.fail(null);
        }
    }

    @Override
    public JsonVO<List<SaleDetailDTO>> getSaleDetail(String customer, LocalDateTime time, String number) {
        try {
            // 参数校验：至少需要一个查询条件
            if ((customer == null || customer.trim().isEmpty()) &&
                    time == null &&
                    (number == null || number.trim().isEmpty())) {
                return JsonVO.create(null, ResultStatus.PARAMS_INVALID.getCode(), "至少需要提供一个查询条件");
            }

            // 根据条件查询销售单详情
            List<SaleDetailDTO> saleDetails = saleFormInfoMapper.selectSaleDetailByCondition(customer, time, number);
            if (saleDetails == null || saleDetails.isEmpty()) {
                return JsonVO.create(null, ResultStatus.FAIL.getCode(), "未找到对应的销售单详情");
            }

            return JsonVO.success(saleDetails);

        } catch (Exception e) {
            log.error("获取销售单详情失败, customer: {}, time: {}, number: {}", customer, time, number, e);
            return JsonVO.fail(null);
        }
    }

    @Override
    public JsonVO<SaleReturnGenerateDTO> getGenerateReturnData(String saleId) {
        try {
            if (saleId == null || saleId.trim().isEmpty()) {
                return JsonVO.create(null, ResultStatus.PARAMS_INVALID.getCode(), "销售单ID不能为空");
            }

            // 获取销售单基础信息 - 使用 selectSaleForReturn 方法
            SaleForm saleForm = saleFormMapper.selectSaleForReturn(saleId);
            if (saleForm == null) {
                return JsonVO.create(null, ResultStatus.FAIL.getCode(), "未找到对应的销售单");
            }

            // 基于销售单数据构建退货单数据
            SaleReturnGenerateDTO returnData = new SaleReturnGenerateDTO();
            BeanUtils.copyProperties(saleForm, returnData);

            // 设置退货单特定字段
            returnData.setSource(saleForm.getId()); // 关联原销售单
            returnData.setTime(LocalDateTime.now()); // 退货单时间为当前时间

            return JsonVO.success(returnData);

        } catch (Exception e) {
            log.error("获取销售退货单生成数据失败, saleId: {}", saleId, e);
            return JsonVO.fail(null);
        }
    }

    /**
     * 新增销售单
     * @param saleDTO 销售单数据
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addSale(SaleDTO saleDTO) {
        ///为销售单 生成id 使用雪花算法
        String saleId = IdUtil.getSnowflake().nextIdStr();

        SaleDO sale = new SaleDO();
        BeanUtils.copyProperties(saleDTO, sale);
        sale.setId(saleId);
        saleDTO.setId(saleId);
        /// 遍历销售单详情 赋值所属ID
        List<SaleInfoDO> details = saleDTO.getItems().stream().map(item -> {
            SaleInfoDO saleInfo = new SaleInfoDO();
            BeanUtils.copyProperties(item, saleInfo);
            saleInfo.setPid(saleId);
            saleInfo.setId(IdUtil.getSnowflake().nextIdStr());
            return saleInfo;
        }).collect(Collectors.toList());

        /// 计算折扣率,金额,税额,价税合计等
        calculateTotal(sale,details);
        /// 插入sell表
        saleMapper.insert(sale);
        /// 插入sell_info详情表
        saleInfoMapper.insertBatch(details);
    }

    /**
     * 修改销售单
     * @param saleDTO 销售单数据
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSale(SaleDTO saleDTO) {
        /// 先获取销售单id 判断在数据库中是否存在
        String saleId = saleDTO.getId();
        SaleDO existing = saleMapper.selectById(saleId);
        if(existing == null){
            throw new IllegalArgumentException("id:" + saleId + ",销售单不存在");
        }
        /// 销售单存在的话 判断是否已审核 已审核不能修改
        if(Boolean.TRUE.equals(existing.getExamine())){
            throw new IllegalArgumentException("id:" + saleId + ",销售单已审核,不能修改");
        }
        /// 未审核 进行修改
        SaleDO sale = new SaleDO();
        BeanUtils.copyProperties(saleDTO, sale);
        sale.setId(saleId);
        /// 拷贝一份销售单详情,并设置Pid,得到一个销售单详情的集合
        List<SaleInfoDO> details = saleDTO.getItems().stream().map(item -> {
            SaleInfoDO saleInfo = new SaleInfoDO();
            BeanUtils.copyProperties(item, saleInfo);
            /// 设置pid为销售单id
            saleInfo.setPid(saleId);
            saleInfo.setId(IdUtil.getSnowflake().nextIdStr());
            return saleInfo;
        }).collect(Collectors.toList());
        /// 重新计算折扣率,金额,税额,价税合计等
        calculateTotal(sale, details);

        /// 更新主表
        saleMapper.updateById(sale);
        /// 批量删除旧详情并插入新销售单详情
        saleInfoMapper.deleteBatchByPid(saleId);
        saleInfoMapper.insertBatch(details);
    }

    /**
     * 批量删除销售单
     * @param ids 销售单id
     * @return 删除的个数
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchDeleteSales(List<String> ids) {
        /// 先查询到ids列表中的所有销售单
        List<SaleDO> sales = saleMapper.selectBatchIds(ids);
        /// 把每一个销售单的详情id存入一个Map中
        Map<String,SaleDO> saleMap = new HashMap<>();
        for (SaleDO sale : sales) {
            saleMap.put(sale.getId(),sale);
        }
        /// 批量校验
        for (String id : ids) {
            SaleDO sale = saleMap.get(id);
            if(sale == null){
                throw new IllegalArgumentException("id:" + id + ",销售单不存在");
            }
            if(Boolean.TRUE.equals(sale.getExamine())){
                throw new IllegalArgumentException("id:" + id + ",销售单已审核,不能删除");
            }
            if(sale.getNucleus() != 0){
                throw new IllegalArgumentException("id:" + id + ",销售单已核销,不能删除");
            }
        }
        /// 执行批量删除 同时删除主表以及详情表有关的数据
        saleMapper.deleteBatchWithDetails(ids);

        /// 最终只返回删除主表的个数
        return ids.size();
    }

    /**
     * 核对/反核对销售单
     * @param ids 销售单id
     * @param check 核对/反核对
     * @return 核对/反核对个数
     */

    @Override
    public Integer checkSales(List<String> ids, boolean check) {
        /// 先获取到ids列表中所有销售单
        List<SaleDO> sales = saleMapper.selectBatchIds(ids);
        /// 把每一个销售单的详情id存入一个Map中
        Map<String,SaleDO> saleMap = new HashMap<>();
        for (SaleDO sale : sales) {
            saleMap.put(sale.getId(),sale);
        }
        /// 允许核对的id
        List<String> validIds = new ArrayList<>();
        /// 批量校验
        for (String id : ids) {
            SaleDO sale = saleMap.get(id);
            if(sale == null){
                throw new IllegalArgumentException("id:" + id + ",销售单不存在");
            }
            if(Boolean.TRUE.equals(sale.getExamine())){
                throw new IllegalArgumentException("id:" + id + ",销售单已审核,不能核对");
            }

            /// 核对操作校验
            if(check){
                if(Boolean.TRUE.equals(sale.getCheck())){
                    throw new IllegalArgumentException("id:" + id + ",销售单已核对,不能重复核对");
                }
                /// 允许核对
                validIds.add(id);
            }else{
                /// 反核对操作校验
                if(Boolean.TRUE.equals(sale.getCheck())){
                    /// 只有check为true的时候 才允许反核对
                    validIds.add(id);
                }else{
                    throw new IllegalArgumentException("id:" + id + ",销售单未核对,不能反核对");
                }
            }
        }

        if(validIds.isEmpty()){
            return 0;
        }
        /// 批量更新数据库
        return saleMapper.updateCheck(validIds,check);
    }

    /**
     * 审核销售单
     * @param ids 销售单id
     * @param examine 审核/反审核
     * @return 审核/反审核个数
     */

    @Override
    public Integer examineSales(List<String> ids, boolean examine) {
        /// 先获取到ids列表中所有销售单
        List<SaleDO> sales = saleMapper.selectBatchIds(ids);
        /// 把每一个销售单的详情id存入一个Map中
        Map<String,SaleDO> saleMap = new HashMap<>();
        for (SaleDO sale : sales) {
            saleMap.put(sale.getId(),sale);
        }
        /// 允许审核的id
        List<String> validIds = new ArrayList<>();
        /// 批量校验
        for (String id : ids) {
            SaleDO sale = saleMap.get(id);
            if(sale == null){
                throw new IllegalArgumentException("id:" + id + ",销售单不存在");
            }
            /// 审核操作校验
            if(examine){
                if(Boolean.TRUE.equals(sale.getExamine())){
                    throw new IllegalArgumentException("id:" + id + ",销售单已审核,不能重复审核");
                }
                if(sale.getNucleus()!=0){
                    throw new IllegalArgumentException("id:" + id + ",销售单已核销,不能审核");
                }
                if(!Boolean.TRUE.equals(sale.getCheck())){
                    throw new IllegalArgumentException("id:" + id + ",销售单未核对,不能审核");
                }
                /// 允许审核
                validIds.add(id);
            }else{
                /// 反审核操作校验
                if(!Boolean.TRUE.equals(sale.getExamine())){
                    throw new IllegalArgumentException("id:" + id + ",销售单未审核,不能反审核");
                }
                if(sale.getNucleus()!=0){
                    throw new IllegalArgumentException("id:" + id + ",销售单已核销,不能反审核");
                }
                /// 只有examine为true的时候且nucleus为0 才允许反审核
                validIds.add(id);
            }
        }

        if(validIds.isEmpty()){
            return 0;
        }
        /// 批量更新数据库
        return saleMapper.updateExamine(validIds,examine);
    }

    /**
     * 计算销售单金额
     * @param sale 销售单
     * @param details 销售单详情
     */

    private void calculateTotal(SaleDO sale, List<SaleInfoDO> details){
        BigDecimal saleTotal = BigDecimal.ZERO;
        BigDecimal saleActual = BigDecimal.ZERO;

        details.forEach(detail -> {

            /// 增加空值检查
            if(detail.getPrice() == null || detail.getNums() == null || detail.getTax() == null) {
                throw new IllegalArgumentException("销售明细数据不完整");
            }

            ///如果说折扣率不为空
            if(detail.getDiscount().compareTo(BigDecimal.ZERO) != 0){
                /// 折扣额 = 单价 * 数量 * (1-折扣率/100)
                BigDecimal dsc = detail.getPrice().multiply(detail.getNums())
                        .multiply(BigDecimal.ONE.subtract(detail.getDiscount().divide(BigDecimal.valueOf(100),4, RoundingMode.HALF_UP)))
                        .setScale(2, RoundingMode.HALF_UP);
                detail.setDsc(dsc);
                /// 金额 = 单价 * 数量 - 折扣额
                BigDecimal total = detail.getPrice()
                        .multiply(detail.getNums()).subtract(dsc).setScale(2, RoundingMode.HALF_UP);
                detail.setTotal(total);
            }else{
                /// 折扣率为0
                detail.setDsc(BigDecimal.ZERO);
                BigDecimal total = detail.getPrice()
                        .multiply(detail.getNums()).setScale(2, RoundingMode.HALF_UP);
                detail.setTotal(total);
            }
            /// 税额 = 金额 * 税率
            BigDecimal tat = detail.getTotal().multiply(detail.getTax()).setScale(2, RoundingMode.HALF_UP);
            detail.setTat(tat);
            /// 价税合计 = 税额 + 金额
            BigDecimal tpt = detail.getTotal().add(tat).setScale(2, RoundingMode.HALF_UP);
            detail.setTpt(tpt);
        });

        /// 计算主表总金额,实际金额,实收金额
        for (SaleInfoDO detail : details) {
            saleTotal = saleTotal.add(detail.getTotal());
            saleActual = saleActual.add(detail.getTpt());
        }

        sale.setTotal(saleTotal);
        sale.setActual(saleActual);
        sale.setMoney(saleActual);/// 默认实收金额与实际金额相同
    }
}