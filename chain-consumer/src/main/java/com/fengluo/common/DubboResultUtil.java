package com.fengluo.common;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:34
 */
public class DubboResultUtil {

    private DubboResultUtil() {}

    public static <T> DubboResult<T> success(T data) {
        DubboResult<T> dubboResult = new DubboResult<>();
        dubboResult.setSuccess(true);
        dubboResult.setData(data);
        return dubboResult;
    }

    public static <T> DubboResult<T> fail(String errorMsg) {
        DubboResult<T> dubboResult = new DubboResult<>();
        dubboResult.setSuccess(false);
        dubboResult.setRspDesc(errorMsg);
        return dubboResult;
    }

}
