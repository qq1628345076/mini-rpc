package com.mini.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * 暴露服务的注解
 *
 * @author zy
 * @date 2023/10/31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface RpcService {

    String value() default "";
}
