package com.zeroone.star.homepage.mapper;

import com.zeroone.star.homepage.entity.Often;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j1.homepage.CommonMenuDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 常用功能 Mapper 接口
 * </p>
 *
 * @author heavydrink
 * @since 2025-10-22
 */
@Mapper
public interface OftenMapper extends BaseMapper<Often> {

    List<CommonMenuDTO> getCommonMenus(String userId);
}
