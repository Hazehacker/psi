package com.zeroone.star.capital.service.impl;

import com.zeroone.star.capital.mapper.BuyMapper;
import com.zeroone.star.capital.service.IBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.capital.DO.BuyDO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 采购单 服务实现类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Service("capitalIBuyService")
public class BuyServiceImpl extends ServiceImpl<BuyMapper, BuyDO> implements IBuyService{

}
