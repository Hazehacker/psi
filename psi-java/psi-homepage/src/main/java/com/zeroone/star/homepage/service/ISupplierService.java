package com.zeroone.star.homepage.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.homepage.entity.Supplier;
import com.zeroone.star.project.vo.j1.homepage.BalanceVO;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author Duuuuuu
 * @since 2025-10-28
 */
public interface ISupplierService extends IService<Supplier> {

    BalanceVO queryBalance();
}
