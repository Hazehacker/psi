package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.DetailedAccountPayableStatementDTO;
import com.zeroone.star.project.query.j5.fundreport.DetailedAccountPayableStatementQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.DetailedAccountPayableStatementMapper;
import com.zeroone.star.reportmanagement.service.DetailedAccountPayableStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应付账款明细表服务实现类
 * @author 天天困
 * @date 2025/10/28
 */
@Service
@Transactional
public class DetailedAccountPayableStatementServiceImpl implements DetailedAccountPayableStatementService {

    @Resource
    private DetailedAccountPayableStatementMapper mapper;

    @Override
    public PageDTO<DetailedAccountPayableStatementDTO> listDetailedAccountPayableStatement(DetailedAccountPayableStatementQuery query) {
        // 参数校验
        validateQueryParams(query);
        Page<DetailedAccountPayableStatementDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        mapper.selectDetailedAccountPayableStatement(page, query);
        return PageDTO.create(page);
    }

    @Override
    public List<DetailedAccountPayableStatementDTO> listAllForExport(DetailedAccountPayableStatementQuery query) {
        // 参数校验
        validateQueryParams(query);
        return mapper.selectAllForExport(query);
    }

    /**
     * 校验查询参数
     * @param query 查询参数
     */
    private void validateQueryParams(DetailedAccountPayableStatementQuery query) {
        if (query.getStartTime() != null && query.getEndTime() != null &&
                query.getStartTime().compareTo(query.getEndTime()) > 0) {
            throw new IllegalArgumentException("开始时间不能大于结束时间");
        }
    }
}
