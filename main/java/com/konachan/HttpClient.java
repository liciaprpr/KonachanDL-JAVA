package com.konachan;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {

    public static String httpClient(String url, Map<String, String> params, JSONObject jsonObject, String encoding, Map<String, String> headers) throws Exception {
        String body = "";

        //把代理设置到请求配置
        CloseableHttpClient client = null;
        if (Utils.isBlank(KonachanAuto.PROXY_PORT)) {
            client = HttpClients.createDefault();
        } else {
            HttpHost proxy = new HttpHost("127.0.0.1", Integer.parseInt(KonachanAuto.PROXY_PORT), "http");
            RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
            client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        }


        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (String paramsKey : params.keySet()) {
                nvps.add(new BasicNameValuePair(paramsKey, params.get(paramsKey)));
            }
        }

        if (jsonObject != null) {
            StringEntity s = new StringEntity(jsonObject.toString(),"UTF-8");
            httpPost.setEntity(s);
        }

        System.out.println("请求地址：" + url);
        System.out.println("请求参数：" + nvps.toString());

        if (headers != null) {
            for (String headersKey : headers.keySet()) {
                httpPost.setHeader(headersKey, headers.get(headersKey));
            }
        }

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);

        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();

        return body;
    }


    /**
     * 绕过验证
     */
    public static SSLContext createIgnoreVerifySSL() throws Exception {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
