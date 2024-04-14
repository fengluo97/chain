package com.fengluo.chain.sample;

import com.fengluo.chain.chain.BaseChain;
import com.fengluo.chain.chain.ChainProperties;
import com.fengluo.chain.node.BaseNode;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Map;

/**
 * 业务链条类
 * @Author: fengluo
 * @Date: 2024/4/13 17:40
 */
@Slf4j
public class SampleChain extends BaseChain<SampleRequest, SampleResponse> {


    public SampleChain(ChainProperties chainProperties) {
        super(chainProperties);
    }

    @Override
    public SampleResponse invoke(SampleRequest request) {
        SampleResponse sampleResponse = new SampleResponse();
        if (CollectionUtils.isEmpty(this.nodes)) {
            return sampleResponse;
        }
        for (BaseNode<SampleRequest, SampleResponse> node : nodes) {
            if (node == null || node.isForbidden()) {
                log.info("当前节点为空，或者节点被禁用");
                continue;
            }
            SampleResponse response = node.invoke(request);
            if (!response.getSuccess()) {
                Map<String, Object> params = sampleResponse.getParams();
                params.putAll(response.getParams());
                sampleResponse.setSuccess(false);
                sampleResponse.setParams(params);
                return sampleResponse;
            }
        }
        return sampleResponse;
    }


}
