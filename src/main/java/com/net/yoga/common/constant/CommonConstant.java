package com.net.yoga.common.constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author gaort
 * @since 2020/8/2 19:51
 */
public class CommonConstant {

    public static final ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<>(); //保存request的threadlocal
    public static final ThreadLocal<HttpServletResponse> responseTL = new ThreadLocal<>(); //保存response的threadlocal
    public static final ThreadLocal<HttpSession> sessionTL = new ThreadLocal<>(); //保存session的threadlocal

    public final static String SPACE_SPLIT_STR = " ";
    public final static String SPACE_NULL_STR = "";
    public final static String PERCENT_SPLIT_STR = "%";
    public final static String COMMON_SPLIT_STR = "_";
    public final static String COMMON_DASH_STR = "-";
    public final static String COMMA_SPLIT_STR = ",";
    public final static String SEMICOLON_SPLIT_STR = ";";
    public final static String COMMON_VERTICAL_STR = "|";
    public final static String URL_SPLIT_STR = "/";
    public final static String DOUBLE_SLASH_STR = "//";
    public final static String POUND_SPLIT_STR = "#";
    public final static String COMMON_ESCAPE_STR = "\\";
    public final static String COMMON_AT_STR = "@";
    public final static String COMMON_DOLLAR_STR = "$";
    public final static String COMMON_WAVE_STR = "~";
    public final static String COMMON_STAR_STR = "*";
    public final static String COMMON_COLON_STR = ":";
    public final static String COMMON_DOT_STR = ".";
    public final static String COMMON_EQUAL_STR = "=";
    public final static String COMMON_AND_STR = "&";
    public final static String UP_ARROW_STR = "^";
    public final static String COMMON_BRACKET_LEFT = "(";
    public final static String COMMON_BRACKET_RIGHT = ")";
    public final static String COMMON_SQUARE_BRACKET_LEFT = "[";
    public final static String COMMON_SQUARE_BRACKET_RIGHT = "]";
    public final static String DOUBLE_DASH_STR = "--";
    public final static String COMMON_PLUS_STR = "+";
    public final static String COMMON_QUESTION_MARK = "?";
    public final static String LINE_FEED = "\n";
    public final static String COMMON_TAB = "\t";

    //分隔符
    public static final String SEPARATOR_LINE = System.getProperty("line.separator");
    public static final String SEPARATOR_FILE = System.getProperty("file.separator");

    //开关
    public final static String SWITCH_ON = "on";
    public final static String SWITCH_OFF = "off";
    //通用状态
    public final static int STATUS_YES = 1;
    public final static int STATUS_NO = 0;

    // redis
    public final static String REDIS_DEFAULT_CHARSET = "UTF-8";
    public final static String REDIS_DEFAULT_OK = "ok";
    public final static Integer REDIS_DEFAULT_RESULT = 1;
    public final static Long REDIS_DEFAULT_RESULT_LONG = 1L;
    public final static Long REDIS_PREDICT_NUM_RESULT = 10000L;
    public final static Long REDIS_LAST_INDEX = -1L;
    public final static String REDIS_MODE_SINGLE = "single";
    public final static String REDIS_MODE_CLUSTER = "cluster";
}
