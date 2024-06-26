package com.fengluo.chain.sample;

import com.fengluo.chain.chain.BaseChain;
import com.fengluo.chain.chain.ChainProperties;
import com.fengluo.chain.node.BaseNode;
import com.fengluo.dto.request.SampleRequest;
import com.fengluo.dto.response.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
        log.info("获得节点：{}", nodes);
        for (BaseNode<SampleRequest, SampleResponse> node : this.nodes) {
            if (node == null || node.isForbidden()) {
                log.info("当前节点为空，或者节点被禁用");
                continue;
            }
            try {
                sampleResponse = node.invoke(request);
                log.info("节点：{}", node.getNodeName());
                log.info("返回结果：{}", sampleResponse);
                if (!sampleResponse.getSuccess()) {
                    Map<String, Object> params = sampleResponse.getParams();
                    params.putAll(sampleResponse.getParams());
                    sampleResponse.setSuccess(false);
                    sampleResponse.setParams(params);
                    return sampleResponse;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException("节点node: " + node.getNodeName() + "执行失败");
            }
        }
        return sampleResponse;
    }

    public List<SampleResponse> invokeList(SampleRequest request) {
        List<SampleResponse> sampleResponseList = new ArrayList<>();
        if (CollectionUtils.isEmpty(this.nodes)) {
            return sampleResponseList;
        }
        log.info("获得节点：{}", nodes);
        for (BaseNode<SampleRequest, SampleResponse> node : this.nodes) {
            if (node == null || node.isForbidden()) {
                log.info("当前节点为空，或者节点被禁用");
                continue;
            }
            try {
                SampleResponse sampleResponse = node.invoke(request);
                log.info("节点：{}", node.getNodeName());
                log.info("返回结果：{}", sampleResponse);
                sampleResponseList.add(sampleResponse);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException("节点node: " + node.getNodeName() + "执行失败");
            }
        }
        return sampleResponseList;
    }

}
