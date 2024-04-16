package com.fengluo.service.impl;

import com.fengluo.chain.chain.ChainContainer;
import com.fengluo.chain.sample.SampleChain;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
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
    private ChainContainer chainContainer;

    @Override
    public List<SampleResponse> invoke(Long id) {
        SampleChain sampleChain = chainContainer.getChain("sampleChain", SampleChain.class);
        return sampleChain.invokeList(SampleRequest.builder().businessId(id).build());
    }

}
