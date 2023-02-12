package com.br.zz.helper;

import org.apache.commons.lang.StringUtils;

/**
 * @author xinyu.zhang
 * @since 2023/2/9 18:32
 */
public class DataProcessHelper {
    public static String processData(String s) {

        if (StringUtils.isBlank(s)) {
            return null;
        }
        //去掉html标签
        s = s.replaceAll("<[.[^>]]*>", "");
        return s;
    }
}
