package com.fengluo.chain.node;

import lombok.Data;

import java.util.Comparator;
import java.util.Objects;

/**
 * 基础节点类
 * @Author: fengluo
 * @Date: 2024/4/10 22:40
 */
@Data
public abstract class BaseNode<RE, RS> implements Comparable<BaseNode<RE, RS>>, Comparator<BaseNode<RE, RS>> {

    /**
     * 节点配置
     */
    protected NodeProperties nodeProperties;

    /**
     * 节点名称，应唯一
     */
    protected String nodeName;

    /**
     * 节点类型
     */
    protected String nodeType;

    /**
     * 节点排序值
     */
    protected int order;

    /**
     * 节点禁用标志
     */
    protected boolean forbidden;

    public BaseNode(NodeProperties nodeProperties) {
        if (nodeProperties == null) {
            throw new IllegalArgumentException("节点配置为空！");
        }
        nodeProperties.validate();
        this.nodeProperties = nodeProperties;
        this.nodeName = nodeProperties.getNodeName();
        this.nodeType = nodeProperties.getNodeType();
        this.order = nodeProperties.getOrder();
        this.forbidden = nodeProperties.isForbidden();
    }

    /**
     * 抽象方法 节点invoke
     * @param request
     * @return
     */
    public abstract RS invoke(RE request);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseNode<?, ?> baseNode = (BaseNode<?, ?>) o;
        return Objects.equals(nodeName, baseNode.nodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeName);
    }

    @Override
    public int compareTo(BaseNode<RE, RS> o) {
        if (o == null) {
            throw new NullPointerException("o1 is null");
        }
        if (this.order < o.getOrder()) {
            return 1;
        } else if (this.order > o.getOrder()) {
            return -1;
        }
        return 0;
    }

    @Override
    public int compare(BaseNode<RE, RS> o1, BaseNode<RE, RS> o2) {
        if (o1 == null) {
            throw new NullPointerException("o1 is null");
        }
        return o1.compareTo(o2);
    }

}
