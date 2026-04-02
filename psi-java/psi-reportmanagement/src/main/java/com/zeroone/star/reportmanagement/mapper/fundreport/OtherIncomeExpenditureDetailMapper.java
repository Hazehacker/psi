package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 其他收支明细表Mapper接口
 * @author 天天困
 * @date 2025/10/28
 */
public interface OtherIncomeExpenditureDetailMapper {

    /**
     * 分页查询其他收支明细表
     * @param page MyBatis-Plus 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    Page<OtherIncomeExpenditureDetailDTO> selectOtherIncomeExpenditureDetail(
            Page<OtherIncomeExpenditureDetailDTO> page,
            @Param("query") OtherIncomeExpenditureQuery query
    );

    /**
     * 用于导出的方法，返回 List
     * @param query 查询条件
     * @return 其他收支明细列表
     */
    List<OtherIncomeExpenditureDetailDTO> selectAllForExport(
            @Param("query") OtherIncomeExpenditureQuery query
    );
}
