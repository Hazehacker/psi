package com.zeroone.star.fund.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zeroone.star.fund.entity.Imy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j4.fund.FundReceiptListDTO;
import com.zeroone.star.project.query.j4.fund.FundReceiptListQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 收款单 Mapper 接口
 * </p>
 *
 * @author icefast
 * @since 2025-10-24
 */
@Mapper
public interface ImyMapper extends BaseMapper<Imy> {

    IPage<FundReceiptListDTO> listAll(IPage<FundReceiptListDTO> page, @Param("query") FundReceiptListQuery query);
}
