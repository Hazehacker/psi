package com.zeroone.star.capital.mapper;

import com.zeroone.star.capital.DO.BreDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import com.zeroone.star.project.dto.j3.purchase.PurchaseReturnDetailReportDTO;
import java.util.List;

/** 采购退货单 Mapper 接口 */
@Mapper
public interface BreMapper extends BaseMapper<BreDO> {
    List<PurchaseReturnDetailReportDTO> exportBreInfo(List<String> ids);
    @Update("update bre set `check` = 1 where id = #{id}")
    void checkOut(@Param("id") String id);
    @Update("update bre set `check` = 0 where id = #{id}")
    void notCheckOut(@Param("id") String id);
}
