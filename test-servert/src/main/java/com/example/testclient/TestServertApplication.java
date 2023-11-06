package com.example.testclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(com.mini.rpc.config.RpcAutoConfiguration.class)
public class TestServertApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServertApplication.class, args);

    }

}
