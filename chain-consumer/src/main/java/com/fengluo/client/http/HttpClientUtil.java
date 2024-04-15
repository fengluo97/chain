package com.fengluo.client.http;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author: fengluo
 * @Date: 2024/4/15 21:40
 */
@Slf4j
@Data
@Component
public class HttpClientUtil {

    @Resource(name = "closeableHttpClient")
    private CloseableHttpClient closeableHttpClient;

    public String get(String url, JSONObject params) {
        String sendUrl = url;
        if (Objects.nonNull(params) && params.size() > 0) {
            sendUrl = connectParams(url, params);
        }
        HttpGet httpGet = new HttpGet(sendUrl);
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() && null != httpEntity) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("调用GET请求失败！");
    }

    /**
     * @Description: POST 请求
     * @Author: zzc
     * @Date: 2022-12-16 16:48
     * @param url:
     * @param params:
     * @param requestBody: json 串
     * @return: java.lang.String
     **/
    public String post(String url, JSONObject params, String requestBody) {
        String sendUrl = url;
        // 1.拼接参数
        if (Objects.nonNull(params) && params.size() > 0) {
            sendUrl = connectParams(url, params);
        }
        HttpPost httpPost = new HttpPost(sendUrl);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse response = null;
        try {
            // 2.设置request-body
            if (StringUtils.isNotBlank(requestBody)) {
                ByteArrayEntity entity = new ByteArrayEntity(requestBody.getBytes(StandardCharsets.UTF_8));
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() && null != httpEntity) {
                return EntityUtils.toString(httpEntity);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("调用POST请求失败！");
    }

    private String connectParams(String url, JSONObject params) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url).append("?");
        params.forEach((x, y) -> buffer.append(x).append("=").append(y).append("&"));
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public void close(CloseableHttpResponse httpResponse) throws IOException{
        if (null != httpResponse) {
            httpResponse.close();
        }
    }

}
