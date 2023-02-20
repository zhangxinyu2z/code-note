package com.br.zz.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.br.zz.helper.DataProcessHelper;
import com.br.zz.business.TungeeRequestRule;
import com.br.zz.business.OpenApiConfigure;
import com.br.zz.util.UnicodeUtil;
import com.br.zz.util.http.HttpClientUtil;
import com.br.zz.util.http.HttpConstant;
import com.br.zz.util.http.OkHttpClientUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author xinyu.zhang
 * @since 2023/2/6 12:49
 */
public class ThirdApiTest {
    private OpenApiConfigure openApiConfigure;

    @Before
    public void init() {
        openApiConfigure = new OpenApiConfigure();
        openApiConfigure.setSecretId("A8KP4Y12JBFD5SH96IRXUV3LE0TWMZGQ");
        openApiConfigure.setSecretKey("WZL8X3BESMUGYOHA0QR45CF1VK6TPJDI");
        openApiConfigure.setSearchUrl("https://paas.tungee.com/api/open-api/enterprise/brief-enterprise-search");
        openApiConfigure.setHolderInfoUrl("https://paas.tungee.com/api/open-api/enterprise/business/second"
            + "-dimensionsx");

        // 模糊搜索
        String searchUrl = "https://paas.tungee.com/api/open-api/enterprise/brief-enterprise-search";

        // 工商照面，详细信息
        String detailUrl = "https://paas.tungee.com/api/open-api/enterprise/business/detail";

        // 工商二维，含股东信息
        String holderUrl = "https://paas.tungee.com/api/open-api/enterprise/business/second-dimensions";

        // 工商变更 含变更记录
        String biangengUrl = "https://paas.tungee.com/api/open-api/enterprise/industrial-commercial-change";


    }

    @Test
    public void testU() throws IOException {
        CloseableHttpClient client= HttpClients.createDefault();
        String url="https://www.baidu.com/";
        url = "https://www.12306.cn/mormhweb/";

        ook(url, new HashMap<>());

        HttpGet httpGet=new HttpGet(url);
        //处理响应部分
        CloseableHttpResponse response =null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            System.out.println("获取到的内容："+ EntityUtils.toString(entity,"UTF-8"));
            EntityUtils.consume(entity);//关闭entity
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (client!=null) {
                try {client.close();} catch (IOException e) {e.printStackTrace();}
            }
            if (response!=null) {
                try {response.close();} catch (IOException e) {e.printStackTrace();}
            }
        }

    }

    @Test
    public void testUrl() {
        String payload = "{\"data\":{\"page\":1,\"limit\":10,\"type\":1,\"customerType\":1,"
            + "\"ssoToken\":\"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9"
            +
            ".eyJwc3NfaWQiOiJQUzAwMDAwMDAwMDAwMDAxMTg4NiIsInVzZXJfbmFtZSI6Iis4Ni0xNDIwMDAwMDAwMCIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2NzYxMDk2MDYsImxvZ2luIjoiU0MxNTAwNTA5MCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI2ZjMxMWFhMy1kMGRkLTQyMjktYWRkYS00ZDA1MzQ2YzBlNTUiLCJjbGllbnRfaWQiOiJzc28ifQ.TOhcbN3jw3Yer4N5BRxRnRec03vn_sX_SqecLO-X4I6VxJ4qTdzPc7BMLKHGJGO4oy1HgQ0_q3BC7pGj5qnImic3HwGmW239Gvb8akb-LnwYqrvor7ZstxmMAM43reQcXrOhGXaVXg7fbdl-4ILZFzMMYep_eyZ5bbqZtO2TW9A\",\"pageSize\":10},\"time\":1676023218891,\"lang\":\"zh\",\"token\":\"C311085575166C2401198096A593874D\"}";

        JSONObject jsonObject = JSON.parseObject(payload);
        String url = "http://micro.tradechina.com/blade-supplier/customer-buyer/customerBuyerBasicInfoList"
            + ".json";

        String s = HttpClientUtil.doPost(url, payload, null, HttpConstant.APPLICATION_JSON, null);
        //HttpClientUtil.doPost(url, payload, 3, HttpConstant.APPLICATION_JSON, null);
        System.out.println(s);
    }


    @Test
    public void testTungee() {
        String keyword = "hh";
        //String result = requestThirdApi(openApiConfigure.getSearchUrl(), keyword, 1);
        String result = requestThirdApi(openApiConfigure.getHolderInfoUrl(), keyword, 2);
        System.out.println(result);
    }

    private static void ook(String requestUrl, Map<String, Object> paramMap) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(requestUrl).method("GET", null).build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        String bodyStr = response.body().string();
        String s = UnicodeUtil.unicodeDecode(bodyStr);
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject);
    }


    private String requestThirdApi(String searchUrl, String keyword, Integer type) {
        if(StringUtils.isBlank(searchUrl)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        //时间戳
        String timestamp = sdf.format(new Date());
        //签名随机数
        String signatureNonce = UUID.randomUUID().toString();
        HashMap<String, String> params = new HashMap<>();
        //公共参数
        params.put("accessToken", openApiConfigure.getSecretId());
        params.put("timestamp", timestamp);
        params.put("signatureNonce", signatureNonce);

        // 业务参数
        if (type == 1) {
            params.put("enterprise_names", keyword);
        } else if (type == 2) {
            params.put("match_keyword", keyword);
            params.put("keyword_type", "entity_name");
        }

        //签名
        params.put("signature", TungeeRequestRule.signature(params, openApiConfigure.getSecretKey()));
        // 对keyword encode
        if (params.get("enterprise_names") != null) {
            params.put("enterprise_names", UnicodeUtil.urlEncode(params.get("enterprise_names")));
        }
        if (params.get("match_keyword") != null) {
            params.put("match_keyword", UnicodeUtil.urlEncode(params.get("match_keyword")));
        }

        String requestUrl = TungeeRequestRule.getUrl(searchUrl, params);

        String resJson;
        resJson = OkHttpClientUtils.get(requestUrl, null);
        //resJson = HttpClientHelper.doGet(requestUrl, (BasicHeader)null);

        String s = DataProcessHelper.processData(resJson);

        return UnicodeUtil.unicodeDecode(s);
    }
}
