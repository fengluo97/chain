package com.fengluo.chain.chain;

import com.fengluo.chain.node.BaseNode;
import com.fengluo.chain.node.NodeFactory;
import com.fengluo.chain.node.NodeProperties;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * 基础链条类
 * @Author: fengluo
 * @Date: 2024/4/10 22:39
 */
@Data
public abstract class BaseChain<RE, RS> {

    protected volatile Set<BaseNode<RE, RS>> nodes = new TreeSet<>(BaseNode::compareTo);

    protected String chainName;

    protected volatile ChainProperties chainProperties;

    public BaseChain(ChainProperties chainProperties) {
        if (chainProperties == null) {
            throw new IllegalArgumentException("链条配置类为空！");
        }
        chainProperties.validate();
        this.chainProperties = chainProperties;
        this.chainName = chainProperties.getChainName();
        List<NodeProperties> nodePropertiesList = chainProperties.getNodePropertiesList();
        nodePropertiesList.forEach(nodeProperties -> {
            try {
                nodes.add((BaseNode<RE, RS>) NodeFactory.createBaseNode(nodeProperties));
            } catch (Throwable e) {
                throw new IllegalArgumentException("链条节点初始化失败！");
            }
        });
    }

    /**
     * 执行 invoke 方法
     * @param request
     * @return
     */
    public abstract RS invoke(RE request);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseChain<?, ?> baseChain = (BaseChain<?, ?>) o;
        return Objects.equals(chainName, baseChain.chainName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainName);
    }
}