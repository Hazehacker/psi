package com.zeroone.star.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zeroone.star.purchase.DO.GoodsDO;
import com.zeroone.star.purchase.mapper.GoodsMapper;
import com.zeroone.star.purchase.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsDO> implements IGoodsService {


    /**
     * @author xiaoliu
     * 根据商品名称、编号、规格型号查询商品id
     * @param goods
     * @param number
     * @param spec
     * @return
     */
    @Override
    public String findGoodsId(String goods, String number, String spec) {
        // 1.构建查询构造器
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();

        // 2.构建查询条件
        queryWrapper.eq("name",goods);
        queryWrapper.eq("number",number);
        queryWrapper.eq("spec",spec);

        // 3.执行查询
        GoodsDO goodsDO = this.getOne(queryWrapper);

        // 4.为空抛异常
        if (goodsDO == null) {
            throw new RuntimeException("商品不存在");
        }

        // 4.返回数据
        return goodsDO.getId();
    }
}
