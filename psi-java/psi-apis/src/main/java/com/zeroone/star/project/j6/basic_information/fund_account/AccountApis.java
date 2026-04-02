package com.zeroone.star.project.j6.basic_information.fund_account;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountListDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.FrameDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.SaveAccountDTO;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j6.basic_information.fund_account.AccountQuery;
import com.zeroone.star.project.vo.JsonVO;


import java.util.List;

/**
 * <p>
 * 描述: 资金账户接口
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
public interface AccountApis {

    /**
     * 查询账户列表
     * @param query 查询参数
     * @return 账户列表
     */
    JsonVO<PageDTO<AccountListDTO>> listAccounts(AccountQuery query, PageQuery pageQuery);

    /**
     * 获取账户详情
     * @param id 账户id
     * @return 账户详情
     */
    JsonVO<AccountDTO> getAccount(String id);

    /**
     * 添加账户
     * @param saveAccountDTO 添加账户
     * @return 添加账户的结果
     */
    JsonVO<String> addAccount(SaveAccountDTO saveAccountDTO);

    /**
     * 删除账户
     * @param id 账户id
     * @return 删除的结果
     */
    JsonVO<String> deleteAccount(String id);

    /**
     *  修改账户
     * @param accountDTO 修改账户
     * @return 修改账户的结果
     */
    JsonVO<String> updateAccount(AccountDTO accountDTO);



}
