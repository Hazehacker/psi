package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.UnitArrearsReportDTO;
import com.zeroone.star.project.query.j5.fundreport.UnitReceiptQuery;
import com.zeroone.star.reportmanagement.entity.CustomerDO;
import com.zeroone.star.reportmanagement.entity.SupplierDO;
import com.zeroone.star.reportmanagement.mapper.fundreport.UnitArrearsMapper;
import com.zeroone.star.reportmanagement.service.UnitArrearsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 往来单位欠款表实现类
 * @author 言语
 * @date 2025/10/27
 */
@Service
public class UnitArrearsReportServiceImpl extends ServiceImpl<UnitArrearsMapper,UnitArrearsReportDTO> implements UnitArrearsReportService {

    @Autowired
    private UnitArrearsMapper unitArrearsMapper;

    @Override
    public PageDTO<UnitArrearsReportDTO> query(UnitReceiptQuery query) {

        Page<UnitArrearsReportDTO> page = new Page<>(query.getPageIndex(),query.getPageSize());
        Page<UnitArrearsReportDTO> pageRes = baseMapper.listUnitArrearsReport(page,query);
        return PageDTO.create(pageRes);
    }
}
