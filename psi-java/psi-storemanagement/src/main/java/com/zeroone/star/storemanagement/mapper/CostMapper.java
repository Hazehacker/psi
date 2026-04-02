package com.zeroone.star.storemanagement.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.CostDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CostMapper extends BaseMapper<CostDO> {

    @Select("select max(id) from cost")
    Integer getMaxId();

    @Delete("delete from cost where class = #{id}")
    void deleteBycls(String id);

    void insertBatch(List<CostDO> costList);

    /**
     * 根据调拨单ID查询费用列表
     */
    @Select("SELECT id, type, class, iet, money, data, settle, state FROM cost WHERE class = #{transferId} AND type = 'swap'")
    List<CostDO> findByTransferId(@Param("transferId") String transferId);

    /**
     * 根据ID删除费用
     */
    @Delete("DELETE FROM cost WHERE id = #{id}")
    void deleteById(@Param("id") String id);

    /**
     * 根据ID更新费用
     */
    int updateById(CostDO costDO);

    /**
     * 插入费用
     */
    int insert(CostDO costDO);

    /**
     * 判断费用是否存在
     */
    @Select("SELECT EXISTS(SELECT 1 FROM cost WHERE id = #{id})")
    boolean isExist(String id);

    /**
     * 根据调拨单ID删除对应的费用记录
     * @param transferId 调拨单ID (对应cls字段)
     */
    @Delete("DELETE FROM cost WHERE class = #{transferId} AND type = 'swap'")
    void deleteByTransferId(String transferId);
}
