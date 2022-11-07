package com.eltorofuerte.system.rules.demo;

import org.junit.*;
import org.junit.contrib.java.lang.system.SystemOutRule;

/**
 * @author xinyu.zhang
 * @since 2022/11/7 14:34
 */
public class TestCase {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();


    @Test
    public void writesTextToSystemOut() {
        System.out.print("hello world");
        Assert.assertEquals("hello world", systemOutRule.getLog());
    }
}
