package com.fengluo.chain.chain;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 享元模式，基于Apollo维护链条池
 * @Author: fengluo
 * @Date: 2024/4/13 17:48
 */
@Slf4j
public class ChainContainer {

    private static Map<String, BaseChain<?, ?>> CHAIN_MAP = new HashMap<>(16);

    private static Map<String, ChainProperties> CHAIN_PROPERTIES_MAP = new HashMap<>(16);

    private final Lock chainLock = new ReentrantLock();

    public ChainContainer(List<ChainProperties> chainPropertiesList) {
        chainLock.lock();
        try {
            if (CollectionUtils.isNotEmpty(chainPropertiesList)) {
                CHAIN_PROPERTIES_MAP = chainPropertiesList.stream()
                        .filter(Objects::nonNull)
                        .peek(ChainProperties::validate)
                        .collect(HashMap::new, (map, chainProperties) -> map.put(chainProperties.getChainName(), chainProperties), HashMap::putAll);
            }
        } finally {
            chainLock.unlock();
        }
    }

    /**
     * 初始化创建链条，放入本地缓存
     * @return
     */
    public ChainContainer init() {
        chainLock.lock();
        if (MapUtils.isEmpty(CHAIN_PROPERTIES_MAP)) {
            return this;
        }
        try {
            CHAIN_PROPERTIES_MAP.values().forEach(chainProperties -> {
                try {
                    BaseChain<?, ?> baseChain = ChainFactory.createBaseChain(chainProperties);
                    CHAIN_MAP.put(baseChain.getChainName(), baseChain);
                } catch (Throwable e) {
                    throw new IllegalArgumentException("链条创建失败 ", e);
                }
            });
            this.registerApolloChangeListener();
            return this;
        } finally {
            chainLock.unlock();
        }
    }

    /**
     * 根据链条名称和实现类获取具体链条
     * @param chainName
     * @param chainImplClass
     * @return
     * @param <T>
     */
    public <T extends BaseChain<?, ?>> T getChain(String chainName, Class<T> chainImplClass) {
        BaseChain<?, ?> baseChain = CHAIN_MAP.get(chainName);
        if (baseChain == null) {
            return null;
        }
        if (chainImplClass.equals(baseChain.getClass())) {
            return ((T) baseChain);
        }
        return null;
    }

    /**
     * 注册 apollo 监听
     */
    private void registerApolloChangeListener() {
        ConfigFile configFile = ConfigService.getConfigFile(ChainConfig.CHAIN_NAMESPACE, ConfigFileFormat.JSON);
        configFile.addChangeListener(configFileChangeEvent -> {
            try {
                String content = configFile.getContent();
                List<ChainProperties> chainPropertiesList = new ArrayList<>();
                if (StringUtils.isNotBlank(content)) {
                    chainPropertiesList = JSON.parseArray(content, ChainProperties.class);
                }
                this.update(chainPropertiesList);
            } catch (Throwable e) {
                log.error("更新Apollo链条配置发生异常！", e);
            }
        });
    }

    /**
     * 更新链条
     * @param chainPropertiesList
     */
    private void update(List<ChainProperties> chainPropertiesList) {
        chainLock.lock();
        log.info("更新链条开始");
        try {
            if (CollectionUtils.isEmpty(chainPropertiesList)) {
                CHAIN_PROPERTIES_MAP = new HashMap<>(16);
                CHAIN_MAP = new HashMap<>(16);
            } else {
                CHAIN_PROPERTIES_MAP = chainPropertiesList.stream()
                        .filter(Objects::nonNull)
                        .peek(ChainProperties::validate)
                        .collect(HashMap::new, (map, chainProperties) -> map.put(chainProperties.getChainName(), chainProperties), HashMap::putAll);
                CHAIN_PROPERTIES_MAP.values().forEach(chainProperties -> {
                    try {
                        BaseChain<?, ?> baseChain = ChainFactory.createBaseChain(chainProperties);
                        CHAIN_MAP.put(baseChain.getChainName(), baseChain);
                    } catch (Throwable e) {
                        throw new IllegalArgumentException("链条创建失败 ", e);
                    }
                });
            }
        } finally {
            chainLock.unlock();
            log.info("更新链条结束");
        }
    }

}
