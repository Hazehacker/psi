package com.zeroone.star.purchase.service;

import com.zeroone.star.purchase.DO.GoodsDO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
public interface IGoodsService extends IService<GoodsDO> {

    /**
     * @author xiaoliu
     * 根据商品名称、编号、规格型号查询商品id
     * @param goods
     * @param number
     * @param spec
     * @return
     */
    String findGoodsId(@NotBlank(message = "商品名称不能为空") @Size(min = 2, max = 100, message = "商品名称长度应在2-100字符之间") String goods, @NotBlank(message = "商品编号不能为空") @Size(min = 2, max = 50, message = "商品编号长度应在2-50字符之间") @Pattern(regexp = "^[A-Za-z0-9]+$", message = "商品编号只能包含字母和数字") String number, @Size(max = 50, message = "规格型号长度不能超过50字符") String spec);
}
