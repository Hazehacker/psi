package com.zeroone.star.capital.mapper;

import com.zeroone.star.capital.DO.BillInfoDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 核销单详情 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
public interface BillInfoMapper extends BaseMapper<BillInfoDO> {

    /**
     * 批量插入核销单详情
     * @param billInfoDOs 核销单详情
     * @author: a
     * @date: 2025/10/30
     */
    void insertBatch(List<BillInfoDO> billInfoDOs);

    /**
     * 批量更新核销单详情
     * @param billInfoDOs 核销单详情
     * @author: a
     * @date: 2025/10/30
     */
    void updateBatch(List<BillInfoDO> billInfoDOs);
}
