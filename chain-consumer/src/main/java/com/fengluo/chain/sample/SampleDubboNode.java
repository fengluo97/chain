package com.fengluo.chain.sample;

import com.fengluo.chain.node.BaseDubboNode;
import com.fengluo.chain.node.NodeProperties;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;

/**
 * @Author: fengluo
 * @Date: 2024/4/13 16:08
 */
public class SampleDubboNode extends BaseDubboNode<SampleRequest, SampleResponse, SampleDubboNode> {


    public SampleDubboNode(NodeProperties nodeProperties) {
        super(nodeProperties);
    }

    @Override
    public SampleResponse invoke(SampleRequest request) {
        return null;
    }

}
