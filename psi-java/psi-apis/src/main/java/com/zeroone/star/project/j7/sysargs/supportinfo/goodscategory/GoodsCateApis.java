package com.zeroone.star.project.j7.sysargs.supportinfo.goodscategory;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryQuery;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryAddDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.vo.JsonVO;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryQuery;
import com.zeroone.star.project.vo.JsonVO;

/**
 * 类名：GoodCateApis
 * 包名：com.zeroone.star.project.j7.sysargs.supportinfo.goodcategory
 * 描述：商品类别接口
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
public interface GoodsCateApis {
    /**
     * 获取商品名称树
     * @Param treeRootId
     * @return 树结构的json格式数据
     */
    JsonVO<CategoryTreeNodeDTO> queryNameTree(String treeRootId);

    /**
     * 根据id查询
     * @Param id商品id
     * @return 查询结果（单条）
     */
    JsonVO<CategoryDTO> queryById(String id);

    /**
     * 条件查询（按名称模糊查询）
     * @Param 查询条件（商品名称）
     * @return 查询结果的分页数据
     */
    JsonVO<PageDTO<CategoryDTO>> queryAll(CategoryQuery condition);

    /**
     * 新增商品类别
     * @Param categoryAddDTO 新增的商品类别对象
     * @return 添加结果
     */
    JsonVO<String> addCategory(CategoryAddDTO addCategoryDTO);

    /**
     * 修改商品类别
     * @Param categoryDTO 修改的商品类别对象
     * @return 修改结果
     */
    JsonVO<String> updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除商品类别
     * @Param id 删除的商品类别id
     * @return 修改结果
     */
    JsonVO<String> deleteCategory(String id);
}
