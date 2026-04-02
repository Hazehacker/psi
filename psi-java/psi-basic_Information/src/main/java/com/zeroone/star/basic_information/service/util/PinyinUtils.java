package com.zeroone.star.basic_information.service.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.stereotype.Component;

/**
 * 简化版拼音转换工具类
 */
@Component
public class PinyinUtils {

    /**
     * 将中文转换为拼音首字母（小写）
     * @param text 输入文本
     * @return 拼音首字母小写
     */
    public static String getPinyinInitials(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        char[] chars = text.trim().toCharArray();

        for (char c : chars) {
            // 判断是否为中文字符
            if (isChineseCharacter(c)) {
                // 使用pinyin4j转换拼音
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    // 取第一个拼音的首字母
                    result.append(pinyinArray[0].charAt(0));
                } else {
                    // 转换失败，保留原字符
                    result.append(c);
                }
            } else {
                // 非中文字符直接保留
                result.append(c);
            }
        }

        return result.toString().toLowerCase();
    }

    /**
     * 简单判断是否为中文字符
     */
    private static boolean isChineseCharacter(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A;
    }
}