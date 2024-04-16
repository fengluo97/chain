package com.fengluo.chain.chain;

import com.fengluo.chain.node.BaseNode;
import com.fengluo.chain.node.NodeFactory;
import com.fengluo.chain.node.NodeProperties;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * 基础链条类
 * @Author: fengluo
 * @Date: 2024/4/10 22:39
 */
@Slf4j
@Data
@ToString
public abstract class BaseChain<RE, RS> {

    /**
     * 链条名称
     */
    protected String chainName;

    /**
     * 链条配置
     */
    protected volatile ChainProperties chainProperties;

    /**
     * 链条内部节点
     */
    protected volatile Set<BaseNode<RE, RS>> nodes = new TreeSet<>(BaseNode::compareTo);

    public BaseChain(ChainProperties chainProperties) {
        if (chainProperties == null) {
            throw new IllegalArgumentException("链条节点初始化失败，链条配置类为空！");
        }
        chainProperties.validate();
        this.chainName = chainProperties.getChainName();
        this.chainProperties = chainProperties;
        chainProperties.getNodePropertiesList().stream()
                .filter(Objects::nonNull)
                .peek(NodeProperties::validate)
                .forEach(nodeProperties -> {
                    try {
                        this.nodes.add((BaseNode<RE, RS>) NodeFactory.createBaseNode(nodeProperties));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException("链条节点初始化失败，未知异常！");
                    }
                });
        log.info("链条初始化成功！chainName:{}, chain:{}", this.getChainName(), this);
    }

    /**
     * 执行 invoke 方法，链条 invoke 直接对外提供服务
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
