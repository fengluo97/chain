package com.fengluo.client.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
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
 * @Author: fengluo
 * @Date: 2024/4/13 17:16
 */
@Slf4j
public class DubboReferenceContainer {

    private static DubboReferenceContainer DUBBO_REFERENCE_CONTAINER;

    private Map<String, ReferenceConfigWrapper<?>> referenceConfigWrapperMap = new HashMap<>(16);

    private volatile Map<String, DubboReferenceProperties> dubboReferencePropertiesMap = new HashMap<>(16);

    private final ApplicationConfig applicationConfig;

    private final List<RegistryConfig> registryConfigs;

    private final Lock containerLock = new ReentrantLock();

    public DubboReferenceContainer(ApplicationConfig applicationConfig, List<RegistryConfig> registryConfigs, List<DubboReferenceProperties> dubboReferencePropertiesList) {
        if (DUBBO_REFERENCE_CONTAINER != null) {
            throw new IllegalArgumentException("DubboReferenceContainer 正在重复初始化！");
        }
        synchronized (DubboReferenceContainer.class) {
            if (DUBBO_REFERENCE_CONTAINER != null) {
                throw new IllegalArgumentException("DubboReferenceContainer 正在重复初始化！");
            }
            if (applicationConfig == null) {
                throw new IllegalArgumentException("applicationConfig 为空！");
            }
            if (CollectionUtils.isEmpty(registryConfigs)) {
                throw new IllegalArgumentException("registryConfigs 为空！");
            }
            this.applicationConfig = applicationConfig;
            this.registryConfigs = registryConfigs;
            if (CollectionUtils.isNotEmpty(dubboReferencePropertiesList)) {
                this.dubboReferencePropertiesMap = dubboReferencePropertiesList.stream()
                        .filter(Objects::nonNull)
                        .peek(DubboReferenceProperties::validate)
                        .collect(HashMap::new, (map, dubboReferenceProperties) -> map.put(dubboReferenceProperties.getReferenceId(), dubboReferenceProperties), HashMap::putAll);
            }
            DUBBO_REFERENCE_CONTAINER = this;
        }
    }

    public static DubboReferenceContainer getInstance() {
        if (DUBBO_REFERENCE_CONTAINER == null) {
            throw new NullPointerException("DubboReferenceContainer 还未初始化！");
        }
        return DUBBO_REFERENCE_CONTAINER;
    }

    public <T> T getReferenceInstance(String referenceId, Class<T> referenceClass) {
        ReferenceConfigWrapper<?> referenceConfigWrapper = referenceConfigWrapperMap.get(referenceId);
        if (referenceConfigWrapper == null) {
            return null;
        }
        if (referenceConfigWrapper.getInterfaceClass().equals(referenceClass)) {
            return (T) referenceConfigWrapper.get();
        } else {
            throw new IllegalArgumentException("根据引用名称查询到的代理对象与入参的类型不一致");
        }
    }

    public DubboReferenceContainer init() {
        this.containerLock.lock();
        try {
            this.dubboReferencePropertiesMap.values().forEach(dubboReferenceProperties -> {
                ReferenceConfigWrapper<?> referenceConfigWrapper = new ReferenceConfigWrapper<>(applicationConfig, registryConfigs, dubboReferenceProperties);
                log.info("ReferenceConfig 初始化成功：{}", JSON.toJSONString(referenceConfigWrapper.getDubboReferenceProperties()));
                referenceConfigWrapperMap.put(referenceConfigWrapper.getId(), referenceConfigWrapper);
            });
            registerApolloConfigListener();
            return this;
        } finally {
            this.containerLock.unlock();
        }
    }

    private void registerApolloConfigListener() {
        ConfigFile configFile = ConfigService.getConfigFile(DubboReferenceContainerConfig.DUBBO_REFERENCE_CONTAINER_NAMESPACE_NAME, ConfigFileFormat.JSON);
        configFile.addChangeListener(configFileChangeEvent -> {
            try {
                String content = configFile.getContent();
                List<DubboReferenceProperties> dubboReferencePropertiesList = new ArrayList<>();
                if (StringUtils.isNotBlank(content)) {
                    dubboReferencePropertiesList = JSON.parseArray(content, DubboReferenceProperties.class);
                }
                this.update(dubboReferencePropertiesList);
            } catch (Throwable e) {
                log.error("更新 Dubbo Container 失败！" + e);
            }
        });
    }

    private void update(List<DubboReferenceProperties> dubboReferencePropertiesList) {
        this.containerLock.lock();
        try {
            if (CollectionUtils.isNotEmpty(dubboReferencePropertiesList)) {
                this.dubboReferencePropertiesMap = dubboReferencePropertiesList.stream()
                        .filter(Objects::nonNull)
                        .peek(DubboReferenceProperties::validate)
                        .collect(HashMap::new, (map, dubboReferenceProperties) -> map.put(dubboReferenceProperties.getReferenceId(), dubboReferenceProperties), HashMap::putAll);
            } else {
                this.dubboReferencePropertiesMap = new HashMap<>(16);
            }
            if (MapUtils.isNotEmpty(this.dubboReferencePropertiesMap)) {
                this.referenceConfigWrapperMap = new HashMap<>();
                this.dubboReferencePropertiesMap.values().forEach(dubboReferenceProperties -> {
                    ReferenceConfigWrapper<?> referenceConfigWrapper = new ReferenceConfigWrapper<>(applicationConfig, registryConfigs, dubboReferenceProperties);
                    log.info("ReferenceConfig 初始化成功：{}", JSON.toJSONString(referenceConfigWrapper.getDubboReferenceProperties()));
                    referenceConfigWrapperMap.put(referenceConfigWrapper.getId(), referenceConfigWrapper);
                });
            } else {
                this.referenceConfigWrapperMap = new HashMap<>();
            }
        } finally {
            this.containerLock.unlock();
        }
    }

}
