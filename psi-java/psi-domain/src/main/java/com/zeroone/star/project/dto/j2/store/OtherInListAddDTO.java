package com.zeroone.star.project.dto.j2.store;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @BelongsProject: psi-java
 * @BelongsPackage: com.zeroone.star.project.dto.j2.store
 * @Author: 高
 * @CreateTime: 2025-10-18 18:18
 * @Description: 其他入库单数据对象
 * @Version: 1.0
 */
@Data
@ApiModel("OtherInListAddDTO")
public class OtherInListAddDTO {

    @ExcelProperty(value = "供应商", index = 1)
    @ApiModelProperty(value = "供应商",required = false, example = "0")
    private String supplier;

    @ExcelProperty(value = "所属组织", index = 0)
    @ApiModelProperty(value = "所属组织", example = "0")
    private String frame;

    @ExcelProperty(value = "单据日期", index = 3)
    @ApiModelProperty(value = "单据日期",required = true, example = "2023-10-18 18:18:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @ExcelProperty(value = "单据编号", index  =4)
    @ApiModelProperty(value = "单据编号",required = true, example = "QTRKD2510151628275")
    private String number;

    @ExcelProperty(value = "单据类型", index = 2)
    @ApiModelProperty(value = "单据类型[0:其它入库单|1:盘盈单]",required = true, example = "0")
    private Integer type;

    @ExcelProperty(value = "单据成本", index = 5)
    @ApiModelProperty(value = "单据成本",required = true, example = "1000.0000")
    private BigDecimal total;

    @ExcelProperty(value = "单据费用", index = 6)
    @ApiModelProperty(value = "单据费用",required = true, example = "50.0000")
    private BigDecimal cost;

    @ExcelProperty(value = "关联人员", index = 7)
    @ApiModelProperty(value = "关联人员",required = false, example = "0")
    private String people;

    @ApiModelProperty(value = "物流信息",required = false, example = "{\"key\":\"auto\",\"name\":\"自动识别\",\"number\":\"\"}")
    private LogisticsDTO logistics;

    @ApiModelProperty(value = "单据附件", required = false, example = "")
    private List<FileDTO> file;

    @ExcelProperty(value = "备注信息", index = 12)
    @ApiModelProperty(value = "备注信息", required = false,example = "紧急订单，请优先处理")
    private String data;

    @ApiModelProperty(value = "扩展信息", example = "111")
    private Object more;

    @ExcelProperty(value = "审核状态", index = 8)
    @ApiModelProperty(value = "审核状态[0:未审核|1:已审核]",required = true, example = "0")
    private Integer examine;

    @ExcelProperty(value = "费用状态", index = 9)
    @ApiModelProperty(value = "费用状态[0:未结算|1:部分结算|2:已结算|3:无需结算]",required = true, example = "3")
    private Integer cse;

    @ExcelProperty(value = "核对状态", index = 10)
    @ApiModelProperty(value = "核对状态[0:未核对|1:已核对]",required = true, example = "0")
    private Integer check;

    @ExcelProperty(value = "制单人", index = 11)
    @ApiModelProperty(value = "制单人",required = true, example = "1")
    private String user;

    @ApiModelProperty(value = "入库单详细信息列表",required = true)
    List<OtherInListDetailInfoDTO> otherInListDetailInfoDTOList;

    @ApiModelProperty(value = "单据费用列表",required = true, example = "{id: 7, type: \"entry\", class: 4, time: \"2025-10-15\", iet: 5, money: 111, data: \"\", settle: 0, state: 0}")
    private List<CostDTO> costDTOList;
}
