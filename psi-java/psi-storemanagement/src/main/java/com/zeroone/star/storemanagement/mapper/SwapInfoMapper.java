package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j2.store.TransferDetailListDTO;
import com.zeroone.star.storemanagement.entity.SwapInfoDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface SwapInfoMapper extends BaseMapper<SwapInfoDO> {
    /*用来查询Swap_info中是否已经有即将插入的信息，有返回id字段*/
    String selectValue(@Param("swapInfoDO") SwapInfoDO swapInfoDO);
    /**
     * 查看调拨单详细信息，前端传来的调拨单id，对应的swap_info表里的pid
     * @param id  调拨单id，然后根据此信息去查并返回调拨单详细信息
     * @return 拨单详细信息
     */
    TransferDetailListDTO detailTransfer(String id);

    /**新增详细调拨单信息中的swap_info信息*/
    void insertSwapInfo(SwapInfoDO swapInfoDO);

    /**
     * 根据id查询到调拨单的id
     */
    @Select("SELECT pid FROM swap_info WHERE id = #{id}")
    String getSwapById(@Param("id") String id);

    /**
     * 更新调拨单详情信息
     */
    int updateSwap(SwapInfoDO swapInfo);

    /**
     * 根据ID列表查询对应的pid列表
     */
    List<String> getPidListByIds(@Param("ids") List<Integer> ids);

    ArrayList<TransferDetailListDTO> getTransferDetailListDTO(@Param("id") String id);

    /**
     * 根据id查询商品属性
     */
    @Select("SELECT attr FROM swap_info WHERE id = #{goodsId}")
    String getAttrById(String goodsId);

    /**
     * 批量删除调拨单详情
     */
    int deleteByIdList(@Param("ids") List<Integer> ids);

    /**
     * 批量查询调拨单状态和主表信息
     */
    @MapKey("id")
    List<Map<String, Object>> getBatchTransferStatus(@Param("ids") List<String> ids);

    /**
     * 批量查询调拨单详情
     */
    List<SwapInfoDO> getBatchTransferDetail(@Param("ids") List<String> ids);

    /**
     * 批量获取调拨单对应的主表ID
     */
    List<String> getSwapByIds(@Param("ids") List<String> ids);

    /**
     * 批量检查调拨单是否存在
     */
    int getExistsCountByIds(@Param("ids") List<String> ids);

    /**
     * 批量获取调拨单对应的编号
     */
    List<String> getBatchTransferNumbers(@Param("ids") List<String> ids);
}
