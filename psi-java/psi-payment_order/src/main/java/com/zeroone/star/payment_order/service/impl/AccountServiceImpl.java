package com.zeroone.star.payment_order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.payment_order.config.BusinessException;
import com.zeroone.star.payment_order.entity.Account;
import com.zeroone.star.payment_order.entity.OmyInfo;
import com.zeroone.star.payment_order.mapper.AccountMapper;
import com.zeroone.star.payment_order.mapper.OmyInfoMapper;
import com.zeroone.star.payment_order.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.j6.payment_order.OmyInfoAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 资金账户 服务实现类
 * </p>
 *
 * @author 温白开
 * @since 2025-10-28
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private OmyInfoMapper omyInfoMapper;

    /**
     * 审核 (扣减资金账户余额)
     * @param info
     * @param sourceId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchBalance(OmyInfo info, String sourceId) {

        int rows = accountMapper.updateBalance(info.getAccount(), info.getMoney());
        if (rows == 0){
            throw new BusinessException("扣除账户余额失败");
        }
    }

    /**
     * 反审核 (恢复资金账户余额)
     * @param omyId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void UpdateBatchBalanceRestore(String omyId) {
        List<OmyInfo> infoList = omyInfoMapper.selectList(
                new QueryWrapper<OmyInfo>().eq("pid", omyId));
        for (OmyInfo info : infoList) {
            if (info.getMoney() != null && info.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                accountMapper.updateBalanceAdd(info.getAccount(), info.getMoney());
            }
        }
    }
    }

