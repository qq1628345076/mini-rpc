package com.example.testclient;

import com.mini.rpc.annotation.EnableMiniRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableMiniRpc
public class TestServertApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServertApplication.class, args);

    }

}
