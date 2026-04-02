package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.TransferDetailListDTO;
import com.zeroone.star.project.dto.j2.store.TransferListDTO;
import com.zeroone.star.project.query.j2.store.TransferQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.storemanagement.entity.SwapDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface SwapMapper extends BaseMapper<SwapDO> {
    /**用来查询swap表中是否已经存在该数据，存在则返回id*/
    String selectSwap(@Param("swapDO") SwapDO swapDO);
    /**
     * 获取调拨单列表
     * @param transferQuery 调拨单列表查询参数
     * @return 包含调拨单列表信息的Page对象
     */
     Page<TransferListDTO> queryTransfer(
            @Param("page")
            Page<TransferListDTO> page,
            @Param("query")
            TransferQuery transferQuery);

    /*给swap表添加数据，返回值是swap_info的pid值*/
     void insertSwap(SwapDO swap);
    /**
     * 根据id查询调拨单状态
     */
    @Select("SELECT examine FROM swap WHERE id = #{id}")
    Integer getStatusById(@Param("id") String id);
    /**
     * 更新调拨单信息
     */
    int updateSwap(SwapDO swap, @Param("total") BigDecimal total);

    /**
     * 批量删除
     */
    int deleteByIdList(@Param("ids") List<String> ids);

    /**
     * 获取调拨单列表
     */
    List<TransferListDTO> getTransferListDTOList(@Param("idList") List<String> idList);

    /**
     * 批量审核/反审核调拨单
     */
    int auditBatchStatus(@Param("pidList") List<String> validPidList, @Param("operation") Integer operation);

    /**
     * 批量查询调拨单状态
     */
    @MapKey("id")
    List<Map<String, Object>> getStatusByIds(@Param("ids") List<String> ids);

    /**
     * 批量删除调拨单
     */
    int deleteBatchIds(@Param("ids") List<String> ids);

    /**
     * 获取调拨单时间
     */
    @Select("SELECT time FROM swap WHERE id = #{pid}")
    LocalDateTime getTime(String pid);
}
