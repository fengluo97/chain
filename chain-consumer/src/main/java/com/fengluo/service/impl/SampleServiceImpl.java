package com.fengluo.service.impl;

import com.fengluo.common.DubboResult;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import com.fengluo.facade.SampleFacade;
import com.fengluo.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:41
 */
@Slf4j
@Service
public class SampleServiceImpl implements SampleService {

    @Resource
    private SampleFacade innerSampleFacadeImpl;

    @Override
    public List<SampleResponse> invoke(Long id) {
        List<SampleResponse> res = new ArrayList<>();
        DubboResult<SampleResponse> dubboResult = innerSampleFacadeImpl.invoke(SampleRequest.builder().businessId(10L).build());
        if (dubboResult.isSuccess()) {
            res.add(dubboResult.getData());
        } else {
            log.info("exception handle...");
        }
        return res;
    }

}
