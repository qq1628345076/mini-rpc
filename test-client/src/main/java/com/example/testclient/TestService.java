package com.example.testclient;


import com.mini.rpc.annotation.RpcService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@RpcService
@Service
public class TestService {

    public String test (String a){
        System.out.println("hello :" +a);
        return "A";
    }
    @PostConstruct
    public void init(){
        System.out.println("init");
    }

}
