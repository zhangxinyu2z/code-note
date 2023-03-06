package com.wk.xin.util.http;/*
 * Copyright 2018 Meorient, Inc. All rights reserved.
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 访问远程接口
 *
 * @author charles.chen
 * @created on 2018/5/21.
 */
public class HttpClientHelper {
    private static Logger logger = Logger.getLogger(HttpClientHelper.class);
    private final static String CHARSET = "UTF-8";
    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * 请求服务
     *
     * @param url    服务地址
     * @param params 请求参数 json字符串
     * @return
     */
    public static String doPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        logger.debug("params:" + params);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        //httpPost.setHeader("Connection", "Close");

        if (StringUtils.isNotBlank(params)) {
            // 构建消息实体
            StringEntity stringEntity = new StringEntity(params, CHARSET);
            stringEntity.setContentEncoding(CHARSET);
            // 发送Json格式的数据请求
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
        }

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if ((status >= 200) && (status < 300)) {
                    HttpEntity entity = response.getEntity();
                    return ((entity != null) ? EntityUtils.toString(entity) : null);
                }

                return null;
            }
        };
        String responseBody = null;
        try {
            responseBody = httpClient.execute(httpPost, responseHandler);
            logger.info("responseBody:" + responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    /**
     * get请求
     *
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * get请求
     *
     * @param url     地址
     * @param headers 头
     * @return
     */
    public static String doGet(String url, BasicHeader... headers) {
        HttpGet request = new HttpGet(url);
        if (headers != null) {
            request.setHeaders(headers);
        }

        try {
            // 响应报文
            HttpResponse response = httpClient.execute(request);

            logger.info(response.getStatusLine().getStatusCode());
            logger.debug(url);

            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //获得服务器返回响应报文的主体部分，转为json字符串数据
                return EntityUtils.toString(response.getEntity());
            } else {
                String strResult = EntityUtils.toString(response.getEntity());
                logger.info(strResult);
                logger.info("第三方接口调用失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * post请求(用于key-value格式的参数)
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> params) {

        BufferedReader in = null;
        try {
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<>();
            for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String value = params.get(name);
                nvps.add(new BasicNameValuePair(name, value));
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, CHARSET));

            //request.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(request);

            int code = response.getStatusLine().getStatusCode();
            if (code >= 200) {
                //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), CHARSET));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                return sb.toString();
            } else {   //
                logger.info("状态码：" + code);
                logger.info("第三方接口调用失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * https ssl证书
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            //使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            //NoopHostnameVerifier类:  作为主机名验证工具，实质上关闭了主机名验证，它接受任何有效的SSL会话并匹配到目标主机。
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }

    public static void main(String[] args) {
        BasicHeader basicHeader = new BasicHeader("Authorization", "4ba8e63a-b62c-408f-8c20-362050d36139");
        String url = "http://open.api.tianyancha.com/services/v4/open/searchV2?word=北京百度网讯科技有限公司";
        String s = HttpClientHelper.doGet(url, basicHeader);
        System.out.println(s);
    }
}
