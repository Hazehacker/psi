package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.dto.j2.store.OtherOutListDTO;
import com.zeroone.star.project.query.j2.store.OtherOutQuery;
import com.zeroone.star.storemanagement.entity.ExtryDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtherOutMapper extends BaseMapper<ExtryDO> {

    void examine(List<Integer> ids);

    List<OtherOutListDTO> selectMainIds(@Param("ids") List<String> ids);

    void insertBatch(ExtryDO extry);

    Page<ExtryDO> selectOtherOutListPage(Page<ExtryDO> page,@Param("query") OtherOutQuery query);


    List<ExtryDO> selectBatchIds(List<Integer> ids);

    @Delete("delete from extry where id = #{id}")
    void deleteById(String id);


    @Select("select * from extry where id = #{id}")
    Integer getExamineById(String id);



}
