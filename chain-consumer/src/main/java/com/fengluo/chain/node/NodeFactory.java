package com.fengluo.chain.node;

import java.lang.reflect.InvocationTargetException;

/**
 * 节点简单工厂类
 * @Author: fengluo
 * @Date: 2024/4/13 16:25
 */
public class NodeFactory {

    /**
     * 根据节点配置创建单个节点，基于反射api创建
     * @param nodeProperties
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static BaseNode<?, ?> createBaseNode(NodeProperties nodeProperties) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        nodeProperties.validate();
        String nodeImpl = nodeProperties.getNodeImpl();
        Class<?> nodeImplClass = Class.forName(nodeImpl);
        Class<?> superclass = nodeImplClass;
        while (true) {
            superclass = superclass.getSuperclass();
            if (superclass.equals(BaseNode.class)) {
                break;
            }
            if (superclass.equals(Object.class)) {
                throw new IllegalArgumentException("配置的节点类不是BaseNode的子类！");
            }
        }
        return (BaseNode<?, ?>) nodeImplClass.getConstructor(NodeProperties.class).newInstance(nodeProperties);
    }

}
