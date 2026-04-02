package com.zeroone.star.capital.mapper;

import com.zeroone.star.capital.DO.SerialDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 序列号 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
@Component("capitalSerialMapper")
public interface SerialMapper extends BaseMapper<SerialDO> {

}
