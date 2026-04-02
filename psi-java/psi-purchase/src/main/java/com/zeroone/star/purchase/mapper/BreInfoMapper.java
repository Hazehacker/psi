package com.zeroone.star.purchase.mapper;

import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnGoodsDetailDTO;
import com.zeroone.star.purchase.DO.BreInfoDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 采购退货单详情 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
public interface BreInfoMapper extends BaseMapper<BreInfoDO> {

    /**
     * @author xiaoliu
     * 根据采购退货单id查询退货商品详情
     * @param pid 采购退货单id
     * @return 商品详情DTO列表
     */
    List<PurchaseReturnGoodsDetailDTO> selectPurchaseReturnGoodsDetail(@Param("pid") String pid);
}
