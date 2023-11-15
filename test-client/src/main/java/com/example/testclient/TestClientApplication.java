package com.example.testclient;

import com.mini.rpc.annotation.EnableMiniRpc;
import com.mini.rpc.config.RpcAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableMiniRpc
public class TestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestClientApplication.class, args);
    }

}
