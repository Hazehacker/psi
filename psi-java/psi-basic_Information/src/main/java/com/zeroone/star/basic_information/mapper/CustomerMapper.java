package com.zeroone.star.basic_information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.basic_information.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 * @author cmanPro
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
