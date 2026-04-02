package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesDetailFormDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesDetailFormQuery;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesDetailFormMapper;
import com.zeroone.star.reportmanagement.service.SalesDetailFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 销售明细表Service接口实现
 * @author rainsilent
 * @date 2025/10/28
 */
@Service
@Transactional
public class SalesDetailFormServiceImpl implements SalesDetailFormService {
    @Resource
    SalesDetailFormMapper salesDetailFormMapper;
    @Override
    public PageDTO<SalesDetailFormDTO> listSalesDetailForm(SalesDetailFormQuery query) {
        Page<SalesDetailFormDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        salesDetailFormMapper.selectSalesDetailForm(page,query);
        return PageDTO.create(page);
    }

    @Override
    public List<SalesDetailFormDTO> listAllForExcel(SalesDetailFormQuery query){
        return salesDetailFormMapper.selectAllForExcel(query);
    }
}
