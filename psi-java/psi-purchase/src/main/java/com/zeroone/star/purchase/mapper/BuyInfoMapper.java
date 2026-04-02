package com.zeroone.star.purchase.mapper;

import com.zeroone.star.project.dto.j3.purchase.PurchaseNoteInfoDTO;
import com.zeroone.star.purchase.DO.BuyInfoDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 采购单详情 Mapper 接口
 * </p>
 *
 * @author 小夏
 * @since 2025-10-27
 */
@Mapper
public interface BuyInfoMapper extends BaseMapper<BuyInfoDO> {


    /**
     * @Description: 删除没有的商品数目
     * @Author: 正念
     */
    int deleteByPidAndIdNotIn(@Param("pid") String pid,
                              @Param("ids") List<String> ids);

    /**
     * 查询出采购单详情
     * @param pid
     * @return
     */
    List<BuyInfoDO> selectByPid(@Param("pid") String pid);


    /**
     * 根据采购单详情id获取完整详情
     * @param id 采购单详情ID (buy_info.id)
     * @return 包含采购单、采购单详情、商品信息的聚合DTO
     */
    PurchaseNoteInfoDTO getPurchaseNoteInfoDetail(@Param("id") String id);

}
