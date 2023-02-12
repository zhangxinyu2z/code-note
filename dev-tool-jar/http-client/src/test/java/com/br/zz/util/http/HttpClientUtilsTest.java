package com.br.zz.util.http;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author xinyu.zhang
 * @since 2023/2/10 11:52
 */
public class HttpClientUtilsTest {

    @Test
    public void testGet() {
        // from test nacos balde-supplier  inquiry.verfiy.purchaser.by.email
        String meoDataFactoryVerifyEmailUrl = "http://10.21.64.21:9098/service/factorydata/queryByEmail";

        // 业务参数
        HashMap<String, String> params = new HashMap<>();
        params.put("email", "rizwan.shaikh@danubehome.com");
        String s = HttpClientUtils.get(meoDataFactoryVerifyEmailUrl, params, HttpClientUtils.CHARSET_UTF8,
            HttpClientUtils.CHARSET_UTF8);
        System.out.println(s);
    }
}