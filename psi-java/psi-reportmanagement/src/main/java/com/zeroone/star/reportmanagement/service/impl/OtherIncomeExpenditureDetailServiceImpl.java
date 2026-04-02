package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.fundreport.OtherIncomeExpenditureDetailDTO;
import com.zeroone.star.project.query.j5.fundreport.OtherIncomeExpenditureQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.OtherIncomeExpenditureDetailMapper;
import com.zeroone.star.reportmanagement.service.OtherIncomeExpenditureDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 其他收支明细表服务实现类
 * @author 天天困
 * @date 2025/10/28
 */
@Service
@Transactional
public class OtherIncomeExpenditureDetailServiceImpl implements OtherIncomeExpenditureDetailService {

    @Resource
    private OtherIncomeExpenditureDetailMapper mapper;

    @Override
    public PageDTO<OtherIncomeExpenditureDetailDTO> listOtherIncomeExpenditureDetail(OtherIncomeExpenditureQuery query) {
        // 参数校验
        validateQueryParams(query);
        Page<OtherIncomeExpenditureDetailDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        mapper.selectOtherIncomeExpenditureDetail(page, query);
        return PageDTO.create(page);
    }

    @Override
    public List<OtherIncomeExpenditureDetailDTO> listAllForExport(OtherIncomeExpenditureQuery query) {
        // 参数校验
        validateQueryParams(query);
        return mapper.selectAllForExport(query);
    }

    /**
     * 校验查询参数
     * @param query 查询参数
     */
    private void validateQueryParams(OtherIncomeExpenditureQuery query) {
        if (query.getStartTime() != null && query.getEndTime() != null &&
            query.getStartTime().compareTo(query.getEndTime()) > 0) {
            throw new IllegalArgumentException("开始时间不能大于结束时间");
        }
    }
}
