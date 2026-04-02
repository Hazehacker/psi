package com.zeroone.star.payment_order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.payment_order.config.BusinessException;
import com.zeroone.star.payment_order.entity.Omy;
import com.zeroone.star.payment_order.entity.OmyBill;
import com.zeroone.star.payment_order.entity.OmyInfo;
import com.zeroone.star.payment_order.mapper.AccountMapper;
import com.zeroone.star.payment_order.mapper.OmyBillMapper;
import com.zeroone.star.payment_order.mapper.OmyInfoMapper;
import com.zeroone.star.payment_order.mapper.OmyMapper;
import com.zeroone.star.payment_order.service.AccountService;
import com.zeroone.star.payment_order.service.PaymentOrderService;
import com.zeroone.star.payment_order.service.SupplierService;
import com.zeroone.star.project.dto.DataTransCallBack;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.payment_order.*;
import com.zeroone.star.project.query.j6.payment_order.OmyQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentOrderImpl extends ServiceImpl<OmyMapper, Omy> implements PaymentOrderService {

    @Resource
    private MsPaymentOrderMapper mp;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private OmyBillMapper omyBillMapper;

    @Autowired
    private OmyInfoMapper omyInfoMapper;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 新增付款单
     *
     * @param addDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertOmyWithInfo(OmyWithInfoAddDTO addDto) {
        //1.校验单据编号唯一性
        QueryWrapper<Omy> wrapper = new QueryWrapper<>();
        wrapper.eq("number", addDto.getNumber());
        Long count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("单据编号重复：" + addDto.getNumber());
        }
        // 计算总金额（所有info表里的money之和）
        BigDecimal total = BigDecimal.ZERO;
        for (OmyInfoAddDTO info : addDto.getInfoList()) {
            if (info.getMoney() != null) {
                total = total.add(info.getMoney());
            }
        }
        Omy omy = mp.toOmy(addDto);
        omy.setTotal(total);
        baseMapper.insert(omy);
        String mainId = omy.getId();
        // 批量保存信息表
        for (OmyInfoAddDTO infoDto : addDto.getInfoList()) {
            OmyInfo omyInfo = mp.toOmyInfo(infoDto);
            omyInfo.setPid(mainId);
            omyInfoMapper.insert(omyInfo);
        }

        // 循环扣减每个账户余额
        for (OmyInfoAddDTO infoDto : addDto.getInfoList()) {
            if (infoDto.getMoney() != null && infoDto.getMoney().compareTo(BigDecimal.ZERO) > 0){
                // 转换DTO为DO
                OmyInfo omyInfo = mp.toOmyInfo(infoDto);
                omyInfo.setPid(mainId);
                accountService.updateBatchBalance(omyInfo, mainId);
            }
        }
        // 减少供应商应付款
        supplierService.updatePayableReduce(mainId);
        // 更新审核状态
        baseMapper.updateExamineStatus(mainId, 1);

        // 新增账单表记录（OmyBill）
        OmyBill omyBill = new OmyBill();
        omyBill.setPid(mainId); //所属单据
        omyBill.setType("付款单"); // 核销类型
        omyBill.setSource(addDto.getNumber()); // 关联单据
        omyBill.setTime(LocalDate.now()); // 核销时间
        omyBill.setMoney(BigDecimal.ZERO); // 核销金额
        // 保存OmyBill
        omyBillMapper.insert(omyBill);
        return "新增付款单成功";
    }


    /**
     * 修改付款单
     *
     * @param dto 修改后的内容
     * @param id 要修改的付款单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateOmyAll(OmyAllDTO dto, String id) {
        Integer examine = baseMapper.selectById(id).getExamine();
        if (examine == 0) {
            throw new BusinessException("单据未审核，不可修改" );
        }
        //如果单据是已审核状态，执行反审核流程
        if (examine == 1) {
            // 查询原始info记录
            QueryWrapper<OmyInfo> originalWrapper = new QueryWrapper<>();
            originalWrapper.eq("pid", id);
            List<OmyInfo> originalInfoList = omyInfoMapper.selectList(originalWrapper);
            // 查询原始主表记录
            Omy originalOmy = baseMapper.selectById(id);
            BigDecimal originalTotal = originalOmy.getTotal();
            // 恢复账户余额（使用原始info里的money）
            for (OmyInfo info : originalInfoList) {
                if (info.getMoney() != null && info.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                    // 使用账户Mapper直接恢复
                    accountMapper.updateBalanceAdd(info.getAccount(), info.getMoney());
                }

            }
            // 恢复供应商应付款
            supplierService.updatePayableRestore(id);
            // 更新审核状态为0（反审核）
            baseMapper.updateExamineStatus(id, 0);
            // 计算总金额
            BigDecimal total = dto.getInfoList().stream()
                    .map(OmyInfoAddDTO::getMoney)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 更新主表
            Omy omy = mp.toOmy(dto);
            omy.setId(id);
            // 使用计算的总金额
            omy.setTotal(total);
            baseMapper.updateById(omy);

            // 先删除原有信息表记录
            QueryWrapper<OmyInfo> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("pid", id);
            omyInfoMapper.delete(deleteWrapper);

            // 重新插入新的信息表记录
            for (OmyInfoAddDTO infoDto : dto.getInfoList()) {
                OmyInfo omyInfo = mp.toOmyInfo(infoDto);
                // 关联主表ID
                omyInfo.setPid(id);
                omyInfoMapper.insert(omyInfo);
            }

                //执行审核操作（批量）
                //批量扣减账户余额
                for (OmyInfoAddDTO infoDto : dto.getInfoList()) {
                    if (infoDto.getMoney() != null && infoDto.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        OmyInfo omyInfo = mp.toOmyInfo(infoDto);
                        omyInfo.setPid(id);
                        accountService.updateBatchBalance(omyInfo, id);
                    }
            }
            // 减少供应商应付款
            supplierService.updatePayableReduce(id);
            // 更新审核状态
            baseMapper.updateExamineStatus(id, 1);

        }
        return "修改付款单成功";
    }

    /**
     * 获取付款单列表
     *
     * @param condition 查询条件
     * @return
     */
    @Override
    public PageDTO<OmyWithBillDTO> listOmyWithBill(OmyQuery condition) {
        // 处理分页参数为null的情况
        Long pageIndex = condition.getPageIndex();
        Long pageSize = condition.getPageSize();
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1L;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10L;
        }

        // 直接使用MyBatis Plus的分页查询
        Page<Omy> page = new Page<>(pageIndex, pageSize);

        // 构建查询条件
        QueryWrapper<Omy> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(condition.getNumber()), "number", condition.getNumber());
        queryWrapper.eq(condition.getSupplier() != null, "supplier", condition.getSupplier());
        queryWrapper.eq(condition.getExamine() != null, "examine", condition.getExamine());
        queryWrapper.eq(condition.getNucleus() != null, "nucleus", condition.getNucleus());
        queryWrapper.eq(condition.getPeople() != null, "people", condition.getPeople());
        queryWrapper.eq(condition.getUser() != null, "user", condition.getUser());
        queryWrapper.gt(condition.getStartTime() != null, "time", condition.getStartTime());
        queryWrapper.lt(condition.getEndTime() != null, "time", condition.getEndTime());

        // 处理信息表条件 - 使用EXISTS子查询
        if (condition.getAccount() != null || condition.getData1() != null) {
            queryWrapper.exists("SELECT 1 FROM omy_info oi WHERE oi.pid = omy.id" +
                    (condition.getAccount() != null ? " AND oi.account = '" + condition.getAccount() + "'" : "") +
                    (condition.getData1() != null ? " AND oi.data LIKE '%" + condition.getData1() + "%'" : ""));
        }

        queryWrapper.orderByDesc("time");
        queryWrapper.select("id", "frame", "supplier", "time", "number", "total",
                "people", "examine", "nucleus", "user", "data");

        // 分页查询
        Page<Omy> pageRes = baseMapper.selectPage(page, queryWrapper);

        // 批量查询账单数据
        List<String> omyIds = new ArrayList<>();
        for (Omy omy : pageRes.getRecords()) {
            omyIds.add(omy.getId());
        }

        Map<String, BigDecimal> moneyMap = new HashMap<>(); // 直接存储money
        if (!omyIds.isEmpty()) {
            QueryWrapper<OmyBill> billQuery = new QueryWrapper<>();
            // 查询pid和money，但创建只包含money的DTO
            billQuery.select("pid", "money").in("pid", omyIds);
            List<OmyBill> bills = omyBillMapper.selectList(billQuery);

            for (OmyBill bill : bills) {
                moneyMap.put(bill.getPid(), bill.getMoney());
            }
        }

        // 使用回调组装结果
        final Map<String, BigDecimal> finalMoneyMap = moneyMap;
        return PageDTO.create(pageRes, new DataTransCallBack<OmyWithBillDTO, Omy>() {
            @Override
            public OmyWithBillDTO executeTrans(Omy omy) {
                BigDecimal money = finalMoneyMap.get(omy.getId());
                OmyWithBillDTO dto = new OmyWithBillDTO();
                dto.setOmy(mp.toOmyDTO(omy));

                // 创建只包含money的DTO
                OmyBillDTO billDTO = new OmyBillDTO();
                billDTO.setMoney(money != null ? money : BigDecimal.ZERO);
                dto.setBill(billDTO);
                return dto;
            }
        });
    }

    /**
     * 获取指定付款单详情
     *
     * @param id  要获取详情的付款单ID
     * @return
     */
    @Override
    public OmyWithInfoDTO getOmyWithInfoById(String id) {
        OmyWithInfoDTO result = new OmyWithInfoDTO();
        LambdaQueryWrapper<Omy> omyWrapper = new LambdaQueryWrapper<>();
        omyWrapper.select(Omy::getSupplier, Omy::getTime, Omy::getNumber, Omy::getTotal, Omy::getPeople,
                Omy::getData, Omy::getFile).eq(Omy::getId, id);
        //查询主表
        Omy sample = baseMapper.selectOne(omyWrapper);
        result.setOmy(mp.toOmyDTO(sample));
        //查询关联信息表
        LambdaQueryWrapper<OmyInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.select(OmyInfo::getAccount, OmyInfo::getMoney,
                OmyInfo::getSettle, OmyInfo::getData1).eq(OmyInfo::getPid, id);
        List<OmyInfo> infoList = omyInfoMapper.selectList(infoWrapper);
        // 转换为DTO列表
        List<OmyInfoDTO> infoDTOList = new ArrayList<>();
        for (OmyInfo info : infoList) {
            infoDTOList.add(mp.toOmyInfoDTO(info));
        }
        result.setInfoList(infoDTOList);
        return result;
    }

    /**
     * 批量删除付款单
     *
     * @param ids 要删除的付款单ID列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeOmy(List<String> ids) {
        //校验单据的审核状态，已审核不可修改
        List<Omy> omyList = baseMapper.selectBatchIds(ids);
        for (Omy omy : omyList) {
            Integer examine = omy.getExamine();
            if (examine == 1) {
                throw new BusinessException(omy.getId() + "单据已审核，不可删除");
            }
        }
        //批量删除主表记录
        baseMapper.deleteBatchIds(ids);
        //批量删除信息表记录
        QueryWrapper<OmyInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.in("pid", ids);
        omyInfoMapper.delete(infoWrapper);
        //批量删除账单表记录
        QueryWrapper<OmyBill> billWrapper = new QueryWrapper<>();
        billWrapper.in("pid", ids);
        omyBillMapper.delete(billWrapper);
        return "删除付款单成功";
    }

    /**
     * 批量审核与反审核付款单
     *
     * @param ids 要切换状态（审核/反审核）的付款单ID列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateExamineStatus(List<String> ids) {
        for (String id : ids) {
            Omy omy = baseMapper.selectById(id);
            //切换审核状态
            Integer omyExamine = omy.getExamine();
            List<OmyInfo> infoList = omyInfoMapper.selectList(
                    new QueryWrapper<OmyInfo>().eq("pid", id));
            // 重新计算总金额
            BigDecimal recalculatedTotal = BigDecimal.ZERO;
            for (OmyInfo info : infoList) {
                if (info.getMoney() != null) {
                    recalculatedTotal = recalculatedTotal.add(info.getMoney());
                }
            }
            //当前未审核，执行审核流程
            if (omyExamine == 0) {
                // 循环扣减每个账户余额
                for (OmyInfo info : infoList) {
                    if (info.getMoney() != null && info.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        accountService.updateBatchBalance(info, id);
                    }
                }
                // 减少供应商应付款
                supplierService.updatePayableReduce(id, recalculatedTotal);
                // 更新审核状态
                baseMapper.updateExamineStatus(id, 1);
            }
            //当前已审核，执行反审核流程
            if (omyExamine == 1) {
                // 恢复账户余额
                accountService.UpdateBatchBalanceRestore(id);
                // 恢复供应商应付款
                supplierService.updatePayableRestore(id,recalculatedTotal);
                // 更新审核状态
                baseMapper.updateExamineStatus(id, 0);
            }
        }
        //返回状态文本
        return "操作单据（切换审核状态）成功！";
    }
}
