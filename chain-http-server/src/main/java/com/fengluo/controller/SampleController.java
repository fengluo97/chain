package com.fengluo.controller;

import com.fengluo.common.WebResult;
import com.fengluo.common.WebResultUtil;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import com.fengluo.service.SampleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:42
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    @Resource
    private SampleService sampleService;

    @PostMapping("/chainA")
    public WebResult<SampleResponse> chainA(@RequestBody SampleRequest sampleRequest) {
        SampleResponse sampleResponse = new SampleResponse();
        sampleResponse.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "http-server-chainA");
        sampleResponse.setParams(map);
        return WebResultUtil.success(sampleResponse);
    }

    @PostMapping("/chainB")
    public WebResult<SampleResponse> chainB(@RequestBody SampleRequest sampleRequest) {
        SampleResponse sampleResponse = new SampleResponse();
        sampleResponse.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "http-server-chainB");
        sampleResponse.setParams(map);
        return WebResultUtil.success(sampleResponse);
    }

}
