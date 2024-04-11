package com.fengluo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:10
 */
@Slf4j
@EnableDubbo
@SpringBootApplication
public class ProviderBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderBApplication.class, args);
        log.info("ProviderBApplication 启动成功！");
    }

}
