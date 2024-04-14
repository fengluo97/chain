package com.fengluo.facade;

import com.fengluo.common.DubboResult;
import com.fengluo.common.DubboResultUtil;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:25
 */
public interface SampleFacade {

    default DubboResult<SampleResponse> invoke(SampleRequest sampleRequest) {
        return DubboResultUtil.success(SampleResponse.builder().build());
    }

}
