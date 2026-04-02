package com.zeroone.star.capital.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.capital.DO.BuyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 采购单 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
@Component("capitalBuyMapper")
public interface BuyMapper extends BaseMapper<BuyDO> {

}
