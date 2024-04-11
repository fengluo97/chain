package com.fengluo.common;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:34
 */
public class WebResultUtil {

    private WebResultUtil() {}

    public static <T> WebResult<T> success(T data) {
        WebResult<T> webResult = new WebResult<>();
        webResult.setRspCode("200");
        webResult.setRspDesc("success");
        webResult.setData(data);
        return webResult;
    }

    public static <T> WebResult<T> fail(String errorMsg) {
        WebResult<T> webResult = new WebResult<>();
        webResult.setRspCode("500");
        webResult.setRspDesc(errorMsg);
        return webResult;
    }

}
