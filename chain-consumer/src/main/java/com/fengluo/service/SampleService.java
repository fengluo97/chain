package com.fengluo.service;

import com.fengluo.dto.response.SampleResponse;

import java.util.List;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:24
 */
public interface SampleService {

    /**
     * 执行 invoke
     * @param id
     * @return
     */
    List<SampleResponse> invoke(Long id);

}
