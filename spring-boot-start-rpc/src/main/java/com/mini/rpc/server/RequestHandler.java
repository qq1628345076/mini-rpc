package com.mini.rpc.server;


import com.alibaba.fastjson.JSON;
import com.mini.rpc.discovery.objects.MiniRpcServiceObject;
import com.mini.rpc.discovery.regist.ServerRegister;
import com.mini.rpc.exception.RpcException;
import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.protocol.MessageProtocol;
import com.mini.rpc.utils.ReflectUtils;
import com.mini.rpc.utils.RpcResponseUtils;

import java.lang.reflect.Method;


/**
 * 请求处理者，提供解组请求、编组响应等操作
 *
 * @author zy
 * @date 2023/11/05
 */
public class RequestHandler {

    private MessageProtocol protocol;


    private ServerRegister serverRegister;

    public RequestHandler(MessageProtocol protocol, ServerRegister serverRegister) {
        this.protocol = protocol;
        this.serverRegister = serverRegister;
    }


    public byte[] handleRequest(byte[] data) throws Exception {
        // 1.解组消息
        RpcRequest req = this.protocol.unmarshallingRequest(data);

        // 2.查找服务对应
        MiniRpcServiceObject so = serverRegister.getServiceObject(req.getServiceName());

        RpcResponse response = null;

        if (so == null) {
            response = new RpcResponse();

        } else {
            try {
                // 3.反射调用对应的方法过程
                Method method = so.getClazz().getMethod(req.getMethod(), ReflectUtils.convertToParameterTypes(req.getParameterTypeNames()));
                Object returnValue = method.invoke(so.getObj(), req.getParameters());
                response = new RpcResponse(200);
                if (req.getGeneric()) {
                    response.setReturnValue(RpcResponseUtils.handlerReturnValue(returnValue));
                } else {
                    response.setReturnValue(returnValue);
                }
            } catch (Exception e) {
                response = new RpcResponse(500);
                String errMsg = JSON.toJSONString(e);
                response.setException(new RpcException(errMsg));
            }
        }
        // 编组响应消息
        response.setRequestId(req.getRequestId());
        return this.protocol.marshallingResponse(response);
    }


}
