package com.zeroone.star.project.dto.j7.sysargs.supportinfo.codemanage;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("导出条码对象")
public class ExportDTO {

    private String path;

    private List<ExportCodeDTO> codeList;
}
