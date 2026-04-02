package com.zeroone.star.purchase.mapper;

import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnDetailReportDTO;
import com.zeroone.star.purchase.DO.BreDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 采购退货单 Mapper 接口
 * </p>
 *
 * @author Hazenix 
 * @since 2025-10-27
 */
@Mapper
public interface BreMapper extends BaseMapper<BreDO> {

    /**
     * 导出采购退货单详细报表
     * @param ids 采购退货单id
     * @return 带有导出报表信息的响应实体
     * @author Hazenix 
     * @date 2025/10/27
     */
    List<PurchaseReturnDetailReportDTO> exportBreInfo(List<String> ids);
    @Update("update bre set `check` = 1 where id = #{id}")
    void checkOut( @Param("id") String id);

    @Update("update bre set `check` = 0 where id = #{id}")
    void notCheckOut(@Param("id") String id);

}
