package com.fengluo.service.impl;

import com.fengluo.common.DubboResult;
import com.fengluo.dto.request.ChainRequest;
import com.fengluo.dto.response.ChainResponse;
import com.fengluo.facade.BusinessFacade;
import com.fengluo.service.BusinessService;
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
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private BusinessFacade innerBusinessFacadeImpl;

    @Override
    public List<ChainResponse> invoke(Long id) {
        List<ChainResponse> res = new ArrayList<>();
        DubboResult<ChainResponse> dubboResult = innerBusinessFacadeImpl.invoke(ChainRequest.builder().businessId(10L).build());
        if (dubboResult.isSuccess()) {
            res.add(dubboResult.getData());
        } else {
            log.info("exception handle...");
        }
        return res;
    }

}
