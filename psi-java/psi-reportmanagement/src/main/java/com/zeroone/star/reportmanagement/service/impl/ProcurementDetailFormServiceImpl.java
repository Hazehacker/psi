package com.zeroone.star.reportmanagement.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j5.procurementreport.ProcurementDetailFromDTO;
import com.zeroone.star.project.query.j5.procurementreport.ProcurementDetailFormQuery;
import com.zeroone.star.reportmanagement.mapper.procurementreport.ProcurementDetailFormMapper;
import com.zeroone.star.reportmanagement.service.ProcurementDetailFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 采购明细表 服务实现类
 * @author chuming_7
 * @since 2025-10-27
 */
@Service
@Transactional
public class ProcurementDetailFormServiceImpl implements ProcurementDetailFormService {

    @Resource
    ProcurementDetailFormMapper mapper;
    @Override
    public PageDTO<ProcurementDetailFromDTO> listProcurementDetailForm(ProcurementDetailFormQuery query) {
        Page<ProcurementDetailFromDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        mapper.selectProcurementDetailForm(page,query);
        return PageDTO.create(page);
    }

    @Override
    public List<ProcurementDetailFromDTO> listAllForExport(ProcurementDetailFormQuery query) {
        return mapper.selectAllForExport(query);
    }

}
