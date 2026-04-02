package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountReceivableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountReceivableStatementQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.DetailedAccountReceivableStatementMapper;
import com.zeroone.star.reportmanagement.service.IDetailedAccountReceivableStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DetailedAccountReceivableStatementServiceImpl extends ServiceImpl<DetailedAccountReceivableStatementMapper, DetailedAccountReceivableStatementDTO> implements IDetailedAccountReceivableStatementService {

    @Resource
    DetailedAccountReceivableStatementMapper detailedAccountReceivableStatementMapper;

    @Override
    public PageDTO<DetailedAccountReceivableStatementDTO> listDetailedAccountReceivableStatement(DetailedAccountReceivableStatementQuery query) {
        // 参数校验
        if (query == null) {
            throw new IllegalArgumentException("查询参数不能为空");
        }
        log.info("查询现金银行报表，参数：{}", query);
        // 构建分页查询对象
        Page<DetailedAccountReceivableStatementDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        // 分页查询
        Page<DetailedAccountReceivableStatementDTO> resultPage  = detailedAccountReceivableStatementMapper.selectByQuery(page,query);
        log.info("分页信息 - 当前页: {}, 每页大小: {}, 总记录数: {}",
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal());
        log.info("查询结果: {}", resultPage.getRecords());
        return PageDTO.create(resultPage);
    }

    @Override
    public List<DetailedAccountReceivableStatementDTO> listAllDetailedAccountReceivableStatement(DetailedAccountReceivableStatementQuery query) {
        return detailedAccountReceivableStatementMapper.selectAllByQuery(query);
    }
}
