package com.fengluo.chain.chain;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 链条简单工厂类
 * @Author: fengluo
 * @Date: 2024/4/13 17:44
 */
@Slf4j
public class ChainFactory {

    /**
     * 根据链条配置创建单个链条类
     * @param chainProperties
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static BaseChain<?, ?> createBaseChain(ChainProperties chainProperties) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        chainProperties.validate();
        String chainImpl = chainProperties.getChainImpl();
        Class<?> chainImplClass = Class.forName(chainImpl);
        Class<?> superclass = chainImplClass;
        while (true) {
            superclass = superclass.getSuperclass();
            if (superclass.equals(BaseChain.class)) {
                break;
            }
            if (superclass.equals(Object.class)) {
                throw new IllegalArgumentException("配置的节点类不是BaseChain的子类！");
            }
        }
        log.info("开始创建");
        Constructor<?> constructor = chainImplClass.getConstructor(ChainProperties.class);
        log.info("开始创建 constructor {}", constructor);
        BaseChain<?, ?> baseChain = (BaseChain<?, ?>) constructor.newInstance(chainProperties);
        log.info("开始创建 baseChain {}", baseChain);
        return baseChain;
    }

}
