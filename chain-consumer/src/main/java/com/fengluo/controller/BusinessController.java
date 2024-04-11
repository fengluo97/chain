package com.fengluo.controller;

import com.fengluo.common.WebResult;
import com.fengluo.common.WebResultUtil;
import com.fengluo.dto.response.ChainResponse;
import com.fengluo.service.BusinessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:42
 */
@RestController
@RequestMapping("/test")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/chain/{id}")
    public WebResult<List<ChainResponse>> getChainResponse(@PathVariable("id") Long id) {
        return WebResultUtil.success(businessService.invoke(id));
    }

    @Value("${test:默认值}")
    private String test;

    @GetMapping("/test")
    public String test() {
        return test;
    }

}
