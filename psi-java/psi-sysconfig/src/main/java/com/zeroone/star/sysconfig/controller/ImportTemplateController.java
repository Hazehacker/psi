package com.zeroone.star.sysconfig.controller;

import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.TemplateUpdateDTO;
import com.zeroone.star.project.j1.sysconfig.ImportTemplateApis;
import com.zeroone.star.project.query.j1.sysconfig.TempImportQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportDownloadVO;
import com.zeroone.star.project.vo.j1.sysconfig.TempImportVO;
import com.zeroone.star.sysconfig.service.ITmplImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/import-template")
@Api(tags="导入模板")
public class ImportTemplateController implements ImportTemplateApis {
    @Autowired
    ITmplImportService tmplImportService;

/**
 * Handles HTTP POST requests for importing template data.
 * This endpoint accepts multipart/form-data content type for file uploads.
 *
 * @param templateAddDTO The data transfer object containing template information to be imported
 * @return JsonVO containing a success message if the import operation is completed successfully
 */
    @PostMapping(value = "/import" , consumes = "multipart/form-data")
    @Override
    @ApiOperation("新增模板")
    public JsonVO<String> importTemplate(@ModelAttribute TemplateAddDTO templateAddDTO) {
        if(templateAddDTO.getFile()==null||templateAddDTO.getFile().isEmpty()) {
            return JsonVO.fail("文件不能为空");
        }

        if(tmplImportService.importTemplate(templateAddDTO)==1){
            return JsonVO.success("导入成功");
        }
        else{
            return JsonVO.fail("导入失败");
        }
    }
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @Override
    @ApiOperation("修改指定模板")
    public JsonVO<String> updateTemplate(@PathVariable("id") String id, @ModelAttribute TemplateUpdateDTO templateUpdateDTO) {
        templateUpdateDTO.setId(id);
        if(tmplImportService.updateTemplate(templateUpdateDTO)==1){
            return JsonVO.success("修改成功");
        }
        else{
            return JsonVO.fail("修改失败");
        }
    }

    @DeleteMapping("delete/{id}")
    @Override
    @ApiOperation("删除指定模板")
    public JsonVO<String> deleteTemplate(@PathVariable @ApiParam(value="模板id") String id) {
       if(tmplImportService.deleteTemplate(id)==0){
           return JsonVO.fail("删除失败,模板不存在");
       }
       else{
           return JsonVO.success("删除成功");
       }
    }






    @GetMapping("/list")
    @Override
    @ApiOperation("获取模板列表（条件+分页）")
    public JsonVO<PageDTO<TempImportVO>> queryTemplateList(@Validated TempImportQuery query) {
        return JsonVO.success(tmplImportService.queryTemplateList(query));
    }

    @GetMapping("/download/{id}")
    @Override
    @ApiOperation("获取指定模板详情")
    public JsonVO<TempImportDownloadVO> getDownloadInfoById(@PathVariable("id") String id) {
        return JsonVO.success(tmplImportService.getDownloadInfoById(id));
    }


}
