package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetailedAccountReceivableStatementMapper extends BaseMapper<DetailedAccountReceivableStatementDTO> {

    Page<DetailedAccountReceivableStatementDTO> selectByQuery(
            @Param("page") Page<DetailedAccountReceivableStatementDTO> page,
            @Param("query") DetailedAccountReceivableStatementQuery query
    );

    List<DetailedAccountReceivableStatementDTO> selectAllByQuery(@Param("query") DetailedAccountReceivableStatementQuery query);
}
