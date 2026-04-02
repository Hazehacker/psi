package com.zeroone.star.reportmanagement.mapper.procurementreport;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.ProcurementDetailFromDTO;
import com.zeroone.star.project.query.j5.procurementreport.ProcurementDetailFormQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购明细表 Mapper 接口
 * @author chuming_7
 * @since 2025-10-25
 */
@Mapper
public interface ProcurementDetailFormMapper {
    /**
     * 查询采购明细表
     * @param page 分页参数
     * @param query 查询参数
     * @return 采购明细表数据
     */
    Page<ProcurementDetailFromDTO> selectProcurementDetailForm(
            Page<ProcurementDetailFromDTO> page,
            @Param("query") ProcurementDetailFormQuery query
    );

    /**
     * 用于导出的方法，返回 List
     * @param query 查询条件
     * @return
     */
    List<ProcurementDetailFromDTO> selectAllForExport(@Param("query")ProcurementDetailFormQuery query);
}
