package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Imy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.ImyCountsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 收款单 Mapper 接口
 * </p>
 *
 * @author MRME39
 * @since 2025-10-23
 */
@Mapper
public interface ImyMapper extends BaseMapper<Imy> {
    /**
     * 获取对应表中的审核状态，如果审核状态为已审核就查询出当天的结算金额
     * @return 某一天总和金额
     */
    @Select("SELECT time days,total count FROM imy where examine = 1")
    List<ImyCountsDTO> ImyCount();
}
