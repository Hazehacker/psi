package com.zeroone.star.supportinfo.service;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryAddDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryAddQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryUpdateQuery;
import com.zeroone.star.supportinfo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品类别 服务类
 * </p>
 *
 * @author null
 * @since 2025-10-23
 */
public interface ICategoryService extends IService<Category> {
    CategoryTreeNodeDTO getNameTree(String id);
    CategoryDTO getById(String id);
    PageDTO<CategoryDTO> listAll(CategoryQuery query);

    boolean updateCategory(CategoryDTO categoryDTO);
    String insertCategory(CategoryAddDTO categoryAddDTO);

    boolean deleteCategory(String id);
}
