package com.mini.rpc.model;


import com.mini.rpc.exception.RpcException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RpcResponse implements Serializable {

    private String requestId;

    private Map<String, String> headers = new HashMap<>();

    private Object returnValue;

    private RpcException exception;
    private Integer rpcStatus;

    public RpcResponse() {
    }

    public RpcException getException() {
        return exception;
    }

    public RpcResponse setException(RpcException exception) {
        this.exception = exception;
        return this;
    }

    public RpcResponse(Integer rpcStatus) {
        this.rpcStatus = rpcStatus;
    }
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }


    public Integer getRpcStatus() {
        return rpcStatus;
    }

    public void setRpcStatus(Integer rpcStatus) {
        this.rpcStatus = rpcStatus;
    }
}
