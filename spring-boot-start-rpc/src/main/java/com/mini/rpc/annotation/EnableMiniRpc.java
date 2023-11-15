package com.mini.rpc.annotation;

import com.mini.rpc.config.RpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用迷你rpc
 *
 * @author zy
 * @date 2023/11/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcAutoConfiguration.class)
public @interface EnableMiniRpc {
}
