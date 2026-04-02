package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.BatchDetailDTO;
import com.zeroone.star.project.query.j2.store.BatchDetailQuery;
import com.zeroone.star.storemanagement.entity.BatchInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.storemanagement.mapper
 * @Author: 高
 * @CreateTime: 2025-10-29 15:14
 * @Description: 批次信息数据访问层
 * 负责批次信息（batch_info表）的CRUD操作及批次详情查询
 * @Version: 1.0
 */
@Mapper
public interface BatchInfoMapper extends BaseMapper<BatchInfoDO> {

    /**
     * 获取批次详情数据列表（分页）
     * @param page 分页对象，包含分页参数
     * @param batchDetailQuery 查询条件，包含pid、类型、时间范围、单据编号等过滤条件
     * @return 批次详情分页数据列表
     */
    Page<BatchDetailDTO> getBatchDetail(Page<BatchDetailDTO> page, @Param("batchDetailQuery") BatchDetailQuery batchDetailQuery);

    /**
     * 根据换货单ID删除批次信息
     * @param swapInfoId 换货单ID
     * @return 删除的批次信息数量
     */
    @Delete("delete from batch_info where info = #{swapInfoId}")
    int deleteBySwapInfoId(String swapInfoId);

    @Delete("delete from batch_info where info = #{info}")
    void deleteByInfo(String info);
}
