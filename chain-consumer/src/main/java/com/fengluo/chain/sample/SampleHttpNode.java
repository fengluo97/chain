package com.fengluo.chain.sample;

import com.fengluo.chain.node.BaseHttpNode;
import com.fengluo.chain.node.NodeProperties;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fengluo
 * @Date: 2024/4/13 16:59
 */
@Slf4j
public class SampleHttpNode extends BaseHttpNode<SampleRequest, SampleResponse> {

    public SampleHttpNode(NodeProperties nodeProperties) {
        super(nodeProperties);
    }

    @Override
    protected Class<?> getResponseClass() {
        return SampleResponse.class;
    }

}
