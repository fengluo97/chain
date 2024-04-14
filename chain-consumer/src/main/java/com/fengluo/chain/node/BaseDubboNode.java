package com.fengluo.chain.node;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 基础dubbo节点
 * @Author: fengluo
 * @Date: 2024/4/10 22:40
 */
@Getter
@Setter
public abstract class BaseDubboNode<RE, RS, T> extends BaseNode<RE, RS> {

    private static final String REFERENCE_ID_KEY = "referenceId";

    protected String referenceId;

    public BaseDubboNode(NodeProperties nodeProperties) {
        super(nodeProperties);
        Map<String, String> nodeParams = nodeProperties.getNodeParams();
        String referenceId = nodeParams.get(REFERENCE_ID_KEY);
        if (StringUtils.isBlank(referenceId)) {
            throw new IllegalArgumentException("节点配置的referenceId为空！");
        }
        this.referenceId = referenceId;
    }


}
