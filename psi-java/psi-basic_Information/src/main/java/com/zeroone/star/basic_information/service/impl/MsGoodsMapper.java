package com.zeroone.star.basic_information.service.impl;

import com.zeroone.star.basic_information.entity.Goods;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsAddDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsSelectDTO;
import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsExcelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 描述：商品对应MapStruct映射接口
 * 
 * @author 杨潇, nick8311370879
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface MsGoodsMapper {
    
    /**
     * 实体转DTO
     * 
     * @param entity 商品实体
     * @return 商品DTO
     */
    GoodsDTO goodsToGoodsDto(Goods entity);

    /**
     * AddDTO转实体
     * 
     * @param dto 新增DTO
     * @return 商品实体
     */
    Goods addDtoToGoods(GoodsAddDTO dto);

    /**
     * DTO转实体
     * 
     * @param dto 商品DTO
     * @return 商品实体
     */
    Goods goodsDtoToGoods(GoodsDTO dto);

    /**
     * 实体转选择DTO
     * 
     * @param entity 商品实体
     * @return 商品选择DTO
     */
    GoodsSelectDTO goodsToGoodsSelectDto(Goods entity);

    /**
     * Excel DTO转实体（用于导入）
     * 
     * @param goodsExcelDTO Excel数据传输对象
     * @return 商品实体
     */
    @Mapping(target = "id", ignore = true) // ID由系统生成，忽略映射
    @Mapping(target = "py", ignore = true) // 拼音信息需要单独处理
    @Mapping(target = "imgs", ignore = true) // 商品图像不在Excel中
    @Mapping(target = "details", ignore = true) // 图文详情不在Excel中
    @Mapping(target = "units", ignore = true) // 多单位配置需要单独处理
    @Mapping(target = "strategy", ignore = true) // 折扣策略需要单独处理
    @Mapping(target = "serial", constant = "0") // 序列产品默认关闭
    @Mapping(target = "batch", constant = "0") // 批次产品默认关闭
    @Mapping(target = "validity", constant = "0") // 有效期默认关闭
    @Mapping(target = "protect", ignore = true) // 保质期不在Excel中
    @Mapping(target = "threshold", ignore = true) // 预警阀值不在Excel中
    @Mapping(target = "more", constant = "{}") // 扩展信息不在Excel中
    Goods toEntity(GoodsExcelDTO goodsExcelDTO);

    /**
     * 实体转Excel DTO（用于导出）
     * 
     * @param goods 商品实体
     * @return Excel DTO
     */
    @Mapping(target = "retailPrice", source = "sell") // 零售价格使用销售价
    @Mapping(target = "exchangePoints", ignore = true) // 兑换积分暂时忽略
    GoodsExcelDTO goodsToExcelDto(Goods goods);

    /**
     * 实体列表转Excel DTO列表
     * 
     * @param goodsList 商品实体列表
     * @return Excel DTO列表
     */
    List<GoodsExcelDTO> goodsListToExcelDtoList(List<Goods> goodsList);
}
