package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应付账款明细表Mapper接口
 * @author 天天困
 * @date 2025/10/28
 */
public interface DetailedAccountPayableStatementMapper {

    /**
     * 分页查询应付账款明细表
     * @param page MyBatis-Plus 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    Page<DetailedAccountPayableStatementDTO> selectDetailedAccountPayableStatement(
            Page<DetailedAccountPayableStatementDTO> page,
            @Param("query") DetailedAccountPayableStatementQuery query
    );

    /**
     * 用于导出的方法，返回 List
     * @param query 查询条件
     * @return 应付账款明细列表
     */
    List<DetailedAccountPayableStatementDTO> selectAllForExport(
            @Param("query") DetailedAccountPayableStatementQuery query
    );
}
