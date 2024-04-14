package com.fengluo.chain.node;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 节点配置类
 * @Author: fengluo
 * @Date: 2024/4/13 16:11
 */
@Data
public class NodeProperties implements Serializable {

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点实现类地址
     */
    private String nodeImpl;

    /**
     * 节点排序值，值越小越靠前
     */
    private int order = Integer.MIN_VALUE;

    /**
     * 禁用标志
     */
    private boolean forbidden = false;

    /**
     * 节点参数
     */
    private Map<String, String> nodeParams = new HashMap<>();

    /**
     * 基础校验方法
     */
    public void validate() {
        if (StringUtils.isBlank(nodeName)) {
            throw new IllegalArgumentException("节点名称不能为空！");
        }
        if (StringUtils.isBlank(nodeType)) {
            throw new IllegalArgumentException("节点类型不能为空！");
        }
        if (NodeType.existValue(nodeType)) {
            throw new IllegalArgumentException("节点类型非法！");
        }
        if (StringUtils.isBlank(nodeImpl)) {
            throw new IllegalArgumentException("节点地址不能为空！");
        }
    }

}
