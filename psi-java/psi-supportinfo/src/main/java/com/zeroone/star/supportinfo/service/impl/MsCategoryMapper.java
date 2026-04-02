package com.zeroone.star.supportinfo.service.impl;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.supportinfo.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Named;


@Mapper(componentModel="spring")
public interface MsCategoryMapper {


    @Named("intToString")
    String intToString(Integer id);

    @Named("StringToInt")
    String StringToInt(String str);

    /**
     * 映射DTO
     * @param entity 实体
     * @return DTO
     */
    CategoryDTO categoryToCategoryDto(Category entity);

    /**
     * 映射实体
     * @param dto SampleDTO
     * @return 实体
     */
    Category categoryDtoToCategory(CategoryDTO dto);

    /**
     * 映射实体
     * @param entity 实体
     * @return DTO
     */
    CategoryTreeNodeDTO categoryToCategoryTreeNodeDTO(Category entity);


}
