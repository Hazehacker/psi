package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Sre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.SreCountsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 销售退货单 Mapper 接口
 * </p>
 *
 * @author MRME39
 * @since 2025-10-23
 */
@Mapper
public interface SreMapper extends BaseMapper<Sre> {
    /**
     * 获取对应表中的审核状态，如果审核状态为已审核就查询出当天的金额
     * @return 某一天总和金额
     */
    @Select("SELECT time days,actual count FROM sre where examine = 1")
    List<SreCountsDTO> SreCount();
}
