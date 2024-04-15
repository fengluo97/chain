package com.fengluo.chain.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengluo.client.http.HttpClientUtil;
import com.fengluo.util.SpringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 基础http节点
 * @Author: fengluo
 * @Date: 2024/4/13 16:02
 */
@Slf4j
@Getter
@Setter
public abstract class BaseHttpNode<RE, RS> extends BaseNode<RE, RS> {

    private static final String HTTP_URL = "httpUrl";

    private static final String TIMEOUT = "timeout";

    protected String httpUrl;

    protected long timeout = 3000;

    public BaseHttpNode(NodeProperties nodeProperties) {
        super(nodeProperties);
        Map<String, String> nodeParams = nodeProperties.getNodeParams();
        String httpUrl = nodeParams.get(HTTP_URL);
        if (StringUtils.isBlank(httpUrl)) {
            throw new IllegalArgumentException("节点配置的httpUrl为空！");
        }
        this.httpUrl = httpUrl;
        String timeout = nodeParams.get(TIMEOUT);
        if (StringUtils.isNotBlank(timeout)) {
            this.timeout = Long.parseLong(timeout);
        }
    }

    protected abstract Class<?> getResponseClass();

    @Override
    public RS invoke(RE request) {
        String response = null;
        try {
            response = getHttpClientUtil().post(httpUrl, null, JSON.toJSONString(request));
            log.info("response return:{}", response);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("http 节点执行失败！");
        }
        JSONObject webResult = JSON.parseObject(response);
        if (webResult == null) {
            throw new RuntimeException("http 返回结果为空！");
        }
        String rspCode = webResult.getString("rspCode");
        String rspDesc = webResult.getString("rspDesc");
        String data = webResult.getString("data");
        return ((RS) JSON.parseObject(data, getResponseClass()));
    }

    private HttpClientUtil getHttpClientUtil() {
        return SpringUtil.getBean("httpClientUtil", HttpClientUtil.class);
    }

}
