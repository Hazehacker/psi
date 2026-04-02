package com.zeroone.star.moneytransfer.mapper;

import com.zeroone.star.moneytransfer.entity.AllotInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeroone.star.moneytransfer.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 转账单详情 Mapper 接口
 * </p>
 *
 * @author hh
 * @since 2025-10-24
 */
@Mapper
public interface AllotInfoMapper extends BaseMapper<AllotInfo> {
    @Select("select money from allot_info where pid=#{pid}")
    BigDecimal getMoney(String pid);

    @Select("select name from account where id=#{id}")
    String getAccountName( String id);
    //获取account列表
    @Select("select name from account")
    List<String> getAccountStringList();


    /**
     * 根据账户查询转账单详情
     * @param account 账户 这是对账户表的查询
     * @return 转账单详情
     */
    Boolean selectByAccount (String account);

    /**
     *
     * @param name 关联人员名称 这是对人员表的查询
     * @return
     */
    Boolean selectByName(String name);

    /**
     *  根据单据编号查询
     * @param number
     * @return
     */
    String selectByNumber(String number);

    User selectUserById(String id);

    List<AllotInfo> selectBatchPids(List<String> ids);
}
