# chain

### 项目介绍

chain，可热插拔的动态责任链，支持 Dubbo 与 Http 调用，基于 Apollo 实现链条的动态配置与热插拔，无需修改代码即可更新链条以及节点的接口调用。

### 安装说明

依赖项：JDK、SpringBoot、Dubbo、Apollo，分布式配置中心可自行替换为其他。

在 application.properties 中配置好 Dubbo 和 Apollo 即可启动。

### 使用说明

分布式环境中，各业务线常通过 Rpc 或者 Http 的方式实现跨服务的接口调用。

在某些业务场景下，我们需要动态的更改对其他业务线的接口调用，举个例子，电商用户注销场景，用户在注销前，系统需要发起多项校验（在途订单校验、在途售后校验、待使用优惠券校验、待使用零钱校验等），这些校验通常由各业务线提供，随着业务的发展，校验项可能发生更改。

为了避免每次更改都去修改代码，本项目提供了一种可热插拔的动态责任链实现，只需要我们定义好校验接口规范，包括入参、出参等，具体校验逻辑由下游各业务方实现，将链条节点信息配置到 Apollo 即可：

- Dubbo 接口：维护 Dubbo Reference 池，基于 Apollo 配置 Reference 信息，以便于链条节点调用
- Http 接口：在与其他业务线跨 Zookeeper 集群的情况下，只需约定好入参与出参等信息，基于 Apollo 配置 Http Url，以便于链条节点调用

这样我们就实现了动态的接口调用，将下游各业务线的每项校验都封装为一个 node，并进行排序，放入 chain 中，这样就更优雅的完成了用户注销的校验逻辑。

### 示例代码

#### 链条 Apollo Config

链条配置，包含了链条名称、链条实现类、节点信息

Apollo NameSpace：chain，JSON 类型

```json
[
    {
        "chainName": "sampleChain",
        "chainImpl": "com.fengluo.chain.sample.SampleChain",
        "chainParams": {},
        "nodePropertiesList": [
            {
                "nodeName": "inner",
                "order": 1,
                "nodeImpl": "com.fengluo.chain.sample.SampleDubboNode",
                "nodeType": "dubbo",
                "forbidden": false,
                "nodeParams": {
                    "referenceId": "innerSampleFacadeImpl"
                }
            },
            {
                "nodeName": "provide-a",
                "order": 2,
                "nodeImpl": "com.fengluo.chain.sample.SampleDubboNode",
                "nodeType": "dubbo",
                "forbidden": false,
                "nodeParams": {
                    "referenceId": "providerASampleFacadeImpl"
                }
            },
            {
                "nodeName": "provide-b",
                "order": 3,
                "nodeImpl": "com.fengluo.chain.sample.SampleDubboNode",
                "nodeType": "dubbo",
                "forbidden": false,
                "nodeParams": {
                    "referenceId": "providerBSampleFacadeImpl"
                }
            },
            {
                "nodeName": "provide-c",
                "order": 4,
                "nodeImpl": "com.fengluo.chain.sample.SampleDubboNode",
                "nodeType": "dubbo",
                "forbidden": false,
                "nodeParams": {
                    "referenceId": "providerCSampleFacadeImpl"
                }
            },
            {
                "nodeName": "http-a",
                "order": 5,
                "nodeImpl": "com.fengluo.chain.sample.SampleHttpNode",
                "nodeType": "http",
                "forbidden": false,
                "nodeParams": {
                    "httpUrl": "http://127.0.0.1:8004/sample/chainA"
                }
            },
            {
                "nodeName": "http-b",
                "order": 6,
                "nodeImpl": "com.fengluo.chain.sample.SampleHttpNode",
                "nodeType": "http",
                "forbidden": false,
                "nodeParams": {
                    "httpUrl": "http://127.0.0.1:8004/sample/chainB"
                }
            }
        ]
    }
]
```

#### Dubbo 引用池 Apollo Config

dubbo reference 配置，为链条节点提供 dubbo 调用服务

Apollo NameSpace：dubbo.reference.container，JSON 类型

```json
[
    {
        "referenceId": "innerSampleFacadeImpl",
        "interfaceName": "com.fengluo.facade.SampleFacade",
        "version": "1.0.0",
        "group": "innerSampleFacadeImpl",
        "check": false,
        "timeout": 5000
    },
    {
        "referenceId": "providerASampleFacadeImpl",
        "interfaceName": "com.fengluo.facade.SampleFacade",
        "version": "1.0.0",
        "group": "providerASampleFacadeImpl",
        "check": false,
        "timeout": 5000
    },
    {
        "referenceId": "providerBSampleFacadeImpl",
        "interfaceName": "com.fengluo.facade.SampleFacade",
        "version": "1.0.0",
        "group": "providerBSampleFacadeImpl",
        "check": false,
        "timeout": 5000
    },
    {
        "referenceId": "providerCSampleFacadeImpl",
        "interfaceName": "com.fengluo.facade.SampleFacade",
        "version": "1.0.0",
        "group": "providerCSampleFacadeImpl",
        "check": false,
        "timeout": 5000
    }
]
```

#### 测试

示例代码中的每个节点的处理逻辑仅仅是返回该节点的名称，并且为了测试方便查看结果，测试接口会返回每个节点的处理结果，具体需要哪种责任链的处理方式可以自行调整。

##### 新增

直接使用上面的 Apollo 配置文件进行测试，可以看到每个节点都返回了响应：

```json
{
    "rspCode": "200",
    "rspDesc": "success",
    "data": [
        {
            "success": true,
            "params": {
                "name": "inner"
            }
        },
        {
            "success": true,
            "params": {
                "name": "provider-A"
            }
        },
        {
            "success": true,
            "params": {
                "name": "provider-B"
            }
        },
        {
            "success": true,
            "params": {
                "name": "provider-C"
            }
        },
        {
            "success": true,
            "params": {
                "name": "http-server-chainA"
            }
        },
        {
            "success": true,
            "params": {
                "name": "http-server-chainB"
            }
        }
    ]
}
```

##### 删除

我们将链条 Apollo 配置中的 provider-A、provider-C 删除，可以看到这两个节点已经没有返回，返回结果如下：

```json
{
    "rspCode": "200",
    "rspDesc": "success",
    "data": [
        {
            "success": true,
            "params": {
                "name": "inner"
            }
        },
        {
            "success": true,
            "params": {
                "name": "provider-B"
            }
        },
        {
            "success": true,
            "params": {
                "name": "http-server-chainA"
            }
        },
        {
            "success": true,
            "params": {
                "name": "http-server-chainB"
            }
        }
    ]
}
```

##### 修改

我们将 provider-B 与 http-server-chainB 节点的顺序交换，并且将 http-server-chainA 禁用，可以看到响应符合预期，返回结果如下：

```json
{
    "rspCode": "200",
    "rspDesc": "success",
    "data": [
        {
            "success": true,
            "params": {
                "name": "inner"
            }
        },
        {
            "success": true,
            "params": {
                "name": "http-server-chainB"
            }
        },
        {
            "success": true,
            "params": {
                "name": "provider-B"
            }
        }
    ]
}
```

