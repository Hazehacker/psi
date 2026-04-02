package com.zeroone.star.payment_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.payment_order.entity.OmyInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 付款单详情Mapper接口
 */
@Mapper
public interface OmyInfoMapper2 extends BaseMapper<OmyInfoDO> {
    // 根据pid查询详情列表
    List<OmyInfoDO> selectByPid(String pid);
}