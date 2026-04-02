package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.OtherInListDTO;
import com.zeroone.star.project.query.j2.store.OtherInQuery;
import com.zeroone.star.storemanagement.entity.EntryDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 其他入库单 Mapper 接口
 * </p>
 *
 * @author blissette
 * @since 2025-10-27
 */
public interface OtherInListMapper extends BaseMapper<EntryDO> {
    /**
     * 分页查询其他入库单
     * @param page  分页对象
     * @param query 查询条件参数
     * @return 分页结果
     */
    Page<EntryDO> selectOtherInListPage(Page<EntryDO> page, @Param("query") OtherInQuery query);
}