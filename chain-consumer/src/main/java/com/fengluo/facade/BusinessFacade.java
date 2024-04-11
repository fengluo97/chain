package com.fengluo.facade;

import com.fengluo.common.DubboResult;
import com.fengluo.common.DubboResultUtil;
import com.fengluo.dto.request.ChainRequest;
import com.fengluo.dto.response.ChainResponse;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:25
 */
public interface BusinessFacade {

    default DubboResult<ChainResponse> invoke(ChainRequest chainRequest) {
        return DubboResultUtil.success(ChainResponse.builder().build());
    }

}
