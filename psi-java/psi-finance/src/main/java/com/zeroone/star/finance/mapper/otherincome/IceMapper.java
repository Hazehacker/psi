package com.zeroone.star.finance.mapper.otherincome;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.finance.entity.Ice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.finance.entity.Iet;
import com.zeroone.star.project.query.j8.finance.otherincome.OtherIncomeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 其它收入单 Mapper 接口
 * </p>
 *
 * @author 幻風
 * @since 2025-10-24
 */
@Mapper
public interface IceMapper extends BaseMapper<Ice> {
    /**
     * 分页查询收入单列表
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    Page<Ice> selectOtherIncomePage(@Param("page") Page<Ice> page, @Param("query") OtherIncomeQuery query);

    /**
     * 根据其它收入单id查询收入单
     * @param id 收入单ice.id
     * @return 收入单
     * */
    Ice selectOtherIncomeById(String id);

    @Select("SELECT COUNT(*) FROM zo_psi.frame  WHERE id = #{id}")
    int checkFrame(String id);

    @Select("SELECT COUNT(*) FROM zo_psi.customer  WHERE id = #{id}")
    int checkCustomer(String id);

    @Select("SELECT COUNT(*) FROM zo_psi.account  WHERE id = #{id}")
    int checkAccount(String id);

    @Select("SELECT COUNT(*) FROM zo_psi.people  WHERE id = #{id}")
    int checkPeople(String id);

    @Select("SELECT COUNT(*) FROM zo_psi.user  WHERE id = #{id}")
    int checkUser(String id);


    List<Iet> selectIncomeExpendTypeByIds(@Param("ids") List<String> ids);
}
