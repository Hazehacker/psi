package com.zeroone.star.project.j6.basic_information.product_management;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.AttributeDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodExportDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsImportResultDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsSelectDTO;
import com.zeroone.star.project.query.j6.basic_information.product_management.AttributeQuery;
import com.zeroone.star.project.query.j6.basic_information.product_management.GoodsQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j6.basic_information.product_management.CategoryTreeVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 描述：商品管理API接口定义（统一接口）
 * 
 * @author 杨潇, nick8311370879
 * @version 1.0.0
 */
public interface GoodsApis {

    // ============ 商品查询相关接口 ============

    /**
     * 获取商品列表（条件+分页）
     * 
     * @param query 查询条件
     * @return 商品列表
     */
    JsonVO<PageDTO<GoodsDTO>> queryGoodsList(GoodsQuery query);

    /**
     * 获取指定商品详情
     * 
     * @param id 商品ID
     * @return 商品详情
     */
    JsonVO<GoodsDTO> queryGoodsDetail(String id);

    /**
     * 获取商品选择列表（条件+分页）
     * 
     * @param query 查询条件
     * @return 商品选择列表
     */
    JsonVO<PageDTO<GoodsSelectDTO>> queryGoodsSelectList(GoodsQuery query);

    // ============ 商品操作相关接口 ============

    /**
     * 新增商品
     * 
     * @param goods 商品信息
     * @return 新增结果（返回商品ID）
     */
    JsonVO<String> addGoods(GoodsAddDTO goods);

    /**
     * 修改商品信息
     * 
     * @param goods 商品信息（包含ID）
     * @return 修改结果
     */
    JsonVO<String> updateGoods(GoodsDTO goods);

    /**
     * 删除商品（支持批量）
     * 
     * @param ids 商品ID列表
     * @return 删除结果
     */
    JsonVO<String> removeGoods(List<String> ids);

    // ============ 分类和属性相关接口 ============

    /**
     * 获取商品类别名称树
     * 
     * @return 分类树
     */
    JsonVO<List<CategoryTreeVO>> queryCategoryTree();

    /**
     * 获取属性名与属性内容列表（分页）
     * 
     * @param query 查询条件
     * @return 属性列表
     */
    JsonVO<PageDTO<AttributeDTO>> queryAttributeList(AttributeQuery query);

    // ============ 导入导出相关接口 ============

    /**
     * 导入商品数据
     * 
     * @param file 商品导入Excel文件
     * @return 导入结果
     */
    JsonVO<GoodsImportResultDTO> importGoods(MultipartFile file) throws IOException;

    /**
     * 导出商品数据
     * 
     * @param goodsExport 选择导出的数据
     * @return 返回Excel文件
     */
    ResponseEntity<byte[]> exportGoods(GoodExportDTO goodsExport);

    /**
     * 下载导入模板
     * 
     * @return 返回Excel模板文件
     */
    ResponseEntity<byte[]> downloadTemplate();
}
