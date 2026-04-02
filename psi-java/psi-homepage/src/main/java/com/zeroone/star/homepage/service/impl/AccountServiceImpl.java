package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Account;
import com.zeroone.star.homepage.mapper.AccountMapper;
import com.zeroone.star.homepage.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.vo.j1.homepage.AccountBalanceVO;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import com.zeroone.star.project.vo.j1.homepage.ListVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 资金账户 服务实现类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    public BalanceVO queryBalance() {
        //1.创建vo
        BalanceVO vo = new BalanceVO();
        //2.查询数据
        BigDecimal balanceSum =this.baseMapper.queryBalanceSum();
        vo.setLabel("资金余额");
        //3.判断数据是否为空
        if(balanceSum==null){
            vo.setValue("0.00元");
        }else{
            vo.setValue(balanceSum.setScale(2, RoundingMode.HALF_UP).toString()+"元");
        }
        vo.setTo("/report/fund/current-account");
        return vo;
    }

    @Override
    public ListVO<AccountBalanceVO> queryAccountBalance() {
        //1.查询数据
        List<AccountBalanceVO> listVO = this.baseMapper.queryAccountBalance();
        //2.是否为空
        if(listVO==null||listVO.isEmpty()){
            return new ListVO<>(Collections.emptyList());
        }
        listVO.forEach(vo -> {
            BigDecimal value = vo.getValue();
            if (value != null) {
                vo.setValue(value.setScale(2, RoundingMode.HALF_UP));
            }else{
                vo.setValue(BigDecimal.ZERO);
            }
        });
        return new ListVO<AccountBalanceVO>(listVO);
    }
}
