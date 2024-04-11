package com.fengluo.chain.node;

import lombok.Data;

import java.util.Comparator;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:40
 */
@Data
public abstract class BaseNode<RE, RS> implements Comparable<BaseNode<RE, RS>>, Comparator<BaseNode<RE, RS>> {

    protected int order = Integer.MIN_VALUE;

    protected String nodeName = "DEFAULT";

    protected boolean forbidden = false;


    /**
     * 抽象方法 节点invoke
     * @param request
     * @return
     */
    public abstract RS invoke(RE request);


}
