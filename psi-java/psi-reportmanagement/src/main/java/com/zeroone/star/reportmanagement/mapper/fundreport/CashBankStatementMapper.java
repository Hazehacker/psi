package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j5.fundreport.CashBankStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.CashBankStatementQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CashBankStatementMapper extends BaseMapper<CashBankStatementDTO> {

    Page<CashBankStatementDTO> selectByQuery(
            @Param("page") Page<CashBankStatementDTO> page,
            @Param("query") CashBankStatementQuery query
    );

    List<CashBankStatementDTO> selectAllByQuery(@Param("query") CashBankStatementQuery query);
}
