package com.wk.xin.process;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xinyu.zhang
 * @since 2023/3/6 17:28
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
