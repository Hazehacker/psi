package com.zeroone.star.project.j6.basic_information.product_management;

import com.zeroone.star.project.dto.j6.basic_information.product_management.GoodsDTO;
import com.zeroone.star.project.vo.JsonVO;

import java.util.List;

/**
 * @author chupeiqing
 * @version 1.0
 * @date: 2025/10/21
 */

public interface GoodsAPI {
    /**
     * 更新商品信息
     * @param goodsDTO 商品信息
     * @return 返回结果
     */
    JsonVO updateGoods(GoodsDTO goodsDTO);

    /**
     * 删除商品
     * @param ids 商品id数组
     * @return
     */
    JsonVO deleteGoods(List<String> ids);
}
