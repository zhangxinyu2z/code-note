package com.wk.xin.util;

import org.junit.Test;

/**
 * @author xinyu.zhang
 * @since 2023/2/10 14:00
 */
public class UnicodeUtilTest {

    @Test
    public void unicodeDecode() {
        String s1  = "2022-06-18T18:30:59+08:00";

        String s2 = "2022-07-16T18:30:57+08:00";

        System.out.println(s1.compareTo(s2));
    }
}