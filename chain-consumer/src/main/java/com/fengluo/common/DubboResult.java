package com.fengluo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:30
 */
@Data
public class DubboResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private String rspDesc;

    private T data;

}
