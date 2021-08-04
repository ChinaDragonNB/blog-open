package com.ak47007.utils;

import cn.hutool.core.util.StrUtil;
import com.ak47007.constant.SortConstant;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AK47007
 * @date 2019/10/16
 * describe: ElementUI 表格字段排序
 */
public class ElementUISortUtil {


    /**
     * 空字符串
     */
    private static final String STRING_EMPTY = "";

    /**
     * 斜杠
     */
    private static final String STRING_SLASH = "/";

    /**
     * 反斜杠
     */
    private static final String STRING_BACKSLASH = "\\\\";

    /**
     * 下划线
     */
    private static final String STRING_UNDERLINE = "_";

    /**
     * 百分号
     */
    private static final String STRING_PERCENT = "%";

    /**
     * 单引号
     */
    private static final String STRING_SINGLE_QUOTATION = "'";

    /**
     * 两个单引号
     */
    private static final String STRING_SINGLE_QUOTATIONS = "''";


    /**
     * mybatis特殊字符过滤
     *
     * @param text 过滤的字符
     * @return 过滤好的字符
     */
    public static String mybatisFilter(String text) {
        if (StrUtil.isNotBlank(text)) {
            text = text.replaceAll(STRING_SLASH, STRING_SLASH + STRING_SLASH);
            text = text.replaceAll(STRING_BACKSLASH, STRING_SLASH + STRING_BACKSLASH);
            text = text.replaceAll(STRING_UNDERLINE, STRING_SLASH + STRING_UNDERLINE);
            text = text.replaceAll(STRING_PERCENT, STRING_SLASH + STRING_PERCENT);
            text = text.replaceAll(STRING_SINGLE_QUOTATION, STRING_SLASH + STRING_SINGLE_QUOTATIONS);
        } else {
            text = "";
        }
        return text;
    }


    /**
     * SQL排序
     *
     * @param columnName 排序的列名
     * @param order      排序方式
     * @param defaultSql 默认排序SQL
     * @return 返回已经解析好的SQL
     */
    public static String sortSql(String columnName, String order, String defaultSql) {
        StringBuilder stringBuilder = new StringBuilder(" ORDER BY ");
        if (StrUtil.isNotBlank(columnName) && StrUtil.isNotBlank(order)) {
            switch (order) {
                case SortConstant.ASC:
                    order = "ASC";
                    break;
                case SortConstant.DESC:
                    order = "DESC";
                    break;
            }
            stringBuilder.append(columnName).append(" ").append(order);
        } else {
            stringBuilder.append(defaultSql);
        }
        return stringBuilder.toString();
    }


    /**
     * 从字符串中获取网址
     *
     * @param reg 正则表达式
     * @param str 字符串
     * @return 网址集合
     */
    public static List<String> getUrlByString(String reg, String str) {
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            urls.add(matcher.group());
            str = str.substring(matcher.end());
            matcher = pattern.matcher(str);
        }
        return urls;
    }

}
