//package com.zeroone.star.basic_information.controller;
//
//import com.zeroone.star.basic_information.service.queryUserListService;
//import com.zeroone.star.project.dto.PageDTO;
//import com.zeroone.star.project.dto.j6.basic_information.customer_management.userDto;
//import com.zeroone.star.project.query.j6.basic_information.customer_management.userListQuery;
//import com.zeroone.star.project.vo.JsonVO;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("/basic-information/user")
//public class QueryUserListController {
//    @Resource
//    queryUserListService queryUserListService;
//    @ApiOperation(value = "查询用户列表")
//    @RequestMapping("/queryUserList")
//    public JsonVO<PageDTO<userDto>> queryUserList(userListQuery query) {
//        return JsonVO.success(queryUserListService.queryUserList(query));
//    }
//}
