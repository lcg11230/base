package com.lcg.base.utils;

import com.lcg.base.surpport.Style;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by johnny on 2019/5/8.
 * 字符转化工具类
 *
 * @author johnny
 */
public class StringConvertUtils {

    public StringConvertUtils() {
    }

    /**
     * 根据格式转化字符串
     *
     * @param str   字符串
     * @param style Style（格式)枚举
     * @return 转化格式后的字符串
     */
    public static String convertByStyle(String str, Style style) {
        switch (style) {
            case camelhump:
                return camelhumpToUnderline(str);
            case uppercase:
                return str.toUpperCase();
            case lowercase:
                return str.toLowerCase();
            case camelhumpAndLowercase:
                return camelhumpToUnderline(str).toLowerCase();
            case camelhumpAndUppercase:
                return camelhumpToUnderline(str).toUpperCase();
            case normal:
            default:
                return str;
        }
    }

    /**
     * 驼峰命名方式 转化为  有下划线的命名方式
     * 例：userName->user_name
     *
     * @param str 字符串
     * @return 转化格式后的字符串
     */
    public static String camelhumpToUnderline(String str) {
        int size;
        char[] chars;
        StringBuilder sb = new StringBuilder((size = (chars = str.toCharArray()).length) * 3 / 2 + 1);

        for (int i = 0; i < size; ++i) {
            char c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(toLowerAscii(c));
            } else {
                sb.append(c);
            }
        }

        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    /**
     * 有下划线的命名方式 转化为 驼峰命名方式
     * 例：user_name->userName
     *
     * @param str 字符串
     * @return 转化格式后的字符串
     */
    public static String underlineToCamelhump(String str) {
        //正则匹配
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);

        StringBuilder builder = new StringBuilder(str);


        for (int i = 0; matcher.find(); ++i) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }

        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }

        return builder.toString();
    }


    /**
     * 判断字符是否在大写字母范围内
     *
     * @param c
     * @return
     */
    public static boolean isUppercaseAlpha(char c) {
        //当char型字符参与计算时，均会将char型数据准换成int型
        return c >= 'A' && c <= 'Z';
    }

    /**
     * 判断字符是否在小写字母范围内
     *
     * @param c
     * @return
     */
    public static boolean isLowercaseAlpha(char c) {
        //当char型字符参与计算时，均会将char型数据准换成int型
        return c >= 'a' && c <= 'z';
    }

    /**
     * 根据ASCII编码转化为大写字母
     *
     * @param c 字符
     * @return 该字符大写字母
     */
    public static char toUpperAscii(char c) {

        if (isLowercaseAlpha(c)) {
            //如果是小写 减去32得到该字母大写的ASCII编码
            c = (char) (c - 32);
        }

        return c;
    }

    /**
     * 根据ASCII编码转化为小写字母
     *
     * @param c 字符
     * @return 该字符小写字母
     */
    public static char toLowerAscii(char c) {
        if (isUppercaseAlpha(c)) {
            //如果是大写 加上32得到该字母小写的ASCII编码
            c = (char) (c + 32);
        }

        return c;
    }
}
