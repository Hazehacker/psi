package com.zeroone.star.homepage.service;

import com.zeroone.star.homepage.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
public interface ICustomerService extends IService<Customer> {

    BalanceVO queryBalance();
}
