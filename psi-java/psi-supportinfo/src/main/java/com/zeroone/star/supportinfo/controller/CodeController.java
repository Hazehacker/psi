package com.zeroone.star.supportinfo.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeroone.star.project.components.easyexcel.EasyExcelComponent;
import com.zeroone.star.project.components.easyexcel.ExcelReadListener;
import com.zeroone.star.project.dto.PageDTO;
import com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage.*;

import com.zeroone.star.project.j7.sysargs.supportinfo.codemanage.CodeApis;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeDetailQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeFormQuery;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.codemanage.CodeImgQuery;
import com.zeroone.star.project.vo.JsonVO;
import com.zeroone.star.project.vo.ResultStatus;
import com.zeroone.star.project.vo.j7.sysargs.supportinfo.codemanage.CodeVO;
import com.zeroone.star.supportinfo.entity.Code;
import com.zeroone.star.supportinfo.service.ICodeService;
import com.zeroone.star.supportinfo.service.impl.CodeStructMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类名：CodeController
 * 包名：com.zeroone.star
 * 描述：
 * 作者：su,star
 * 创建日期：2025/10/18
 * 版本号：V1.0
 */
@RestController
@RequestMapping("/code")
@Api(tags = "条码管理")
public class CodeController implements CodeApis {

    @Resource
    ICodeService codeService;

    @Resource
    private EasyExcelComponent excle;

    @Resource
    private CodeStructMapper codeStructMapper;

    /**
     *
     * @param codeDTO
     * @return
     */
    @ApiOperation("新增条码")
    @PostMapping("/addCode")
    public JsonVO<String> addCode(@Validated  @RequestBody CodeDTO codeDTO) {
        String res=codeService.saveCode(codeDTO);
        if(res==null){
            return JsonVO.fail("新增失败");
        }
        return JsonVO.success(res);
    }

    /**
     *
     * @param codeTO
     * @return
     */
    @ApiOperation(("修改指定条码"))
    @PutMapping ("/updateCode")
    public JsonVO<String> modifyCode(@Validated @RequestBody UpdateCodeDTO codeTO) {
        String res=codeService.updateCode(codeTO);
        if(res==null){
            return JsonVO.fail("更新记录为0");
        }
        return JsonVO.success( res);
    }

    /**
     *
     * @param ids
     * @return
     */
    @ApiOperation("删除指定条码（支持批量）")
    @DeleteMapping ("/deleteCodes")
    public JsonVO<List<String>> removeCodes(@ApiParam(
            value = "条码id列表（多个ID用逗号分隔）",
            required = true,
            example = "1,2"
    ) @RequestBody List<String> ids) {
        List<String> res= null;

            res = codeService.removeCodes(ids);

        if(res==null){
           return JsonVO.create(null, ResultStatus.FAIL.getCode(),"无可操作记录");
        }
        return JsonVO.success(res);
    }




    @GetMapping("/getCodeList")
    @ApiOperation(value = "获取条码列表")
    @Override
    public JsonVO<PageDTO<CodeListDTO>> getCodeList(@Validated CodeFormQuery query) {
        try {
            // 调用服务层获取条码列表
            Page<Code> pageResult = codeService.queryCodeList(query);

            // 获取实际的记录列表
            List<Code> records = pageResult.getRecords();

            // 检查记录列表是否为空
            if (records == null) {
                records = new ArrayList<>();
            }

            // 使用MapStruct转换数据
            List<CodeListDTO> codeListDtos = records.stream()
                    .map(codeStructMapper::toCodeListDTO)
                    .collect(Collectors.toList());

            // 确保总记录数不为0 - 使用records.size()作为备选方案
            long totalRecords = pageResult.getTotal();
            if (totalRecords <= 0 && !records.isEmpty()) {
                totalRecords = records.size();
            }

            // 创建PageDTO，使用正确的构造方式
            PageDTO<CodeListDTO> pageDTO = new PageDTO<>();
            pageDTO.setRows(codeListDtos);
            pageDTO.setPageIndex(pageResult.getCurrent());
            pageDTO.setPageSize(pageResult.getSize());
            pageDTO.setTotal(totalRecords);
            pageDTO.setPages(pageResult.getPages());

            return JsonVO.success(pageDTO);
        } catch (RuntimeException e) {
            // 捕获服务层抛出的业务异常，返回具体的错误信息
            return JsonVO.fail((PageDTO<CodeListDTO>) null);
        } catch (Exception e) {
            // 处理其他未预期异常
            return JsonVO.fail((PageDTO<CodeListDTO>) null);
        }
    }

    @GetMapping("/getCodeDetail")
    @ApiOperation(value = "获取指定条码详情")
    @Override
    public JsonVO<CodeDetailDTO> getCodeDetail(@Validated CodeDetailQuery query) {
        try {
            // 调用服务层获取条码详情
            Code code = codeService.getCodeById(query.getId());

            // 检查是否找到条码
            if (code == null) {
                return JsonVO.fail((CodeDetailDTO) null);
            }

            // 使用MapStruct转换为DTO
            CodeDetailDTO codeDetailDTO = codeStructMapper.toCodeDetailDTO(code);

            // 检查转换结果
            if (codeDetailDTO == null) {
                return JsonVO.fail((CodeDetailDTO) null);
            }

            // 返回成功响应
            return JsonVO.success(codeDetailDTO);
        } catch (Exception e) {
            return JsonVO.fail((CodeDetailDTO) null);
        }
    }

    @GetMapping("/getCodeImage")
    @ApiOperation(value = "获取指定条码图片")
    @Override
    public JsonVO<String> getCodeImage(@Validated CodeImgQuery query) {
        try {
            // 直接使用默认值300x300，只传入id参数
            String base64Image = codeService.generateCodeImage(query.getId(), 300, 300);

            return JsonVO.success(base64Image);
        } catch (RuntimeException e) {
            // 处理服务层抛出的业务异常，返回具体的错误信息
            return JsonVO.create(null, ResultStatus.FAIL.getCode(), e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期异常
            return JsonVO.create(null, ResultStatus.SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 导入数据
     * @param file 导入数据的路径
     * @return
     */
    @SneakyThrows
    @ApiOperation("导入数据")
    @PostMapping("/import")
    public JsonVO<List<CodeVO>> importCode(@ApiParam(value = "条码信息列表文件",required = true) MultipartFile file){
//        解析excel
        ExcelReadListener<ImportCodeDTO> listener = new ExcelReadListener<>();
        EasyExcel.read(file.getInputStream(), ImportCodeDTO.class, listener).sheet().doRead();
//        解析excle问题
        List<ImportCodeDTO> importCodes = listener.getDataList();

        List<CodeVO> iCodes = new ArrayList<>();
        List<Code> codes=new ArrayList<>();
        for (ImportCodeDTO importCod:importCodes){
            CodeVO isCode = new CodeVO();
            BeanUtils.copyProperties(importCod,isCode);
            isCode.setType(importCod.getType().equals("二维码")?1:0);
            iCodes.add(isCode);

            Code code = new Code();
            BeanUtils.copyProperties(importCod,code);
            code.setType(importCod.getType().equals("二维码")?1:0);
            codes.add(code);
        }
        codeService.saveBatch(codes);

        return JsonVO.success(iCodes);
    }
    /**
     * 导出exlce
     * @param codeList 导出数据
     * @return
     */
    @SneakyThrows
    @ApiOperation("导出数据")
    @PostMapping("/export")
    public void exportCode(@ApiParam(value = "导出条码列表数据")@RequestBody   List<ExportCodeDTO> codeList){

        // 从上下文获取当前请求的 response
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        String fileName = URLEncoder.encode("导出数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), ExportCodeDTO.class)
                .sheet("导出数据")
                .doWrite(codeList);
//        返回下载exlce路径
        excle.export("导出数据",response.getOutputStream(),ExportCodeDTO.class,codeList);

    }

}
