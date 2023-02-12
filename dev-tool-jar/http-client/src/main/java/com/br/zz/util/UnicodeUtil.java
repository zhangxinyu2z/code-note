package com.br.zz.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 在线UNICODE转换：https://c.runoob.com/front-end/3602/
 *
 * @author xinyu.zhang
 * @since 2023/2/9 17:11
 */
public class UnicodeUtil {
    private static final Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    /**
     * unicode编码 将中文字符转换成Unicode字符
     */
    public static String unicodeEncode(String str) {
        if(str == null) {
            return null;
        }
        char[] utfBytes = str.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 对输入的字符串进行URL编码, 即转换为%20这种形式
     *
     * httpClients，输入的浏览器地址使用URI.create(uri)，不支持特殊字符%%$
     *
     * @param str 原文
     * @return URL编码. 如果编码失败, 则返回原文
     */
    public static String urlEncode(String str) {
        try {
            if(str == null) {
                return null;
            }
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * unicode解码 将Unicode的编码转换为中文
     */
    public static String unicodeDecode(String str) {
        if(str == null) {
            return null;
        }
        Matcher matcher = PATTERN.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

}
