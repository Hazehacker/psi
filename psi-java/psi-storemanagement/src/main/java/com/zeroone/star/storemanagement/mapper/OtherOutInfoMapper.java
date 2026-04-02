package com.zeroone.star.storemanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.project.dto.j2.store.OtherOutListInfoDTO;
import com.zeroone.star.storemanagement.entity.ExtryDO;
import com.zeroone.star.storemanagement.entity.ExtryInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OtherOutInfoMapper extends BaseMapper<ExtryInfoDO> {

    List<OtherOutListInfoDTO> selectByMainIds(@Param("mainIds") List<String> mainIds);
    void insertBatch(List<ExtryInfoDO> extryInfoList);
    void insertByOne(ExtryInfoDO extryInfoDO);

    @Delete("delete from extry_info where pid = #{id}")
    void deleteById(String id);


}
