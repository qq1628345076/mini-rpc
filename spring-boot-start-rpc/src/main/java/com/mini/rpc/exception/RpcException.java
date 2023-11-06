package com.mini.rpc.exception;


/**
 * rpc异常
 *
 * @author zy
 * @date 2023/11/04
 */
public class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }
}
