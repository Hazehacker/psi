package com.zeroone.star.homepage.service.impl;

import com.zeroone.star.homepage.entity.Goods;
import com.zeroone.star.homepage.mapper.GoodsMapper;
import com.zeroone.star.homepage.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Override
    public BalanceVO queryBalance() {
        BalanceVO vo = new BalanceVO();
        BigDecimal balanceSum =this.baseMapper.queryBalanceSum();
        vo.setLabel("库存成本");
        if(balanceSum==null){
            vo.setValue("0.00元");
        }else{
            vo.setValue(balanceSum.setScale(2, RoundingMode.HALF_UP).toString()+"元");
        }
        vo.setTo("/report/warehouse/inventory-summary");
        return vo;
    }
}
