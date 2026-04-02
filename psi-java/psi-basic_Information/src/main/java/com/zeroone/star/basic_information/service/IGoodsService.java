package com.zeroone.star.basic_information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeroone.star.basic_information.entity.Goods;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsSelectDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsImportResultDTO;
import com.zeroone.star.project.query.j6.basic_information.product_management.GoodsQuery;
import com.zeroone.star.project.vo.JsonVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 商品服务接口
 * 
 * @author 杨潇, nick8311370879
 * @since 2025-10-19
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 分页查询商品列表
     * 
     * @param query 查询条件
     * @return 商品列表
     */
    PageDTO<GoodsDTO> listGoods(GoodsQuery query);

    /**
     * 根据ID查询商品详情
     * 
     * @param id 商品ID
     * @return 商品详情
     */
    GoodsDTO getGoodsById(String id);

    /**
     * 分页查询商品选择列表
     * 
     * @param query 查询条件
     * @return 商品选择列表
     */
    PageDTO<GoodsSelectDTO> listGoodsSelect(GoodsQuery query);

    /**
     * 导入商品数据
     * 
     * @param file Excel文件
     * @return 导入结果
     */
    JsonVO<GoodsImportResultDTO> importGoods(MultipartFile file) throws IOException;

    /**
     * 导出商品数据
     * 
     * @param ids 商品ID列表（如果为空则导出所有商品）
     * @return Excel文件字节数组
     */
    byte[] exportGoods(List<String> ids);
}
