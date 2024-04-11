package com.fengluo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:47
 */
@Data
public class WebResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rspCode;

    private String rspDesc;

    private T data;

}
