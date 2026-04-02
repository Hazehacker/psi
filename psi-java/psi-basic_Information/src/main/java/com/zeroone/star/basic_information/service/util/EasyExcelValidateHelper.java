package com.zeroone.star.basic_information.service.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 杨潇, nick8311370879
 * @version 1.0
 * @date:
 */

public class EasyExcelValidateHelper {
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 校验对象并返回错误信息，如果返回null或空字符串则表示校验通过
     */
    public static <T> String validate(T data) {
        Set<ConstraintViolation<T>> violations = validator.validate(data);
        if (violations.isEmpty()) {
            return null;
        }
        // 将错误信息用逗号拼接
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
    }
}
