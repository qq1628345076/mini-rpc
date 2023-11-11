package com.mini.rpc.protocol.imp;

import com.mini.rpc.annotation.MessageProtocolAnnotation;
import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.protocol.MessageProtocol;
import io.fury.Fury;
import io.fury.config.Language;

/**
 * fury序列化消息协议(序列化速度快)
 *
 * @author zy
 * @date 2023/11/09
 */
@MessageProtocolAnnotation("fury")
public class FurySerializeMessageProtocol implements MessageProtocol {
    private Fury fury = Fury.builder().withLanguage(Language.JAVA).build();
    {
        fury.register(RpcRequest.class);
        fury.register(RpcResponse.class);
    }
    @Override
    public byte[] marshallingRequest(RpcRequest request) throws Exception {

        byte[] serialize = fury.serialize(request);
        return serialize;
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) throws Exception {
        return (RpcRequest) fury.deserialize(data);
    }

    @Override
    public byte[] marshallingResponse(RpcResponse response) throws Exception {
        byte[] serialize = fury.serialize(response);
        return serialize;
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) throws Exception {
        return (RpcResponse) fury.deserialize(data);
    }

}
