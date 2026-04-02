package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.ServeDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ServeMapper extends BaseMapper<ServeDO> {

    @Delete("delete from serve where id = #{id}")
    void deleteById(String id);

    @Select("select id,goods,attr,nums from serve where goods = #{goods} and attr = #{attr}")
    ServeDO getByGoodsAndAttr(String goods, String attr);
}
