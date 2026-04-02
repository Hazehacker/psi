package com.zeroone.star.finance.mapper;

import com.zeroone.star.finance.entity.Oce;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 其它支出单 Mapper 接口
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Mapper
public interface OceMapper extends BaseMapper<Oce> {
    /**
     * 删除其他的支出单(支持批量)
     *
     * @param ids 删除支出单的id列表
     * @return Integer
     */
    Integer deleteByIds(List<String> ids);


    /**
     * 插入对应的数据
     * @param oce
     * @return Integer 成功插入的条数
     */
    public Integer insertBatchOce(List<Oce> oce);


    /**
     * 检查oce的状态
     * @param id
     * @return
     */
    Integer selectExamineStatusById(String id);
}
