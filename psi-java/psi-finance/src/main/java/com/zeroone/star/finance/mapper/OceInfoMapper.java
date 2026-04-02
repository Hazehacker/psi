package com.zeroone.star.finance.mapper;

import com.zeroone.star.finance.entity.OceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 其它支出单详情 Mapper 接口
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Mapper
public interface OceInfoMapper extends BaseMapper<OceInfo> {

    /**
     * 删除其他支出单
     * @param pids 其他支出单id集合
     * @return Integer
     */
    public Integer deleteByPids(List<String> pids);

    /**
     * 插入对应的数据
     * @param oceInfos
     * @return Integer 成功插入的条数
     */
    public Integer insertBatchOceBill(List<OceInfo> oceInfos);
}
