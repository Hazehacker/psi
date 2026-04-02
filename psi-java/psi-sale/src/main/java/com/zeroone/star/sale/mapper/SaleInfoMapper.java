package com.zeroone.star.sale.mapper;

import com.zeroone.star.sale.entity.SaleInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaleInfoMapper{
    @Delete("delete from sell_info where pid = #{saleId}")
    void deleteBatchByPid(String saleId);

    void insertBatch(List<SaleInfoDO> details);
}
