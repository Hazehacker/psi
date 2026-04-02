package com.zeroone.star.finance.mapper;

import com.zeroone.star.finance.entity.OceBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 其它支出单核销详情 Mapper 接口
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Mapper
public interface OceBillMapper extends BaseMapper<OceBill> {


    /**
     * 删除其他支出单的oce的子表oce_bill
     * @param pids 其他支出单id集合
     * @return Integer
     */
    public Integer deleteByPids(List<String> pids);


    /**
     * 插入对应的数据
     * @param oceBill
     * @return Integer 成功插入的条数
     */
    public Integer insertBatchtOceBill(List<OceBill> oceBill);
}
