package com.zeroone.star.project.vo.j3.capital;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @Author: a
 * @CreateTime: 2025-10-18
 * @Description: 资金管理-添加核销单返回VO
 * @Version: 1.0
 */
@Data
@SuperBuilder
public class VerificationSheetAddVO {
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 添加的核销单id
     */
    private String id;
}
