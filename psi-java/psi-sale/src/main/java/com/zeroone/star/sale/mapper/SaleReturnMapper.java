package com.zeroone.star.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j4.sale.SaleReturnDTO;
import com.zeroone.star.project.dto.j4.sale.SaleReturnInfoDTO;
import com.zeroone.star.project.query.j4.sale.SaleReturnQuery;
import com.zeroone.star.sale.entity.SaleReturn;
import com.zeroone.star.sale.entity.SaleReturnInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SaleReturnMapper {

    Page<SaleReturnDTO> selectSaleReturnPage(Page<SaleReturnDTO> page, @Param("query") SaleReturnQuery query);

    @Select("select * from sre_info where id = #{id}")
    List<SaleReturnInfoDTO> selectSaleRReturnInfo(String id);


    void insertBatch(@Param("list") List<SaleReturnInfo> tempList);

    List<SaleReturnInfo> selectSaleReturnByPage(int start, int batchSize);
}
