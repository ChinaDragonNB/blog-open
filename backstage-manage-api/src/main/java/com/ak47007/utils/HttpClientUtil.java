package com.ak47007.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类
 */
public class HttpClientUtil {

    /**
     * 发送get请求，利用java代码发送请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        // 发送了一个http请求
        CloseableHttpResponse response = httpclient.execute(httpGet);
        // 如果响应200成功,解析响应结果
        if (response.getStatusLine().getStatusCode() == 200) {
            // 获取响应的内容
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        }
        return null;
    }

}
