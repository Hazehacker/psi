package com.zeroone.star.report.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.query.j8.report.ProductStockBalanceQuery;
import com.zeroone.star.report.entity.Balance.ProductStockBalanceDetailDO;
import com.zeroone.star.report.entity.Balance.ProductStockBalanceHeadDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductStockBalanceMapper {

    /** 商品层分页（跨仓汇总） */
    IPage<ProductStockBalanceHeadDO> pageHeads(
            Page<?> page,
            @Param("q") ProductStockBalanceQuery q);

    /** 商品层不分页（导出用） */
    List<ProductStockBalanceHeadDO> listHeads(@Param("q") ProductStockBalanceQuery q);

    /** 明细层：按商品ID集合，返回各商品在不同仓库的聚合行 */
    List<ProductStockBalanceDetailDO> listDetailsByGoodsIds(
            @Param("q") ProductStockBalanceQuery q,
            @Param("goodsIds") List<String> goodsIds);
}