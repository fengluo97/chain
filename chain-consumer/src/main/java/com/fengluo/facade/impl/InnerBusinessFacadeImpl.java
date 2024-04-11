package com.fengluo.facade.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fengluo.common.DubboResult;
import com.fengluo.common.DubboResultUtil;
import com.fengluo.dto.request.ChainRequest;
import com.fengluo.dto.response.ChainResponse;
import com.fengluo.facade.BusinessFacade;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:26
 */
@Slf4j
@Service(version = "1.0.0", group = "innerBusinessFacadeImpl")
public class InnerBusinessFacadeImpl implements BusinessFacade {

    @Override
    public DubboResult<ChainResponse> invoke(ChainRequest chainRequest) {
        try {
            // doSomeThing...
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "inner");
        chainResponse.setParams(map);
        log.info("chainRequest:{}, chainResponse:{}", chainRequest, chainResponse);
        return DubboResultUtil.success(chainResponse);
    }

}
