package com.zeroone.star.supportinfo.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.inandexpend.*;
import com.zeroone.star.project.j7.sysargs.supportinfo.inandexpend.InandExApis;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.inandexpend.IetQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.supportinfo.exception.ResourceNotFoundException;
import com.zeroone.star.supportinfo.service.IIetService;
import com.zeroone.star.supportinfo.service.impl.MsIetMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import  java.util.List;

import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 类名：InandExController
 * 包名：com.zeroone.star
 * 描述：
 * 作者：l,map
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/iet")
@Api(tags="收支类别")
public class IetController implements InandExApis {
    @Resource
    private IIetService iIetService;

    @Resource
    private MsIetMapper msIetMapper;

    /**
     * 按收支类别查询收支名称
     * @param type 查询参数——收支类型
     * @return
     */
    @GetMapping("/queryByType")
    @ApiOperation(value = "获取收支类别名称列表（条件）")
    @Override
    public JsonVO<List<IetNameDTO>> listInandexName(
            @Min(value = 0, message = "类型必须为0（收入）或1（支出）")
            @Max(value = 1, message = "类型必须为0（收入）或1（支出）")
            @RequestParam("type")
            Integer type) {
        List<IetNameDTO> ietNameDTO = iIetService.selectIetName(type);
        return JsonVO.success(ietNameDTO);
    }


    /**
     * 查询收支类别列表
     * @param query 查询参数
     * @return
     */
    @GetMapping("/queryList")
    @ApiOperation(value = "获取收支类别列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<IetListDTO>> listInandex(@Valid IetQuery query) {
        PageDTO<IetListDTO> listDTOPageDTO = iIetService.listAll(query);
        return JsonVO.success(listDTOPageDTO);
    }


    /**
     * 获取收支类别详情
     * @param id
     * @return
     */
    @GetMapping("/queryDetail")
    @ApiOperation(value = "获取指定类别详情")
    @ApiImplicitParam(name = "id", value = "收支类别id", required = true, example = "1")
    @Override
    public JsonVO<IetDetailDTO> getInandexDetail( String id) {
        IetDetailDTO ietDetailDTO = iIetService.getInandexDetail(id);
        if(ietDetailDTO != null) {
            return JsonVO.success(ietDetailDTO);
        }
        else {
            return JsonVO.fail(null);
        }
    }

    /**
     * 新增收支类别
     *
     * @param ietAddDTO 查询参数——收支类型
     * @return 类别名称列表
     */
    @PostMapping("/addIet")
    @ApiOperation("新增收支类别")
    @Override
    public JsonVO<String> addInandex(@Validated @RequestBody IetAddDTO ietAddDTO) {

//        IetAddDTO addIetAddDto=new IetAddDTO();
//        BeanUtils.copyProperties(ietAddDTO,addIetAddDto);
        String id=null;
        try {
            id = iIetService.insertIet(ietAddDTO);
        }catch (IllegalArgumentException e){
            return JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(id);


    }

    /**
     * 修改指定类别
     *
     * @param ietUpdateDTO 查询参数
     * @return
     */

    @PutMapping("/updateIet")
    @ApiOperation("修改指定类别")
    @Override
    public JsonVO<String> updateInandex(@Validated @RequestBody IetUpdateDTO ietUpdateDTO) {

        // 调用Service更新（直接传DTO，Service层按需处理）
        String id=null;
        try {
            id = iIetService.updateIet(ietUpdateDTO);
        }catch (ResourceNotFoundException e){
            return JsonVO.fail(e.getMessage());
        }catch (IllegalArgumentException e){
            return JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(id);
    }
    /**
     * 删除指定类别
     *
     * @param id 删除的收支类别id
     * @return
     */
    @DeleteMapping("/deleteIet")
    @ApiOperation("删除指定类别")
    @Override
    public JsonVO<String> deleteInandex(@ApiParam(value = "收支类别id",example = "1",required = true)
            @RequestParam String id) {
        String res=null;
        try{
            res=iIetService.deleteIet(id);
        }catch (ResourceNotFoundException e){
            return  JsonVO.fail(e.getMessage());
        }
        return JsonVO.success(id);
    }
}
