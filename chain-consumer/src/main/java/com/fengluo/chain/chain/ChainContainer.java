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

    /**
     * 链条实例 Map 本地缓存
     */
    private volatile static Map<String, BaseChain<?, ?>> CHAIN_MAP = new HashMap<>(16);

    /**
     * 链条配置 Map
     */
    private volatile Map<String, ChainProperties> chainPropertiesMap = new HashMap<>(16);

    private final Lock chainUpdateLock = new ReentrantLock();

    public ChainContainer(List<ChainProperties> chainPropertiesList) {
        if (CollectionUtils.isNotEmpty(chainPropertiesList)) {
            this.chainPropertiesMap = chainPropertiesList.stream()
                    .filter(Objects::nonNull)
                    .peek(ChainProperties::validate)
                    .collect(HashMap::new, (map, chainProperties) -> map.put(chainProperties.getChainName(), chainProperties), HashMap::putAll);
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
     * 初始化创建链条，放入本地缓存
     * @return
     */
    public ChainContainer init() {
        // init 和 update 产生了对共享资源的访问
        this.chainUpdateLock.lock();
        if (MapUtils.isEmpty(this.chainPropertiesMap)) {
            return this;
        }
        try {
            this.chainPropertiesMap.values().forEach(chainProperties -> {
                try {
                    BaseChain<?, ?> baseChain = ChainFactory.createBaseChain(chainProperties);
                    log.info("初始化链条容器，创建了一个链条：{}", baseChain);
                    CHAIN_MAP.put(baseChain.getChainName(), baseChain);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException("初始化链条容器，链条：" + chainProperties.getChainName() + " 创建失败！");
                }
            });
            this.registerApolloChangeListener();
            return this;
        } finally {
            this.chainUpdateLock.unlock();
        }
    }

    /**
     * 注册 apollo 监听
     */
    private void registerApolloChangeListener() {
        ConfigFile configFile = ConfigService.getConfigFile(ChainContainerConfig.CHAIN_NAMESPACE, ConfigFileFormat.JSON);
        configFile.addChangeListener(configFileChangeEvent -> {
            try {
                String content = configFile.getContent();
                List<ChainProperties> chainPropertiesList = new ArrayList<>();
                if (StringUtils.isNotBlank(content)) {
                    chainPropertiesList = JSON.parseArray(content, ChainProperties.class);
                }
                // 监听到 apollo 配置变更，触发链条容器的变更
                this.update(chainPropertiesList);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException("监听到 Apollo 配置更新，链条配置更新发生异常");
            }
        });
    }

    /**
     * 更新链条
     * @param chainPropertiesList
     */
    private void update(List<ChainProperties> chainPropertiesList) {
        this.chainUpdateLock.lock();
        log.info("更新链条开始");
        try {
            if (CollectionUtils.isEmpty(chainPropertiesList)) {
                this.chainPropertiesMap = new HashMap<>(16);
                CHAIN_MAP = new HashMap<>(16);
            } else {
                this.chainPropertiesMap = chainPropertiesList.stream()
                        .filter(Objects::nonNull)
                        .peek(ChainProperties::validate)
                        .collect(HashMap::new, (map, chainProperties) -> map.put(chainProperties.getChainName(), chainProperties), HashMap::putAll);
                Map<String, BaseChain<?, ?>> chainMapTemp = new HashMap<>();
                this.chainPropertiesMap.values().forEach(chainProperties -> {
                    try {
                        BaseChain<?, ?> baseChain = ChainFactory.createBaseChain(chainProperties);
                        chainMapTemp.put(baseChain.getChainName(), baseChain);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException("监听到 Apollo 配置更新，链条" + chainProperties.getChainName() + "配置更新发生异常");
                    }
                });
                CHAIN_MAP = chainMapTemp;
            }
        } finally {
            log.info("更新链条结束");
            this.chainUpdateLock.unlock();
        }
    }

}
