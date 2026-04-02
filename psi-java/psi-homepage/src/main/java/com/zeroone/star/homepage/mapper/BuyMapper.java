package com.zeroone.star.homepage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.homepage.entity.Buy;
import com.zeroone.star.project.dto.j1.homepage.DV;
import com.zeroone.star.project.query.j1.homepage.PurchaseDailyTotalQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * <p>
 * 描述：采购单Mapper
 * </p>
 *
 * @author zonk
 */
@Mapper
public interface BuyMapper extends BaseMapper<Buy> {

    @Select({
            "<script>",
            "SELECT",
            "    DATE_FORMAT(time, '%Y-%m-%d') AS `date`,",
            "    SUM(actual) AS `value`",
            "FROM",
            "    buy",
            "WHERE",
            "    examine = 1",
            "    AND time >= #{query.startDate}",
            "    AND time &lt; DATE_ADD(#{query.endDate}, INTERVAL 1 DAY)",
            "    AND frame = #{frameId}",
            "GROUP BY",
            "    DATE_FORMAT(time, '%Y-%m-%d')",
            "ORDER BY",
            "    `date` ASC",
            "</script>"
    })
    ArrayList<DV> getPurchaseOrderStatistics(@Param("query") PurchaseDailyTotalQuery query,
                                             @Param("cur_userID") String cur_userID,
                                             @Param("frameId") String frameId);
}