package com.wk.xin.third.api;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * httpClients发送get和post请求（HTTPs)
 * ref: https://blog.csdn.net/MaBanSheng/article/details/102921660
 */
public class WeixinHttpClientUtils {
    private static final PoolingHttpClientConnectionManager CONN_MGR;
    //客户端请求的默认设置
    private static final RequestConfig REQUEST_CONFIG;
    private static final int MAX_TIMEOUT = 3000;

    static {
        // 连接池
        CONN_MGR = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        CONN_MGR.setMaxTotal(50);
        CONN_MGR.setDefaultMaxPerRoute(CONN_MGR.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        //configBuilder.setRedirectsEnabled(false);
        //configBuilder.setCookieSpec(CookieSpecs.STANDARD_STRICT);

        REQUEST_CONFIG = configBuilder.build();
    }

    /**
     * 使用HttpClient发送get请求
     *
     * @param url
     * @param ssl
     * @return
     */
    public static String doGet(String url, boolean ssl) {
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = null;
        // 2. 创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        String result = "";
        try {
            if (ssl) {
                httpClient =
                    HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(
                            CONN_MGR)
                        .setDefaultRequestConfig(REQUEST_CONFIG).build();
            } else {
                httpClient = HttpClients.createDefault();
            }
            // 请求设置
            httpGet.setConfig(REQUEST_CONFIG);
            // 执行GET请求
            response = httpClient.execute(httpGet);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 处理响应实体
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param obj    json对象
     * @return
     */
    public static String doPost(String apiUrl, Object obj, Boolean ssl) {
        CloseableHttpClient httpClient = null;
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        try {
            if (ssl) {
                httpClient =
                    HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(
                            CONN_MGR)
                        .setDefaultRequestConfig(REQUEST_CONFIG).build();
            } else {
                httpClient = HttpClients.createDefault();
            }
            // 设置代理 线上需去除代理配置
            HttpHost proxy = new HttpHost("10.5.3.9", 80);
            RequestConfig requestConfig =
                RequestConfig.custom().setProxy(proxy).setConnectTimeout(2000).setSocketTimeout(3000)
                    .setConnectionRequestTimeout(3000).build();
            httpPost.setConfig(requestConfig);
            // 构造消息头
            //httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            //httpPost.setHeader("Connection", "Close");

            //处理入参

            JSONObject jsonObject = JSONObject.fromObject(obj);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                return null;
            }
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            //使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // 信任所有
                    return true;
                }
            }).build();

            //NoopHostnameVerifier类:  作为主机名验证工具，实质上关闭了主机名验证，它接受任何有效的SSL会话并匹配到目标主机。
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            sslsf = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1"}, null, hostnameVerifier);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }
}
