package com.zeroone.star.supportinfo.controller;



import com.zeroone.star.project.dto.j7.sysargs.supportinfo.attachment.AnnexDTO;

import com.zeroone.star.supportinfo.entity.Annex;
import com.zeroone.star.supportinfo.service.IAnnexService;
import com.zeroone.star.project.components.fastdfs.FastDfsClientComponent;
import com.zeroone.star.project.components.fastdfs.FastDfsFileInfo;
import com.zeroone.star.project.dto.PageDTO;

import com.zeroone.star.project.j7.sysargs.supportinfo.attachment.AttachApis;
import com.zeroone.star.project.query.j7.sysargs.supportinfo.attachment.AnnexQuery;
import com.zeroone.star.project.vo.JsonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;


/**
 * 类名：AnnexController

 * <p>
 * 附件 前端控制器
 * </p>
 *
 * @author kai
 * @since 2025-10-23
 */
@RestController
@RequestMapping("/attachment")
@Api(tags="附件管理")
public class AnnexController implements AttachApis {

    @Resource
    private IAnnexService annexService;
    @Resource
    private FastDfsClientComponent dfs;

    @Value("${fastdfs.nginx-servers}")
    private String fileServerUrl;

    @GetMapping("/list")
    @ApiOperation(value = "获取附件列表（条件+分页）")
    @Override
    public JsonVO<PageDTO<AnnexDTO>> listByPage(@Validated AnnexQuery query) {
        return JsonVO.success(annexService.listByPage(query));
    }



    @SneakyThrows
    @PostMapping("/add")
    @ApiOperation("上传附件")
    @Override
    public JsonVO<String> uploadAttachment(@ApiParam(value = "上传的文件",required = true) MultipartFile file) {
        //获取文件后缀名
        String filename = file.getOriginalFilename();
        assert filename != null;
        String extName = filename.substring(filename.lastIndexOf(".")+1);
        //上传文件
        FastDfsFileInfo info = dfs.uploadFile(file.getBytes(), extName);
        if(info!=null){
            annexService.saveAttachment(file,info,fileServerUrl);
            return JsonVO.success(dfs.fetchUrl(info,fileServerUrl,true));
        }
        return JsonVO.fail("上传失败");

    }

    @SneakyThrows
    @PutMapping("/update")
    @ApiOperation("修改附件")
    public JsonVO<String> updateAnnex(@Validated @RequestBody AnnexDTO annexDTO) {
        if(annexService.update(annexDTO)){
            return JsonVO.success(annexDTO.getId());
        }else return JsonVO.fail("修改失败");

    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @ApiOperation("删除附件")
    public JsonVO<String> deleteAnnex(@PathVariable
                                      @ApiParam(value = "附件ID", required = true, example = "123456")
                                          @Validated @NotBlank(message = "附件id不能为空")
                                      String id) {
        Annex annex = annexService.getById(id);
        if (annex == null) return JsonVO.fail("删除失败");
        String savePath = annex.getSavePath();
        if (savePath != null && savePath.contains("/")) {
            String[] parts = savePath.split("/");
            // 拼接 group 之后的所有部分作为 storageId
            StringBuilder storageIdBuilder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                if (i > 1) {
                    storageIdBuilder.append("/"); // 拼接分隔符
                }
                storageIdBuilder.append(parts[i]);
            }
            String storageId = storageIdBuilder.toString();

            // 调用组件删除
            FastDfsFileInfo fileInfo = FastDfsFileInfo.builder()
                    .group(parts[0])
                    .storageId(storageId)
                    .build();
            try {
                dfs.deleteFile(fileInfo); // 此时参数正确
                annexService.removeById(id); // 数据库删除
                return JsonVO.success(id);
            } catch (Exception e) {
                e.printStackTrace();
                return JsonVO.fail("删除文件失败：" + e.getMessage());
            }
        }
        //没有进入if语句
        return JsonVO.fail("文件不存在");
    }

}

