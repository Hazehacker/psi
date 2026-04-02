package com.zeroone.star.reportmanagement.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.salesreport.SalesRankingTableDTO;
import com.zeroone.star.project.query.j5.salesreport.SalesRankingTableQury;
import com.zeroone.star.reportmanagement.mapper.salesreport.SalesRankingMapper;
import com.zeroone.star.reportmanagement.service.salesreport.SalesRankingTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SalesRankingTableServiceImpl implements SalesRankingTableService {

    @Resource
    private SalesRankingMapper salesRankingMapper;
    @Override
    public PageDTO<SalesRankingTableDTO> listSalesRankingTable(SalesRankingTableQury query) {

        if (query.getPageIndex() == 0 || query.getPageSize() == 0) {
            List<SalesRankingTableDTO> salesRankingList = salesRankingMapper.getSalesRankingListNoPage(query);
            Page<SalesRankingTableDTO> page = new Page<>(1, salesRankingList.size(), salesRankingList.size());
            page.setRecords(salesRankingList);
            return PageDTO.create(page);
        }

        // 正常分页查询
        Page<SalesRankingTableDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
       salesRankingMapper.getSalesRankingList(page, query);
       //salesRankingMapper.getSalesRankingListNoPage(query);
        return PageDTO.create(page);

    }
}
