package com.zeroone.star.purchase.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Small parsing helpers extracted from service.
 */
public final class ParseUtils {
    private ParseUtils() {}

    // 日期时间格式常量（预定义避免重复创建）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static final DateTimeFormatter DATETIME_FORMATTER_WITH_SECONDS = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER_WITHOUT_SECONDS = DateTimeFormatter.ofPattern("yyyy-M-d H:mm");
    
    // 正则表达式常量（预编译提升性能）
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}$");
    private static final Pattern DATETIME_PATTERN = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{2}(:\\d{2})?$");

    /**
     * 去除字符串首尾空格，若结果为空则返回null
     */
    public static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 截断字符串至指定最大长度（超过则取前max个字符）
     */
    public static String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        // 用Math.min避免长度判断，逻辑更简洁
        int length = Math.min(s.length(), max);
        return s.substring(0, length);
    }

    /**
     * 支持带逗号、￥符号、百分号的数字字符串解析，解析失败返回 BigDecimal.ZERO
     */
    public static BigDecimal parseToBigDecimalSafe(String s) {
        if (!StringUtils.hasText(s)) {
            return BigDecimal.ZERO;
        }
        try {
            // 提取字符串清理逻辑为私有方法，减少主方法冗余
            String cleanedStr = cleanNumberString(s);
            return cleanedStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(cleanedStr);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 解析百分比或数字（复用parseToBigDecimalSafe逻辑）
     */
    public static BigDecimal parsePercentOrNumber(String s) {
        return parseToBigDecimalSafe(s);
    }

    /**
     * 解析字符串为LocalDateTime（支持纯日期、带时分、带时分秒格式）
     */
    public static LocalDateTime parseToLocalDateTime(String s) {
        if (!StringUtils.hasText(s)) {
            return null;
        }
        String trimmedStr = s.trim();
        
        // 分步解析：先尝试纯日期，再尝试带时间，最后尝试默认格式
        if (DATE_PATTERN.matcher(trimmedStr).matches()) {
            return parseAsDate(trimmedStr);
        }
        if (DATETIME_PATTERN.matcher(trimmedStr).matches()) {
            return parseAsDateTime(trimmedStr);
        }
        return parseAsDefaultFormat(trimmedStr);
    }

    /**
     * 清理数字字符串中的干扰字符（逗号、人民币符号、百分号等）
     */
    private static String cleanNumberString(String s) {
        // 移除逗号和人民币符号
        String cleaned = s.replace(",", "")
                         .replace("￥", "")
                         .replace("¥", "")
                         .trim();
        // 移除末尾的百分号
        if (cleaned.endsWith("%")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1).trim();
        }
        return cleaned;
    }

    /**
     * 解析纯日期字符串（转换为当天起始时间）
     */
    private static LocalDateTime parseAsDate(String trimmedStr) {
        try {
            // 统一替换为"-"格式，兼容"/"分隔符
            String normalizedDate = trimmedStr.replace("/", "-");
            LocalDate date = LocalDate.parse(normalizedDate, DATE_FORMATTER);
            return date.atStartOfDay();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析带时间的字符串（区分是否包含秒）
     */
    private static LocalDateTime parseAsDateTime(String trimmedStr) {
        try {
            // 根据字符串长度判断是否包含秒（简化逻辑）
            DateTimeFormatter formatter = trimmedStr.length() > 16 
                ? DATETIME_FORMATTER_WITH_SECONDS 
                : DATETIME_FORMATTER_WITHOUT_SECONDS;
            return LocalDateTime.parse(trimmedStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试默认格式解析（ISO标准格式等）
     */
    private static LocalDateTime parseAsDefaultFormat(String trimmedStr) {
        try {
            // 先尝试直接解析为LocalDateTime
            return LocalDateTime.parse(trimmedStr);
        } catch (Exception e) {
            // 失败则尝试解析为LocalDate再转为起始时间
            try {
                LocalDate date = LocalDate.parse(trimmedStr, DateTimeFormatter.ISO_LOCAL_DATE);
                return date.atStartOfDay();
            } catch (Exception ex) {
                return null;
            }
        }
    }
}