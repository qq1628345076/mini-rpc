package com.example.testclient;

import com.mini.rpc.annotation.InjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testcontroller {
    @InjectService
    private static TestService testService;
    @RequestMapping("/asd")
    public void setTestService(){
        String a = testService.test("a");
        System.out.println(a);
    }
}
