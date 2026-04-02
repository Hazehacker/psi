package com.zeroone.star.purchase.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.query.j3.capital.PurchaseNoteQuery;
import com.zeroone.star.purchase.DO.BuyDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.purchase.VO.BuyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 采购单 Mapper 接口
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Mapper
@Component("purchaseBuyMapper")
public interface BuyMapper extends BaseMapper<BuyDO> {

     int updateByPurchaseId(BuyDO buyDO);
     int updateCheckById(@Param("id") String id, @Param("status") Integer status);
    /**
     * 自定义多表分页查询（关联商品表）
     * @param page 分页对象
     * @param wrapper mp自动构建的Wrapper （来自BuyDO的条件）
     * @param query 原始查询query (用于获取BuyDO中没有的条件，如name)
     * @return Page<BuyVO>
     */
    Page<BuyVO> selectPageWithProduct(
            Page<BuyVO> page,
            @Param(Constants.WRAPPER) Wrapper<BuyDO> wrapper,
            @Param("query") PurchaseNoteQuery query
            );
}
