package com.zeroone.star.project.dto.j6.basic_information.product_management;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 杨潇, nick8311370879
 * @version 1.0
 * @date:
 */
@Data
@ApiModel(description = "商品导入结果")
public class GoodsImportResultDTO {

    @ApiModelProperty(value = "导入结果", required = true, example = "成功")
    private boolean result;

    @ApiModelProperty(value = "返回消息", required = true, example = "成功导入10行数据")
    private String message;

    @ApiModelProperty(value = "成功导入数量", example = "5")
    private Integer successCount;

    @ApiModelProperty(value = "出错行号", example = "3")
    private Integer failCount;

    /**
     * 成功时的构造方法
     * 
     * @param count 成功导入数量
     * @return 导入结果DTO
     */
    @ApiModelProperty(hidden = true)
    public GoodsImportResultDTO success(int count) {
        GoodsImportResultDTO result = new GoodsImportResultDTO();
        result.setResult(true);
        result.setMessage("导入成功");
        result.setSuccessCount(count);
        return result;
    }

    /**
     * 失败时的构造方法
     * 
     * @param row    出错行号
     * @param reason 失败原因
     * @return 导入结果DTO
     */
    @ApiModelProperty(hidden = true)
    public GoodsImportResultDTO fail(int row, String reason) {
        GoodsImportResultDTO result = new GoodsImportResultDTO();
        result.setResult(false);
        result.setMessage("导入失败：" + reason);
        result.setFailCount(row);
        return result;
    }
}
