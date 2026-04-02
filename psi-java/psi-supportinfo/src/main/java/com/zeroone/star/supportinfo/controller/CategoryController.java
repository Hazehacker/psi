package com.zeroone.star.supportinfo.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryAddDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory.CategoryTreeNodeDTO;
import com.zeroone.star.project.j7.sysargs.supportinfo.goodscategory.GoodsCateApis;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryAddQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.goodscategory.CategoryUpdateQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.supportinfo.exception.ResourceNotFoundException;
import com.zeroone.star.supportinfo.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 类名：CategoryController
 * 包名：com.zeroone.star
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/category")
@Api(tags="商品类别")
public class CategoryController implements GoodsCateApis {



    @Resource
    ICategoryService categoryService;

    @GetMapping("/queryTree")
    @ApiOperation(value = "获取商品类别名称树")
    @ApiImplicitParam(name = "id",value = "查询商品树的根节点ID",required = true,example = "0")
    @Override
    public JsonVO<CategoryTreeNodeDTO> queryNameTree(@RequestParam("id") String id) {
        CategoryTreeNodeDTO root= null;
        try {
            root = categoryService.getNameTree(id);
        } catch (ResourceNotFoundException e) {
            return JsonVO.fail(null);
        }
        return JsonVO.success(root);
    }

    @GetMapping("/queryById")
    @ApiOperation(value = "获取指定类别详情")
    @ApiImplicitParam(name = "id",value = "查询单个商品id",required = true,example = "1")
    @Override
    public JsonVO<CategoryDTO> queryById(@RequestParam("id") String id){
        CategoryDTO res=null;
        try {
            res=categoryService.getById(id);

        } catch (ResourceNotFoundException e) {
            return JsonVO.fail(null);
        }
        return JsonVO.success(res);
    }


    //根据名称关键字模糊查询商品类型，如果不输入名称则为返回全部商品类别
    @GetMapping("/queryAll")
    @ApiOperation(value = "获取商品类别列表（条件）")
    @Override
    public JsonVO<PageDTO<CategoryDTO>> queryAll(@Validated CategoryQuery condition) {
        return JsonVO.success(categoryService.listAll(condition));
    }



    @PostMapping("/add")
    @ApiOperation("新增商品类别")
    @Override
    public JsonVO<String> addCategory(@Validated @RequestBody CategoryAddDTO addCategoryDTO) {

        //把dto转换为query
        CategoryAddQuery categoryAddQuery=new CategoryAddQuery();
        String id=categoryService.insertCategory(addCategoryDTO);
        if(id!=null){
            return JsonVO.success(id);
        }else{
            return JsonVO.fail("新增商品失败");
        }
    }
    @PutMapping("/update")
    @ApiOperation("修改指定类别")
    @Override
    public JsonVO<String> updateCategory(@Validated @RequestBody CategoryDTO categoryDTO) {
        CategoryUpdateQuery query=new CategoryUpdateQuery();
        BeanUtils.copyProperties(categoryDTO,query);
        if(categoryService.updateCategory(categoryDTO)){
            return JsonVO.success(categoryDTO.getId());
        }else{
            return JsonVO.fail("更新商品数据失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除指定类别")
    @Override
    public JsonVO<String> deleteCategory(
            @ApiParam(value = "要删除的商品类别的ID", required = true, example = "1001") @RequestParam("id") String id) {

        try {
            if (categoryService.deleteCategory(id)) {
                return JsonVO.success(id);
            } else {
                return JsonVO.fail("试图删除此id时失败"+id);
            }
        } catch (NumberFormatException e) {
            return JsonVO.fail("id格式错误，必须为数字");
        }

    }
}
