package com.fengluo.client.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * DubboReferenceContainer Spring bean 装配类
 * @Author: fengluo
 * @Date: 2024/4/14 23:51
 */
@Configuration
public class DubboReferenceContainerConfig {

    public static final String DUBBO_REFERENCE_CONTAINER_NAMESPACE_NAME = "dubbo.reference.container";

    @Bean("dubboReferenceContainer")
    public DubboReferenceContainer getDubboReferenceContainer(ApplicationConfig applicationConfig, List<RegistryConfig> registryConfigs) {
        ConfigFile configFile = ConfigService.getConfigFile(DUBBO_REFERENCE_CONTAINER_NAMESPACE_NAME, ConfigFileFormat.JSON);
        String content = configFile.getContent();
        List<DubboReferenceProperties> dubboReferencePropertiesList = new ArrayList<>();
        if (StringUtils.isNotBlank(content)) {
            dubboReferencePropertiesList = JSON.parseArray(content, DubboReferenceProperties.class);
        }
        return new DubboReferenceContainer(applicationConfig, registryConfigs, dubboReferencePropertiesList).init();
    }

}
