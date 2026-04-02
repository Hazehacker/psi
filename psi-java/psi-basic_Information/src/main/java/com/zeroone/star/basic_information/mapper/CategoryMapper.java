package com.zeroone.star.basic_information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.basic_information.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper接口
 * 
 * @author 杨潇
 * @since 2025-10-20
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
