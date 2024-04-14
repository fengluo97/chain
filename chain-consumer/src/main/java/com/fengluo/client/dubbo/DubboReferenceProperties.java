package com.fengluo.client.dubbo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dubbo Reference 配置
 * 对应 @Reference，参见：https://cn.dubbo.apache.org/zh-cn/docsv2.7/user/references/xml/dubbo-reference/
 * @Author: fengluo
 * @Date: 2024/4/14 23:51
 */
@Data
public class DubboReferenceProperties {

    private static final boolean CHECK_VALUE = false;

    private static final int RETRIES_VALUE = 2;

    private List<String> registryIds;

    private String referenceId;

    private String interfaceName;

    private String version;

    private String group;

    private boolean check = CHECK_VALUE;

    private int retries = RETRIES_VALUE;

    private int timeout;

    private String protocol;

    private String url;

    public void validate() {
        if (StringUtils.isBlank(this.interfaceName)) {
            throw new IllegalArgumentException("DubboReference配置的interfaceName为空！");
        }
        if (StringUtils.isBlank(this.referenceId)) {
            throw new IllegalArgumentException("DubboReference配置的referenceId为空！");
        }
    }
}
