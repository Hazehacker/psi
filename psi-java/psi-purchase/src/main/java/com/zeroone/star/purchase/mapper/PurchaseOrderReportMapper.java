package com.zeroone.star.purchase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j3.purchase.PurchaseOrderExcelInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采购订单报表 Mapper 接口
 * @author: 加减法
 */
@Mapper
public interface PurchaseOrderReportMapper {

    /**
     * 查询采购订单详细报表数据
     * @param ids 采购订单ID列表
     * @return 报表数据列表
     */
    List<PurchaseOrderExcelInfoDTO> selectPurchaseOrderReportDataByIds(@Param("ids") List<String> ids);
}
