package com.fengluo.client.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: fengluo
 * @Date: 2024/4/15 21:41
 */
@Configuration
public class HttpClientConfig {

    /**
     * 配置httpclient实例，最好是将参数配置起来
     * @return
     */
    @Bean("closeableHttpClient")
    public CloseableHttpClient closeableHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
        // 配置 http、https
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
                register("http", PlainConnectionSocketFactory.getSocketFactory()).
                register("https", sslConnectionSocketFactory).build();
        // 初始化连接管理器
        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 同时最多连接数
        poolConnManager.setMaxTotal(500);
        // 设置最大路由
        poolConnManager.setDefaultMaxPerRoute(200);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000)
                .setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000)
                .build();
        return HttpClients.custom()
                .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

}
