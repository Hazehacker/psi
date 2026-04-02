package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Customer;
import com.zeroone.star.homepage.mapper.CustomerMapper;
import com.zeroone.star.homepage.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public BalanceVO queryBalance() {
        BalanceVO vo = new BalanceVO();
        BigDecimal balanceSum =this.baseMapper.queryBalanceSum();
        vo.setLabel("应收余款");
        if(balanceSum==null){
            vo.setValue("0.00元");
        }else{
            vo.setValue(balanceSum.setScale(2, RoundingMode.HALF_UP).toString()+"元");
        }
        vo.setTo("/report/fund/receivable-detail");
        return vo;
    }
}
