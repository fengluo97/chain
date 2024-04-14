package com.fengluo.chain.chain;

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
 * 配置类，装载链条池 spring bean
 * @Author: fengluo
 * @Date: 2024/4/13 17:48
 */
@Configuration
public class ChainConfig {

    public final static String CHAIN_NAMESPACE = "chain";

    @Bean("chainContainer")
    public ChainContainer chainContainer() {
        ConfigFile configFile = ConfigService.getConfigFile(CHAIN_NAMESPACE, ConfigFileFormat.JSON);
        String content = configFile.getContent();
        List<ChainProperties> chainPropertiesList;
        if (StringUtils.isBlank(content)) {
            chainPropertiesList = new ArrayList<>();
        } else {
            chainPropertiesList = JSON.parseArray(content, ChainProperties.class);
        }
        return new ChainContainer(chainPropertiesList).init();
    }

}
