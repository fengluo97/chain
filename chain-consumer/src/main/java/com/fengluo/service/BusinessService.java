package com.fengluo.service;

import com.fengluo.dto.response.ChainResponse;

import java.util.List;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:24
 */
public interface BusinessService {

    /**
     * 执行 invoke
     * @param id
     * @return
     */
    List<ChainResponse> invoke(Long id);

}
