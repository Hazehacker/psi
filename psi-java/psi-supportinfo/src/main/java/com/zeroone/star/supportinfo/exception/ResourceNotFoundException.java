package com.zeroone.star.supportinfo.exception;

/**
 * 自定义业务异常：资源不存在（如查询/修改/删除的ID不存在）
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}