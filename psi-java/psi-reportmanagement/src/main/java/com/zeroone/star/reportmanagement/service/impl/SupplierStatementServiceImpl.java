package com.zeroone.star.reportmanagement.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j5.fundreport.SupplierStatementMainDTO;
import com.zeroone.star.project.query.j5.fundreport.SupplierStatementQuery;
import com.zeroone.star.reportmanagement.mapper.fundreport.SupplierStatementMapper;
import com.zeroone.star.reportmanagement.service.SupplierStatementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供应商对账单逻辑层
 *
 * @author toexpl
 * @since 2025/10/29
 */

@Service
public class SupplierStatementServiceImpl implements SupplierStatementService {

    @Resource
    SupplierStatementMapper mapper;

    /**
     * 供应商对账单查询
     * @param query
     * @return
     */
    @Override
    public Page<SupplierStatementMainDTO> listSupplierStatement(SupplierStatementQuery query) {

        Page<SupplierStatementMainDTO> page = new Page<>(query.getPageIndex(),query.getPageSize());

        List<SupplierStatementMainDTO> list = mapper.selectSupplierStatementMain(query);

        if(query.isHideOrShowDetail()){
            for(SupplierStatementMainDTO dto:list){
                dto.setExpanded(false);
            }
        }else {
            for(SupplierStatementMainDTO dto:list){
                dto.setExpanded(true);
            }
        }

        page.setRecords(list);

        return page;

    }

    /**
     * 供应商对账单导出
     * @param query
     * @return
     */
    @Override
    public List<SupplierStatementMainDTO> exportSupplierStatement(SupplierStatementQuery query) {
        return mapper.selectSupplierStatementMain(query);
    }

}
