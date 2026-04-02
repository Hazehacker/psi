package com.zeroone.star.sale.mapper;

import com.zeroone.star.sale.entity.SaleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleMapper{
    void deleteBatchWithDetails(List<String> ids);

    Integer updateCheck(List<String> ids , boolean check);

    Integer updateExamine(List<String> ids, boolean examine);

    void insert(SaleDO sale);

    SaleDO selectById(String saleId);

    void updateById(SaleDO sale);

    List<SaleDO> selectBatchIds(List<String> ids);
}
