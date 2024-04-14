package com.fengluo.chain.chain;

import com.fengluo.chain.node.NodeProperties;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 链条配置类，一个配置代表一条责任链条
 * @Author: fengluo
 * @Date: 2024/4/13 17:23
 */
@Data
public class ChainProperties implements Serializable {

    /**
     * 链条名称，应唯一
     */
    private String chainName;

    /**
     * 链条实现类
     */
    private String chainImpl;

    /**
     * 链条参数
     */
    private Map<String, String> chainParams = new HashMap<>();

    /**
     * 链条节点
     */
    private List<NodeProperties> nodePropertiesList = new ArrayList<>();

    /**
     * 基础校验方法
     */
    public void validate() {
        if (StringUtils.isBlank(this.chainName)) {
            throw new IllegalArgumentException("链条名称不能为空！");
        }
        if (StringUtils.isBlank(this.chainImpl)) {
            throw new IllegalArgumentException("链条实现类不能为空！");
        }
        if (CollectionUtils.isEmpty(this.nodePropertiesList)) {
            throw new IllegalArgumentException("链条节点不能为空！");
        }
        this.nodePropertiesList.forEach(nodeProperties -> {
            if (nodeProperties == null) {
                throw new IllegalArgumentException("链条节点不能为空！");
            }
            nodeProperties.validate();
        });
    }

}
