package com.zeroone.star.project.vo.j7.sysargs.supportinfo.codemanage;

import lombok.Data;

/**
 * 类名：CodeVO
 * 包名：com.zeroone.star.supportinfo.entity
 * 描述：
 * 作者：hh
 * 创建日期：2025/10/23
 * 版本号：V1.0
 */
@Data
public class CodeVO {


    private String id;

    /**
     * 条码名称
     */
    private String name;

    /**
     * 条码内容
     */
    private String info;

    /**
     * 条码类型[0:条形码 | 1:二维码]
     */
    private Integer type;

    /**
     * 备注信息
     */
    private String data;

}
