package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.storemanagement.entity.EntryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OtherInMapper extends BaseMapper<EntryDO> {

    void update(EntryDO entry);

    void updateExamine(List<Integer> ids, int status);

    List<Integer> getExamineByIds(List<Integer> ids);

    List<Integer> getCheckByIds(List<Integer> ids);

    void updateCheck(List<Integer> ids, int status);

    @Select("select examine from entry where id = #{id}")
    Integer getExamineById(String id);

    List<EntryDO> getByIds(List<Integer> ids);

     int insert(EntryDO entryDO);

     EntryDO selectById(String id);

    List<EntryDO> selectBatchIds(List<Integer> ids);

    void deleteById(String id);
}
