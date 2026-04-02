package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesProfitDetailedDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesQuery;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesProfitMapper;
import com.zeroone.star.reportmanagement.service.SalesProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 销售利润表实现类
 * @author 言语
 * @date 2025/10/27
 */
@Service
public class SalesProfitServiceImpl extends ServiceImpl<SalesProfitMapper,Object> implements SalesProfitService {

    @Autowired
    private SalesProfitMapper salesProfitMapper;

    @Override
    public PageDTO<Object> query(SalesQuery query) {
        Page<Object> page = new Page<>(query.getPageIndex(),query.getPageSize());
        if (query.getData().equals("隐藏明细") ) {
            Page<SalesProfitDTO> pageRes = baseMapper.selectSalesProfit(page,query);
            return PageDTO.create((Page<Object>) (Page<?>) pageRes);
        }else if(query.getData().equals("显示明细")){
            Page<SalesProfitDetailedDTO> pageDetRes = baseMapper.listSalesDetailedProfit(page,query);
            return PageDTO.create((Page<Object>) (Page<?>) pageDetRes);
        }
        return new PageDTO<>();
    }
}
