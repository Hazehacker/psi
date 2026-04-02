package com.zeroone.star.moneytransfer.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.zeroone.star.moneytransfer.service.IAllotExportImportService;
import com.zeroone.star.moneytransfer.service.IAllotService;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.money.transfer.*;
import com.zeroone.star.project.j7.money.transfer.AllotApis;
import com.zeroone.star.project.query.j7.money.transfer.AllotExamineQuery;
import com.zeroone.star.project.query.j7.money.transfer.AllotQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名：AllotController
 * 包名：com.zeroone.star.controller
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/allot")
@Api(tags = "转账单")
public class AllotController implements AllotApis {
    @Resource
    IAllotService allotService;

    @Resource
    private IAllotExportImportService allotExportImportService;
    @PostMapping("/import")
    @ApiOperation(value = "导入转账报表")
    public JsonVO<Object> importAllot(@ApiParam(value = "上传的文件", required = true) @RequestPart("file") MultipartFile  file) {
        if (file.isEmpty()) {
            return JsonVO.fail("请选择文件");
        }
        try {
            allotExportImportService.importAllot(file);
        }catch (Exception e){
            return JsonVO.create("导入失败,失败原因为:"+ e.getMessage(), ResultStatus.PARAMS_INVALID);
        }
        return JsonVO.success("导入成功");
    }

    @PostMapping("/export/detail")
    @ApiOperation(value = "导出转账单详情报表")
    public ResponseEntity<byte[]> exportAllotDetail(@Validated @NotNull @ApiParam(value = "转账单ID列表", required = true, example = "[\"1\",\"2\"]")@RequestBody List<String> ids) {
        byte[] bytes = null;
        try {
            bytes = allotExportImportService.exportAllotDetail(ids);
        } catch (Exception e) {
            String errorMessage = makeJsonStr("导出失败", 400, e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // 设置为 JSON 类型
            return new ResponseEntity<>(errorMessage.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers= new HttpHeaders();
        ResponseEntity<byte[]> res;
        String filename = "detail-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        res= new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        return res;
    }

    /**
     *
     * @param data 响应数据
     * @param code 响应码
     * @param message 错误信息
     * @return
     */
    private String makeJsonStr (String data,int code,String message) {
        Map<String,Object> map = new HashMap<>();
        map.put("data", data);
        map.put("code", code);
        map.put("message", message);
        return JSONUtil.toJsonStr(map);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出简略转账单报表")
    public ResponseEntity<byte[]> exportAllot(@Validated @NotNull @ApiParam(value = "转账单ID列表", required = true, example = "[\"1\",\"2\"]")@RequestBody List<String> ids) {
        byte[] bytes = null;
        try {
            bytes = allotExportImportService.exportAllot(ids);
        } catch (Exception e) {
            String errorMessage = makeJsonStr("导出失败", 400, e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // 设置为 JSON 类型
            return new ResponseEntity<>(errorMessage.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers= new HttpHeaders();
        ResponseEntity<byte[]> res;
        String filename = "brief-" + DateTime.now().toString("yyyyMMddHHmmssS") + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        res= new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        return res;
    }


    @GetMapping("/getAllotList")
    @ApiOperation("获取转账单列表(条件+分页)")
    @Override
    public JsonVO<PageDTO<AllotListDTO>> allotList(@Validated AllotQuery query) {

        return JsonVO.success(allotService.listAllot(query));
    }

    @PostMapping("/addAllot")
    @ApiOperation("新增转账单")
    @Override
    public JsonVO<String> addAllot(@Validated @RequestBody AddAllotDTO addAllotDTO) {

        //新增包含allot表和allotinfo表的变化
        String res= null;
        try {
            res = allotService.saveAllot(addAllotDTO);
        } catch (RuntimeException e) {
           return JsonVO.create(null,ResultStatus.FAIL.getCode(),e.getMessage());
        }
        return JsonVO.success(res);
    }

    @GetMapping("/getAllotDetail")
    @ApiOperation(("获取指定转账单详情"))
    @Override
    public JsonVO<AllotDetailDTO> queryDetail(
            @ApiParam(value = "转账单id",example = "1",required = true)
           @Validated @NotBlank(message = "id不能为空") @RequestParam String id) {
        AllotDetailDTO detailDTO=allotService.getAllotdetail(id);
        if(detailDTO==null){
            return JsonVO.create(null,ResultStatus.FAIL.getCode(),"查询该记录在数据表中不存在");
        }else return JsonVO.success(detailDTO);
    }

    @DeleteMapping("/deleteAllots")
    @ApiOperation("删除转账单（支持批量）")
    @Override
    public JsonVO<List<String>> deleteallots(@Validated @NotNull(message = "id列表不能为空")@ApiParam(value = "转账单id",required = true,example = "[\"10001\", \"10002\", \"10003\"]")@RequestBody List<String> ids) {
        List<String> successIds=new ArrayList<>();
        try {
            successIds=allotService.removeAllots(ids);
        } catch (RuntimeException e) {
            //传入了错误id
            return JsonVO.create(null,ResultStatus.FAIL.getCode(),e.getMessage());
        }
        if(successIds.size()<ids.size()){
            return JsonVO.create(successIds,ResultStatus.SUCCESS.getCode(),"已经审核的账单不可删除，可删除数据已完成操作," +
                    "已经操作："+successIds.size()+"条");
        }
        return JsonVO.success(successIds);
    }

    @ApiOperation("审核/反审核（支持批量）")
    @PutMapping("/examineAllots")
    @Override
    public JsonVO<List<String>> examine(@Validated AllotExamineQuery query, @Validated @NotNull(message = "id不能为空")@ApiParam(value = "转账单id",required = true,example = "[\"10001\", \"10002\", \"10003\"]")@RequestBody List<String> ids) {
        List<String> successIds=new ArrayList<>();
        try {
            successIds=allotService.examine(query,ids);
        } catch (RuntimeException e) {
            //
            return  JsonVO.create(null,ResultStatus.FAIL.getCode(), e.getMessage());
        }
       if(successIds.size()<ids.size()){
           return JsonVO.create(successIds,ResultStatus.SUCCESS.getCode(),"存在无需操作数据，可审核/反审核数据已完成操作," +
                   "已经操作："+successIds.size()+"条");
       }
       return JsonVO.success(successIds);
    }

    @ApiOperation("修改转账单")
    @PutMapping("/updateAllot")
    @Override
    public JsonVO<String> updateAllot(@Validated @RequestBody AllotModifyDTO allotModifyDTO) {
        if(allotService.updateAllot(allotModifyDTO)) {
            return JsonVO.success("修改成功");
        }
        return JsonVO.fail("修改失败");
    }
}
