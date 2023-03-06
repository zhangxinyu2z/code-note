package com.wk.xin.util.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : chenlinyan
 * @version : 2.0
 * @date : 2019/9/27 9:40
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 根据url获取输入流
     *
     * @param url
     * @return
     */
    public static InputStream doGet(String url) {
        InputStream inputStream;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            HttpResponse response = null;

            response = httpClient.execute(get);
            //获取网络io流
            inputStream = response.getEntity().getContent();
            return inputStream;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doGet(String url, Map<String, String> headerMap) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        HttpResponse response = null;

        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        response = httpClient.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        httpClient.close();
        return content;
    }

    /**
     * post by json or xml
     *
     * @param url     url
     * @param params  params
     * @param headers headers
     * @return json
     */
    public static String doPost(String url, String params, BasicHeader... headers) {
        return doPost(url, params, null, HttpConstant.APPLICATION_JSON, headers);
    }

    /**
     * post by form
     *
     * @param url     url
     * @param params  params
     * @param headers headers
     * @return json
     */
    public static String doPost(String url, Map<String, String> params, BasicHeader... headers) {
        return doPost(url, null, params,  HttpConstant.APPLICATION_FORM, headers);
    }

    /**
     * 通过post方式调用http接口
     *
     * @param url         url路径
     * @param params      参数
     * @param contentType 参数类型
     */
    public static String doPost(String url, String params, Map<String, String> map, String contentType,
        BasicHeader... headers) {

        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        //请求API接口的响应时间
        long endTime = 0L;

        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        StringEntity entity;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();
            //httpClient = HttpClientBuilder.create().build();
            // 设置请求头和报文
            httpPost = HttpClientFactory.getInstance().httpPost(url);

            if (headers != null) {
                httpPost.setHeaders(headers);
            }
            //Header header = new BasicHeader("Accept-Encoding", null);
            //httpPost.setHeader(header);

            RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
                .setConnectionRequestTimeout(15000).build();

            httpPost.setConfig(defaultRequestConfig);

            // 设置报文和通讯格式
            if (HttpConstant.APPLICATION_FORM.equals(contentType)) {
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String, String> elem : map.entrySet()) {
                    list.add(new BasicNameValuePair(elem.getKey(),
                        null == elem.getValue() ? null : elem.getValue().toString()));
                }
                entity = new UrlEncodedFormEntity(list, HttpConstant.UTF8_ENCODE);
            } else {
                // json 或 text/xml
                entity = new StringEntity(params, HttpConstant.UTF8_ENCODE);
            }
            entity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            entity.setContentType(contentType);

            httpPost.setEntity(entity);
            logger.info("请求{}接口的参数为{}", url, params);

            //执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            logger.error("请求{}接口出现异常", url, e);
            int reSend = HttpConstant.REQ_TIMES;
            // 异常的可能:超时
            while (reSend > 0) {
                logger.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                //result = doPost(url, params, map, contentType, headers);
                try {
                    httpResponse = httpClient.execute(httpPost);
                    httpEntity = httpResponse.getEntity();
                    result = EntityUtils.toString(httpEntity);

                    if (result != null && !"".equals(result)) {
                        break;
                    }
                    reSend--;
                } catch (Exception ignored) {

                }
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                logger.error("http请求释放资源异常", e);
            }
        }
        //请求接口的响应时间
        endTime = System.currentTimeMillis();
        logger.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;
    }

    /**
     * post  by json  可发送文件
     * @param url
     * @param jsonParam
     * @param headersMap
     * @param multipartFiles 文件数组
     * @return
     * @throws Exception
     */
    public static String doPost(String url, JSONObject jsonParam, Map<String, String> headersMap,
        MultipartFile[] multipartFiles) throws Exception {
        //post请求返回结果
        HttpResponse response;
        //返回的字符串
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost method = new HttpPost(url);
        //设置header参数
        setPostHeader(method, headersMap);
        try {
            if (null != jsonParam) {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                if (null != multipartFiles && multipartFiles.length != 0) {
                    for (MultipartFile multipartFile : multipartFiles) {
                        // 文件流
                        builder.addBinaryBody("file", multipartFile.getInputStream(),
                            ContentType.MULTIPART_FORM_DATA, multipartFile.getOriginalFilename());

                    }
                }
                builder.setCharset(Charset.forName("utf-8"));
                builder.setMode(HttpMultipartMode.RFC6532);
                StringBody jsonStr = new StringBody(jsonParam.toString(), ContentType.APPLICATION_JSON);
                HttpEntity entity = builder.addPart("jsonStr", jsonStr).build();
                method.setEntity(entity);
            }
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(15000)
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(15000)
                .build();

            method.setConfig(defaultRequestConfig);

            response = httpClient.execute(method);
            //获取结果实体
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        } catch (ConnectTimeoutException e) {
            //超时后再去获取一次 还有问题只能通过页面刷新
            response = httpClient.execute(method);
            //获取结果实体
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
        }
        return result;
    }

    /**
     * 设置请求头
     *
     * @param method     请求类型
     * @param headersMap 参数
     */
    private static void setPostHeader(HttpPost method, Map<String, String> headersMap) {
        if (null != headersMap && headersMap.size() > 0) {
            for (String key : headersMap.keySet()) {
                String value = headersMap.get(key);
                Header header = new BasicHeader(key, value);
                method.addHeader(header);
            }
        }
    }

}
