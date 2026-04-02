package com.zeroone.star.project.dto.j7.sysargs.supportinfo.goodscategory;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("商品结构树节点")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryTreeNodeDTO implements Serializable {

    @NotBlank(message = "类型编号不能为空")
    @ApiModelProperty(value = "商品类型id",example = "0")
    private String id;

    @NotBlank(message = "类型名称不能为空")
    @ApiModelProperty(value = "商品名称",example = "饮料酒水")
    private String name;

    private Integer sort;

    @NotNull(message = "所属类别不能为空")
    @ApiModelProperty(value = "商品父类id",example = "0")
    private String pid;

    @NotBlank(message = "备注信息不能为空")
    private String data;

    @NotNull(message = "子元素")
    private List<CategoryTreeNodeDTO> tnChildren;

    public void addChild(CategoryTreeNodeDTO child) {
        // 判断子节点集合是否为空
        if (tnChildren == null) {
            tnChildren = new ArrayList<>();
        }
        // 添加子节点
        tnChildren.add(child);
    }

    @Override
    public String toString() {
        return "CategoryTreeNodeDTO{" +
                "name='" + name + '\'' +
                ", pid=" + pid +
                ", data='" + data + '\'' +
                ", child="+ tnChildren +
                '}';
    }
}
