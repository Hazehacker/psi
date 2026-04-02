package com.zeroone.star.moneytransfer.mapper;

import com.zeroone.star.moneytransfer.entity.Account;
import com.zeroone.star.moneytransfer.entity.AccountInfo;
import com.zeroone.star.moneytransfer.entity.Allot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.moneytransfer.entity.AllotInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 转账单 Mapper 接口
 * </p>
 *
 * @author hh
 * @since 2025-10-23
 */
@Mapper
public interface AllotMapper extends BaseMapper<Allot> {
    //映射所属组织--allot.frame==frame.id
    @Select("select name from frame where id=#{frame}")
    String getFrameName( Allot allot);

    @Select("select name from user where id=#{user}")
    String getUserName(Allot allot);

    @Select("select name from people where id=#{people}")
    String getPeopleName(Allot allot);

    @Select("select name from people")
   List<String> getPeopleList();

    @Insert("insert into account_info(id,pid,type,`class`,time,direction,money) values(#{id},#{pid},#{type},#{className},#{time},#{direction},#{money})")
    void insertAccountInfo(AccountInfo accountInfo);

    @Update("update account set time=#{time},balance=#{balance} where id=#{id}")
    void updateAccount(Account account);

    @Select("select *from account where id =#{id}")
    Account getAccount(String id);

    @Select("select * from allot_info where pid =#{pid}")
    List<AllotInfo> getAllotInfoList(String pid);

    String insertBackId(Allot allot);

    @Select("<script>" +
            "SELECT * FROM allot WHERE id IN (" +
            "<foreach collection='ids' item='id' separator=','>#{id}</foreach>" +
            ")</script>")
    List<Allot> allotList(@Param("ids") List<String> ids);
}
