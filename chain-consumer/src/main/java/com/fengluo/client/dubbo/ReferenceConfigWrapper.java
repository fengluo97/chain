package com.fengluo.client.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ReferenceConfig 包装类
 * @Author: fengluo
 * @Date: 2024/4/14 23:52
 */
@Data
public class ReferenceConfigWrapper<T> {

    private final ReferenceConfig<T> referenceConfig;

    private final DubboReferenceProperties dubboReferenceProperties;

    public ReferenceConfigWrapper(ApplicationConfig applicationConfig, List<RegistryConfig> registryConfigs, DubboReferenceProperties dubboReferenceProperties) {
        if (applicationConfig == null) {
            throw new IllegalArgumentException("applicationConfig 为空！");
        }
        if (CollectionUtils.isEmpty(registryConfigs)) {
            throw new IllegalArgumentException("registryConfigs 为空！");
        }
        if (dubboReferenceProperties == null) {
            throw new IllegalArgumentException("dubboReferenceProperties 为空！");
        }
        dubboReferenceProperties.validate();
        this.dubboReferenceProperties = dubboReferenceProperties;
        this.referenceConfig = new ReferenceConfig<>();
        this.referenceConfig.setApplication(applicationConfig);
        List<String> registryIds = this.dubboReferenceProperties.getRegistryIds();
        List<RegistryConfig> finalRegistryConfigs;
        if (CollectionUtils.isNotEmpty(registryIds)) {
            finalRegistryConfigs = registryConfigs.stream()
                    .filter(Objects::nonNull)
                    .filter(registryConfig -> registryIds.contains(registryConfig.getId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(finalRegistryConfigs)) {
                throw new IllegalArgumentException("无法根据配置查询到 registryIds");
            }
        } else {
            finalRegistryConfigs = registryConfigs;
        }
        this.referenceConfig.setRegistries(finalRegistryConfigs);
        this.referenceConfig.setId(this.dubboReferenceProperties.getReferenceId());
        this.referenceConfig.setInterface(this.dubboReferenceProperties.getInterfaceName());
        this.referenceConfig.setVersion(this.dubboReferenceProperties.getVersion());
        this.referenceConfig.setGroup(this.dubboReferenceProperties.getGroup());
        this.referenceConfig.setCheck(this.dubboReferenceProperties.isCheck());
        this.referenceConfig.setRetries(this.dubboReferenceProperties.getRetries());
        this.referenceConfig.setTimeout(this.dubboReferenceProperties.getTimeout());
        this.referenceConfig.setProtocol(this.dubboReferenceProperties.getProtocol());
        this.referenceConfig.setUrl(this.dubboReferenceProperties.getUrl());
        this.referenceConfig.get();
    }

    public T get() {
        return this.referenceConfig.get();
    }

    public Class<?> getInterfaceClass() {
        return this.referenceConfig.getInterfaceClass();
    }

    public String getId() {
        return this.referenceConfig.getId();
    }

    /**
     * 销毁
     */
    public void destroy() {
        new Thread(() -> {
            try {
                Thread.sleep(this.dubboReferenceProperties.getTimeout() + 1000);
            } catch (InterruptedException ignored) {
            }
            this.referenceConfig.destroy();
        }).start();
    }

    /**
     * 是否需求重新构造
     * @param dubboReferenceProperties
     * @return
     */
    public boolean needReconstruct(DubboReferenceProperties dubboReferenceProperties) {
        return !this.dubboReferenceProperties.equals(dubboReferenceProperties);
    }

}
