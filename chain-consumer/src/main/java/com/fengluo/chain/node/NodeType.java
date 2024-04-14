package com.fengluo.chain.node;

/**
 * 节点类型枚举
 * @Author: fengluo
 * @Date: 2024/4/13 16:17
 */
public enum NodeType {

    DUBBO("DUBBO", "DUBBO 节点"),

    HTTP("HTTP", "HTTP 节点");

    private final String value;

    private final String desc;

    NodeType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public static boolean existValue(String value) {
        for (NodeType nodeType : values()) {
            if (nodeType.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
