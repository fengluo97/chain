package com.fengluo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:10
 */
@Slf4j
@SpringBootApplication
public class HttpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpServerApplication.class, args);
        log.info("ProviderCApplication 启动成功！");
    }

}
