package com.wk.xin.util.http;

import com.google.gson.Gson;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author xinyu.zhang
 * @since 2023/2/9 17:02
 */
public class OkHttpClientUtils {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtils.class);

    static final OkHttpClient client
        = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build();

    public String postJson(String url, HashMap<String, String> map) throws IOException {

        if (map == null || StringUtils.isEmpty(url)) {
            throw new RuntimeException("url或请求参数不能为空");
        }

        Gson gson = new Gson();
        String json = gson.toJson(map);
        return commExecute(url, json);
    }

    @NotNull
    private String commExecute(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (Objects.isNull(responseBody)) {
                throw new RuntimeException("响应体为空");
            }
            return responseBody.string();
        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
    }

    /**
     * 需要使用序列化工具将对象序列化成字符串：如gson
     *
     * @param url
     * @param json
     * @return
     */
    public String postJson(String url, String json) {

        if (StringUtils.isEmpty(json) || StringUtils.isEmpty(url)) {
            throw new RuntimeException("url或请求参数不能为空");
        }

        return commExecute(url, json);
    }

    /**
     * @param url
     * @param queryParamMap
     * @return
     */
    public static String get(String url, HashMap<String, String> queryParamMap) {

        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("url或请求参数不能为空");
        }

        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (queryParamMap != null && queryParamMap.size() != 0) {
            queryParamMap.forEach(builder::addQueryParameter);
        }
        url = builder.build().toString();

        Request request = new Request.Builder()
            .url(url)
            .method("GET", null)
            .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (Objects.isNull(responseBody)) {
                throw new RuntimeException("响应体为空");
            }
            return responseBody.string();
        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
    }

    public static void main(String[] args) throws IOException {

        HashMap<String, String> map = new HashMap<>(8);
        map.put("name", "b");
        map.put("schoolNo", "1212b");
        //
        // HttpUtils httpUtils = new HttpUtils();
        // String response = httpUtils.postJson("http://localhost:8080/repeat/createOrder", map);
        // System.out.println(response);
        //
        // System.out.println("==============");
        //
        // DemoData demoData = new DemoData();
        // demoData.setName("zs");
        // demoData.setSchoolNo("李集小学");
        // String json = new Gson().toJson(demoData);
        //
        // HttpUtils httpUtils2 = new HttpUtils();
        // String response2 = httpUtils2.postJson("http://localhost:8080/repeat/createOrder", json);
        // System.out.println(response2);

        // HttpUtils httpUtils3 = new HttpUtils();
        // String response3 = httpUtils3.get("http://localhost:8080/repeat/token", map);
        // System.out.println(response3);
    }
}
