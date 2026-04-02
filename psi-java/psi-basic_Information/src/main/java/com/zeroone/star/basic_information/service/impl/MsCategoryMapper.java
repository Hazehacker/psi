package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Category;
import com.zeroone.star.project.vo.j6.basic_information.product_management.CategoryTreeVO;
import org.mapstruct.Mapper;

/**
 * 描述：分类对应MapStruct映射接口
 * @author 杨潇
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface MsCategoryMapper {
    
    /**
     * 实体转VO
     * @param entity 分类实体
     * @return 分类树VO
     */
    CategoryTreeVO categoryToCategoryTreeVo(Category entity);
}

