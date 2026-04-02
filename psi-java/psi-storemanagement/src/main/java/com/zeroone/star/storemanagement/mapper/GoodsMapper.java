package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.GoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsMapper extends BaseMapper<GoodsDO> {
    @Select("select * from goods where id = #{id}")
    GoodsDO selectById(String id);

    /**新增详细调拨单信息中的商品信息,返回值是swap_info的goods值*/
    void insertGoods(GoodsDO goodsDO);

    /**用来查询goods表中是否已经存在该数据，存在则返回id*/
    String selectGoods(@Param("goodsDO") GoodsDO goodsDO);
}
