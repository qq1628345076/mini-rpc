package com.mini.rpc.protocol;


import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;

/**
 *消息协议:定义编组请求、解组请求、编组响应、解组响应的规范
 * todo 各类实现
 */
public interface MessageProtocol {
    /**
     * 编组请求
     * @param request 请求信息
     * @return 请求字节数组
     * @throws Exception
     */
    byte[] marshallingRequest(RpcRequest request) throws Exception;

    /**
     * 解组请求
     * @param data
     * @return
     * @throws Exception
     */
    RpcRequest unmarshallingRequest(byte[] data) throws Exception;

    /**
     * 编组响应
     * @param response
     * @return
     */
    byte[] marshallingResponse(RpcResponse response) throws Exception;

    /**
     * 解组响应
     * @param data
     * @return
     * @throws Exception
     */
    RpcResponse unmarshallingResponse(byte[] data) throws Exception;
}
