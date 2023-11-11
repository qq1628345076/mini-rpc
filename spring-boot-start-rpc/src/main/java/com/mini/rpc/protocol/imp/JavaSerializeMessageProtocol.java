package com.mini.rpc.protocol.imp;



import com.mini.rpc.annotation.MessageProtocolAnnotation;
import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.protocol.MessageProtocol;

import java.io.*;

/**
 * Java序列化消息协议
 *
 * @author 2YSP
 * @date 2020/7/25 21:07
 */
@MessageProtocolAnnotation("java")
public class JavaSerializeMessageProtocol implements MessageProtocol {

    private byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(o);
        return bout.toByteArray();

    }


    @Override
    public byte[] marshallingRequest(RpcRequest request) throws Exception {
        return this.serialize(request);
    }

    @Override
    public RpcRequest unmarshallingRequest(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcRequest) in.readObject();
    }

    @Override
    public byte[] marshallingResponse(RpcResponse response) throws Exception {
        return this.serialize(response);
    }

    @Override
    public RpcResponse unmarshallingResponse(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcResponse) in.readObject();
    }
}
