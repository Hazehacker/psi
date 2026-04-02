package com.zeroone.star.finance.mapper.otherexpense;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.OceSimpleViewDO;
import com.zeroone.star.project.dto.j8.finance.otherexpense.OtherExpenseViewDTO;
import com.zeroone.star.project.query.j8.finance.othexpense.OtherExpenseQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OceSimpleViewMapper {
    IPage<OceSimpleViewDO> selectByCondition(Page<OceSimpleViewDO> page,
                                             @Param("q") OtherExpenseQuery query);

    List<OceSimpleViewDO> selectByIds(@Param("ids") List<String> ids);

    //新增其它支出单
    int insertOce(OtherExpenseViewDTO otherExpense);
    //更新其它支出单
    int updateOce(OtherExpenseViewDTO otherExpense);
    //批量更新审核状态
    int updateExamine(@Param("ids") List<String> ids, @Param("examine") int examine);
    //查询某个其它支出单
    OtherExpenseViewDTO selectById(String id);

    // 校验 account 是否存在
    int countAccountById(@Param("id") String accountId);

    // 校验 frame 是否存在
    int countFrameById(@Param("id") String frameId);

    // 校验 supplier 是否存在
    int countSupplierById(@Param("id") String supplierId);

    // 校验 people 是否存在
    int countPeopleById(@Param("id") String peopleId);

    // 校验 user 是否存在
    int countUserById(@Param("id") String userId);
}
