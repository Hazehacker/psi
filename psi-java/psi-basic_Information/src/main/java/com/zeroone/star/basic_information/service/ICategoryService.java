package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Category;
import com.zeroone.star.project.vo.j6.basic_information.product_management.CategoryTreeVO;

import java.util.List;

/**
 * 分类服务接口
 * 
 * @author 杨潇
 * @since 2025-10-19
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 获取分类树
     * 
     * @return 分类树列表
     */
    List<CategoryTreeVO> getCategoryTree();
}
