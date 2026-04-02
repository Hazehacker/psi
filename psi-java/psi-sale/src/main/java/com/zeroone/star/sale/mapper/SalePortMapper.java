package com.zeroone.star.sale.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.zeroone.star.project.dto.j4.sale.SaleDetailDTO;
import com.zeroone.star.sale.entity.Sale;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 销售单 Mapper 接口
 * </p>
 *
 * @author renjian
 * @since 2025-10-26
 */
@Mapper
public interface SalePortMapper extends BaseMapper<Sale> {


}
