package com.zeroone.star.basic_information.controller;

import com.zeroone.star.basic_information.service.IAccountService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.AccountListDTO;
import com.zeroone.star.project.dto.j6.basic_information.fund_account.SaveAccountDTO;
import com.zeroone.star.project.j6.basic_information.fund_account.AccountApis;
import com.zeroone.star.project.query.PageQuery;
import com.zeroone.star.project.query.j6.basic_information.fund_account.AccountQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 描述: 资金账户控制层
 * </p>
 *
 * @author goldenHour
 * @version 1.0.0
 */
@RestController
@RequestMapping("/account")
@Api(tags = "资金账户")
public class AccountController implements AccountApis {

    @Resource
    IAccountService accountService;  


    @ApiOperation("查询账户列表")
    @GetMapping("/list")
    @Override
    public JsonVO<PageDTO<AccountListDTO>> listAccounts(@ApiParam("账户条件查询参数")AccountQuery accountQuery, PageQuery pageQuery)
    {
        PageDTO<AccountListDTO> pageDTO =accountService.listAccounts(accountQuery,pageQuery);
        return JsonVO.success(pageDTO);
    }

    @ApiOperation("获取账户详情")
    @GetMapping("/get")
    @Override
    public JsonVO<AccountDTO> getAccount(String id)
    {
        AccountDTO accountDTO=accountService.getAccount(id);
        return JsonVO.success(accountDTO);
    }

    @ApiOperation("添加账户")
    @PostMapping("/add")
    @Override
    public JsonVO<String> addAccount(SaveAccountDTO saveAccountDTO)
    {

        int rows=accountService.addAccount(saveAccountDTO);
        if(rows==0) return JsonVO.fail("添加失败");
        return JsonVO.success("添加成功");
    }

    @ApiOperation("删除账户")
    @DeleteMapping("/delete")
    @Override
    public JsonVO<String> deleteAccount(String id)
    {
        int rows = accountService.deleteAccount(id);
        if(rows==0) return JsonVO.fail("删除失败");
        return JsonVO.success("删除成功");
    }

    @ApiOperation("修改账户")
    @PutMapping("/update")
    @Override
    public JsonVO<String> updateAccount(AccountDTO accountDTO)
    {
        int rows = accountService.updateAccount(accountDTO);
        if(rows==0) return JsonVO.fail("修改失败");
        return JsonVO.success("修改成功");
    }



}
