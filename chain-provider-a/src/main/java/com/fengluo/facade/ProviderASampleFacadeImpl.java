package com.fengluo.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.fengluo.common.DubboResult;
import com.fengluo.common.DubboResultUtil;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:53
 */
@Slf4j
@Service(version = "1.0.0", group = "providerASampleFacadeImpl")
public class ProviderASampleFacadeImpl implements SampleFacade {

    @Override
    public DubboResult<SampleResponse> invoke(SampleRequest sampleRequest) {
        try {
            // doSomeThing...
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        SampleResponse sampleResponse = new SampleResponse();
        sampleResponse.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "provider-A");
        sampleResponse.setParams(map);
        log.info("chainRequest:{}, chainResponse:{}", sampleRequest, sampleResponse);
        return DubboResultUtil.success(sampleResponse);
    }

}
