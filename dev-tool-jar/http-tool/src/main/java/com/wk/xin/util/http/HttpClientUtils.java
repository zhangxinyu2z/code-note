package com.wk.xin.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 单次释放连接的http请求工具类
 * 编码格式 参考HttpClientUtils类的公用属性
 *
 * @author DaLon
 * @version $Id: HttpClientUtils.java 878 2017-03-04 09:29:42Z admin $
 * @created on 2016年9月28日
 */
public class HttpClientUtils {

    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String ISO = "ISO-8859-1";

    public static final Charset CHARSET_UTF8 = Charset.forName(HttpClientUtils.UTF8);
    public static final Charset CHARSET_GBK = Charset.forName(HttpClientUtils.GBK);
    public static final Charset CHARSET_ISO = Charset.forName(HttpClientUtils.ISO);

    /**
     * @param url        请求地址 不要带?
     * @param paras      请求的参数
     * @param reqCharset 请求的url编码格式
     * @return
     */
    public static byte[] get(String url, Map<String, String> paras, Charset reqCharset) {

        byte[] bytes = null;
        String query = "?";
        try {
            if (paras != null) {
                for (Entry<String, String> entry : paras.entrySet()) {
                    query = query + URLEncoder.encode(entry.getKey(), reqCharset.name()) + "=" + URLEncoder.encode(
                        entry.getValue(), reqCharset.name()) + "&";
                }
            }
            bytes = Request.Get(url + query).execute().returnContent().asBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bytes;
    }

    /**
     * @param url        请求地址 不要带?
     * @param paras      请求的参数
     * @param reqCharset 请求的url编码格式
     * @param resCharset 返回数据的解码格式 转 字符串
     * @return
     */
    public static String get(String url, Map<String, String> paras, Charset reqCharset, Charset resCharset) {
        return new String(get(url, paras, reqCharset), resCharset);
    }

    /**
     * 表单格式请求
     *
     * @param url        请求地址
     * @param paras      请求的参数
     * @param reqCharset 请求参数的编码格式
     * @return
     */
    public static byte[] postForm(String url, Map<String, String> paras, Charset reqCharset) {
        Form form = Form.form();
        byte[] bytes = null;
        try {
            if (paras != null) {
                for (Entry<String, String> entry : paras.entrySet()) {
                    form.add(entry.getKey(), entry.getValue());
                }
            }
            bytes = Request.Post(url).bodyForm(form.build(), reqCharset).execute().returnContent().asBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bytes;
    }

    /**
     * @param url        请求地址
     * @param paras      请求的参数
     * @param reqCharset 请求参数的编码格式
     * @param resCharset 返回数据的解码格式 转 字符串
     * @return
     */
    public static String postForm(String url, Map<String, String> paras, Charset reqCharset, Charset resCharset) {
        return new String(postForm(url, paras, reqCharset), resCharset);
    }

    /**
     * @param url        请求地址
     * @param json       标准的json字符串
     * @param reqCharset 请求参数的编码格式
     * @return
     */
    public static byte[] postJson(String url, String json, Charset reqCharset) {
        byte[] bytes = null;
        try {

            bytes = Request.Post(url).bodyString(json, ContentType.create("application/json", reqCharset)).execute()
                .returnContent().asBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bytes;
    }

    /**
     * @param url        请求地址
     * @param json       标准的json字符串
     * @param reqCharset 请求json的编码格式
     * @param resCharset 返回数据的解码格式 转 字符串
     * @return
     */
    public static String postJson(String url, String json, Charset reqCharset, Charset resCharset) {
        return new String(postJson(url, json, reqCharset), resCharset);
    }

    /**
     * 上传字节数组 适合小文件
     *
     * @param url        请求地址
     * @param file       上传文件字节数组
     * @param filename   文件名称
     * @param fileKey    指定上传后台接受的文件key
     * @param paras      文件上传表单的其他参数
     * @param reqCharset 请求数据的编码格式
     * @return
     */
    public static byte[] uploadSmallFile(String url, byte[] file, String filename, String fileKey,
        Map<String, String> paras, Charset reqCharset) {

        CloseableHttpClient httpclient = null;

        try {

            httpclient = HttpClients.createDefault();

            MultipartEntityBuilder create = MultipartEntityBuilder.create();
            MyFileBody bin = new MyFileBody(file, filename);
            create.setCharset(reqCharset);
            create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Entry<String, String> Entry : paras.entrySet()) {
                create.addPart(Entry.getKey(),
                    new StringBody(Entry.getValue(), ContentType.create("text/plain", reqCharset)));
            }
            create.addPart(fileKey, bin);
            HttpEntity entity = create.build();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);

            CloseableHttpResponse response = null;

            try {

                response = httpclient.execute(httppost);

                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        byte[] b = new byte[1024];
                        int n;
                        while ((n = instream.read(b)) != -1) {
                            out.write(b, 0, n);
                        }
                        return out.toByteArray();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (instream != null) {
                                instream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 上传字节数组 适合小文件
     *
     * @param url        请求地址
     * @param file       上传文件字节数组
     * @param fileName   文件名称
     * @param fileKey    指定上传后台接受的文件key
     * @param paras      文件上传表单的其他参数
     * @param reqCharset 请求数据的编码格式
     * @param resCharset 返回数据的解码格式 转 字符串
     * @return
     */
    public static String uploadSmallFile(String url, byte[] file, String fileName, String fileKey,
        Map<String, String> paras, Charset reqCharset, Charset resCharset) {
        return new String(uploadSmallFile(url, file, fileName, fileKey, paras, reqCharset), resCharset);
    }

    /**
     * @param url        请求地址
     * @param file       文件File
     * @param fileKey    指定上传后台接受的文件key
     * @param paras      文件上传表单的其他参数
     * @param reqCharset 请求数据的编码格式
     * @return
     */
    public static byte[] uploadFile(String url, File file, String fileKey, Map<String, String> paras,
        Charset reqCharset) {

        CloseableHttpClient httpclient = null;

        try {

            httpclient = HttpClients.createDefault();

            MultipartEntityBuilder create = MultipartEntityBuilder.create();

            FileBody bin = new FileBody(file);
            create.setCharset(reqCharset);
            create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Entry<String, String> Entry : paras.entrySet()) {
                create.addPart(Entry.getKey(),
                    new StringBody(Entry.getValue(), ContentType.create("text/plain", reqCharset)));
            }
            create.addPart(fileKey, bin);
            HttpEntity entity = create.build();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);

            CloseableHttpResponse response = null;

            try {

                response = httpclient.execute(httppost);

                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        byte[] b = new byte[1024];
                        int n;
                        while ((n = instream.read(b)) != -1) {
                            out.write(b, 0, n);
                        }
                        return out.toByteArray();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (instream != null) {
                                instream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * @param url        请求地址
     * @param file       文件File
     * @param fileKey    指定上传后台接受的文件key
     * @param paras      文件上传表单的其他参数
     * @param reqCharset 请求数据的编码格式
     * @param resCharset 返回数据的解码格式 转 字符串
     * @return
     */
    public static String uploadFile(String url, File file, String fileKey, Map<String, String> paras,
        Charset reqCharset, Charset resCharset) {
        return new String(uploadFile(url, file, fileKey, paras, reqCharset), resCharset);
    }

    /**
     * 拼接url参数
     *
     * @param url    url
     * @param params 表单字段
     * @return
     */
    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) {
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }

    public static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    static class MyFileBody extends AbstractContentBody {

        private byte[] bytes; // 字节数组
        private String name; // 文件名称

        public MyFileBody(byte[] bytes, String name) {
            super(ContentType.DEFAULT_BINARY);
            this.bytes = bytes;
            this.name = name;
        }

        @Override
        public String getFilename() {
            return name;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            try {
                final byte[] tmp = new byte[4096];
                int l;
                while ((l = inputStream.read(tmp)) != -1) {
                    out.write(tmp, 0, l);
                }
                out.flush();
            } finally {
                inputStream.close();
            }
        }

        @Override
        public String getTransferEncoding() {
            return MIME.ENC_BINARY;
        }

        @Override
        public long getContentLength() {
            return bytes.length;
        }

    }


}

