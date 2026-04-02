package com.zeroone.star.finance.mapper.otherexpense;

import com.zeroone.star.finance.entity.OceInfoViewDO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.OtherExpenseInfoViewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OceInfoViewMapper {
    List<OceInfoViewDO> selectByPid(@Param("pid") String pid);

    //新增其它支出单详情（支持批量）
    int insertOceInfoView(@Param("list") List<OtherExpenseInfoViewDTO> otherExpenseInfoList);

    //删除某个其它支出单的所有详情
    int deleteOceInfoView(@Param("pid") String pid);

    // 校验收支类别（iet）
    int countIetById(@Param("id") String id);
}
