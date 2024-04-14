package com.fengluo.chain.node;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 基础http节点
 * @Author: fengluo
 * @Date: 2024/4/13 16:02
 */
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

    @Override
    public RS invoke(RE request) {
        // TODo HTTP调用实现
        return null;
    }
}
