package com.fengluo.chain.sample;

import com.fengluo.chain.node.BaseDubboNode;
import com.fengluo.chain.node.NodeProperties;
import com.fengluo.common.DubboResult;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import com.fengluo.facade.SampleFacade;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fengluo
 * @Date: 2024/4/13 16:08
 */
@Slf4j
public class SampleDubboNode extends BaseDubboNode<SampleRequest, SampleResponse, SampleFacade> {

    public SampleDubboNode(NodeProperties nodeProperties) {
        super(nodeProperties);
    }

    @Override
    protected Class<SampleFacade> getDubboReferenceInterfaceClass() {
        return SampleFacade.class;
    }

    @Override
    public SampleResponse invoke(SampleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request 为空！");
        }
        DubboResult<SampleResponse> response;
        try {
            response = getReferenceInstance().invoke(request);
            if (!response.isSuccess()) {
                throw new IllegalArgumentException("dubbo call 异常！" + response.getRspDesc());
            }
        } catch (Throwable e) {
            log.info("节点 dubbo invoke 失败: {}, {}", this, e.getMessage());
            throw new RuntimeException("节点 dubbo invoke 失败");
        }
        return response.getData();
    }

}
