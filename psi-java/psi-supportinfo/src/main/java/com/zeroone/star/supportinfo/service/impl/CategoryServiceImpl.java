package com.zeroone.star.supportinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryAddDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryQuery;
import com.zeroone.star.supportinfo.exception.ResourceNotFoundException;
import com.zeroone.star.supportinfo.mapper.CategoryMapper;

import com.zeroone.star.supportinfo.entity.Category;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryAddQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryUpdateQuery;


import com.zeroone.star.supportinfo.service.ICategoryService;
import org.springframework.beans.BeanUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    MsCategoryMapper ms;

    @Override
    public CategoryDTO getById(String id) {
        Category category=baseMapper.selectById(id);
        if(category==null){
            throw new ResourceNotFoundException("查询失败：ID为"+ id +"的类别不存在");
        }
        return ms.categoryToCategoryDto(category);
    }

    @Override
    public PageDTO<CategoryDTO> listAll(CategoryQuery query) {
        // 构建分页查询对象
        Page<Category> page = new Page<>(query.getPageIndex(), query.getPageSize());
        QueryWrapper<Category> queryWrapper=new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(query.getName()), "name", query.getName());
        queryWrapper.orderBy(true,true,"sort");
        // 分页查询s
        Page<Category> pageRes = baseMapper.selectPage(page, queryWrapper);
        return PageDTO.create(pageRes,ms::categoryToCategoryDto);
    }

    @Override
    public CategoryTreeNodeDTO getNameTree(String id) {
        Category root=baseMapper.selectById(id);
        if(root==null){
            throw new ResourceNotFoundException("查询树失败：ID为" + id + "的根节点不存在");
        }
        CategoryTreeNodeDTO result=ms.categoryToCategoryTreeNodeDTO(root);
        buildChildren(result,id);
        return result;
    }


    //构建子树
    private void buildChildren(CategoryTreeNodeDTO parent,String parentId) {
        if(parent==null){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("pid", parentId);  // 查找该节点的所有子节点
        List<Category> children = baseMapper.selectByMap(params);

        for (Category child : children) {
            CategoryTreeNodeDTO childDTO=ms.categoryToCategoryTreeNodeDTO(child);
            parent.addChild(childDTO); // 将子节点添加到父节点
            buildChildren(childDTO,child.getId());  // 递归构建子节点的子树
        }
    }

    @Override
    public boolean updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        boolean success = this.updateById(category);
        if (!success && category.getId() != null) {
            throw new ResourceNotFoundException("修改失败：ID为" + category.getId() + "的类别不存在");
        }
        return success;
    }

    @Override
    public String insertCategory(CategoryAddDTO categoryAddDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryAddDTO, category);

        boolean success = this.save(category);

        // 3. 成功则返回生成的 id，失败返回 null
        return success ? category.getId() : null;
    }

    @Override
    public boolean deleteCategory(String id) {
        Category category = super.getById(id);
        if (category == null && id != null) {
            throw new ResourceNotFoundException("查询失败：ID为" + id + "的类别不存在");
        }
        return this.removeById(id);
    }
}
