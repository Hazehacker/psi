package com.zeroone.star.reportmanagement.mapper.fundreport;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UnitArrearsMapper extends BaseMapper<UnitArrearsReportDTO> {

    Page<UnitArrearsReportDTO> listUnitArrearsReport(@Param("page") Page<UnitArrearsReportDTO> page,
                                               @Param("query")UnitReceiptQuery query);
}
