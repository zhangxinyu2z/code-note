package com.br.zz.helper;

import com.br.zz.util.Md5Util;

import java.util.Map;
import java.util.TreeMap;

/**
 * 探迹请求规则
 * @author xinyu.zhang
 * @since 2023/2/9 18:55
 */
public class TungeeRegularHelper {

    /**
     * 生成签名
     *
     * @param paramMap  参数Map
     * @param secretKey 加密用的秘钥
     * @return 签名字符串
     */
    public static String signature(Map<String, ?> paramMap, String secretKey) {
        if (!(paramMap instanceof TreeMap)) {
            paramMap = new TreeMap<>(paramMap);
        }
        StringBuilder paramSplice = new StringBuilder();
        paramMap.forEach((key, value) -> paramSplice.append(value));
        paramSplice.append(secretKey);

        //return EncryptHelper.md5(paramSplice.toString()).toLowerCase();
        return Md5Util.md5Encrypt32Lower(paramSplice.toString());
        //return Md5Util.md5(paramSplice.toString()).toLowerCase();
    }

    /**
     * @param url    请求的接口地址
     * @param params 入参map
     * @return
     */
    public static String getUrl(String url, Map<String, ?> params) {
        if (!(params instanceof TreeMap)) {
            params = new TreeMap<>(params);
        }
        StringBuilder paramSplice = new StringBuilder();
        paramSplice.append(url).append("?");
        params.forEach((key, value) -> paramSplice.append(key).append("=").append(value).append("&"));

        return paramSplice.toString();
    }
}
