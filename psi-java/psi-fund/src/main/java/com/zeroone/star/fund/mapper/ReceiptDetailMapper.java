package com.zeroone.star.fund.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.fund.entity.ReceiptDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 收款单详情 Mapper 接口
 * </p>
 *
 * @author 阿伟学长
 * @since 2025-10-24
 */
@Mapper
public interface ReceiptDetailMapper extends BaseMapper<ReceiptDetail> {
    /**
     * 根据主表ID删除详情记录
     * @param pid 主表ID
     * @return 删除的记录数
     */
    int deleteByPid(@Param("pid") String pid);
}
