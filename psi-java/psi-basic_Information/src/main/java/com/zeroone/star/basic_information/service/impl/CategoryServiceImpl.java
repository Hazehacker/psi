package com.zeroone.star.basic_information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.basic_information.entity.Category;
import com.zeroone.star.basic_information.mapper.CategoryMapper;
import com.zeroone.star.basic_information.service.ICategoryService;
import com.zeroone.star.project.vo.j6.basic_information.product_management.CategoryTreeVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 * 
 * @author 杨潇
 * @since 2025-10-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    MsCategoryMapper msCategoryMapper;

    @Override
    public List<CategoryTreeVO> getCategoryTree() {
        // 查询所有分类
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        List<Category> allCategories = baseMapper.selectList(queryWrapper);

        // 转换为VO列表
        List<CategoryTreeVO> categoryVOList = allCategories.stream()
                .map(msCategoryMapper::categoryToCategoryTreeVo)
                .collect(Collectors.toList());

        // 构建树形结构
        return buildTree(categoryVOList, null);
    }

    /**
     * 递归构建树形结构
     * 
     * @param allCategories 所有分类列表
     * @param parentId      父ID
     * @return 树形结构列表
     */
    private List<CategoryTreeVO> buildTree(List<CategoryTreeVO> allCategories, String parentId) {
        List<CategoryTreeVO> result = new ArrayList<>();

        for (CategoryTreeVO category : allCategories) {
            // 判断是否为顶级节点或指定父节点的子节点
            boolean isMatch = (parentId == null && StringUtils.isEmpty(category.getPid()))
                    || (parentId != null && parentId.equals(category.getPid()));

            if (isMatch) {
                // 递归查找子节点
                List<CategoryTreeVO> children = buildTree(allCategories, category.getId());
                category.setChildren(children.isEmpty() ? null : children);
                result.add(category);
            }
        }

        return result;
    }
}
