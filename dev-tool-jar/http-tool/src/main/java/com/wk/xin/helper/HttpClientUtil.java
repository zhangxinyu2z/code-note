package com.wk.xin.helper;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author dell
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final RequestConfig REQUEST_CONFIG =
        RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();

    private static PoolingHttpClientConnectionManager cm = null;

    @PostConstruct
    public void init() {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(1000);
        cm.setDefaultMaxPerRoute(1000);
    }

    /**
     * @param isPooled 是否使用连接池
     * @param isRetry  是否定义重试请求
     */
    public static CloseableHttpClient getClient(boolean isPooled, boolean isRetry) {
        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException arg0, int retryTimes, HttpContext arg2) {
                // 重试次数
                if (retryTimes > 2) {
                    return false;
                }
                if (arg0 instanceof UnknownHostException || arg0 instanceof ConnectTimeoutException
                    || !(arg0 instanceof SSLException) || arg0 instanceof NoHttpResponseException) {
                    return true;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(arg2);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                    return true;
                }
                return false;
            }
        };

        if (isPooled) {
            return HttpClients.custom().setConnectionManager(cm).setRetryHandler(handler).build();
        }
        return HttpClients.createDefault();
    }

    public static String doPostWithRequest(String path, HttpServletRequest request) {
        Enumeration params = request.getParameterNames();
        List<NameValuePair> nameValuePairs = Lists.newArrayList();
        while (params.hasMoreElements()) {
            String paramName = (String)params.nextElement();
            nameValuePairs.add(new BasicNameValuePair(paramName, request.getParameter(paramName)));
        }
        HttpPost httpPost = new HttpPost(path);

        httpPost.setConfig(REQUEST_CONFIG);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            return execReq(httpPost);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("do post error: ", e);
        }
        return "";
    }

    public static String doPost(String path, Map<String, Object> params) {
        LOGGER.debug("doPost from " + path, params);
        HttpPost httpPost = new HttpPost(path);

        httpPost.setConfig(REQUEST_CONFIG);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(createParams(params)));

            String bodyAsString = execReq(httpPost);
            if (bodyAsString != null) {
                return bodyAsString;
            }

        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return errorResponse("-2", "unknown error");
    }

    private static String execReq(HttpPost httpPost) {
        try {
            CloseableHttpResponse response = getClient(true, true).execute(httpPost);
            if (response != null) {
                try {
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        return EntityUtils.toString(response.getEntity());
                    } else {
                        return errorResponse(String.valueOf(response.getStatusLine().getStatusCode()),
                            "http post request error: " + httpPost.getURI());
                    }
                } finally {
                    EntityUtils.consume(response.getEntity());
                }
            } else {
                return errorResponse("-1", "response is null");
            }

        } catch (IOException e) {
            LOGGER.error("doPost error: ", e);
        }
        return errorResponse("-3", "unknown error");
    }

    private static String errorResponse(String code, String msg) {
        return JSONUtil.toJsonStr(ImmutableMap.of("code", code, "msg", msg));
    }

    private static List<NameValuePair> createParams(Map<String, Object> params) {
        List<NameValuePair> nameValuePairs = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        return nameValuePairs;
    }

    /**
     * 判断访问目标网站是否需要代理
     */
    private boolean isNeedProxy(String str) {
        boolean result = true;
        URL url;
        try {
            url = new URL("http://apkpure.com/");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000);
            // int i = connection.getResponseCode();
            int i = connection.getContentLength();
            if (i > 0) {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}