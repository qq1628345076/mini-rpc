package com.mini.rpc.netty.handler;


import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.model.Service;
import com.mini.rpc.netty.RpcFuture;
import com.mini.rpc.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * netty入站处理程序
 *
 * @author zy
 * @date 2023/11/06
 */
public class NettyInboundHandler extends ChannelInboundHandlerAdapter {

    private String addressKey;
    private MessageProtocol messageProtocol;
    private Channel channel;

    private static Map<String, RpcFuture<RpcResponse>> requestMap = new ConcurrentHashMap<>();

    public NettyInboundHandler(String addressKey, MessageProtocol messageProtocol) {
        this.addressKey = addressKey;
        this.messageProtocol = messageProtocol;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] resp = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(resp);
        // 手动回收
        ReferenceCountUtil.release(byteBuf);
        //解码
        RpcResponse response = messageProtocol.unmarshallingResponse(resp);
        RpcFuture<RpcResponse> future = requestMap.get(response.getRequestId());
        if (future == null) {
            return;
        }

        //放入读取的response后，会自动释放等待的线程，由countDownLatch.countDown();完成
        future.setResponse(response);

    }

    //通信通道引用
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        try {
            String requestId = rpcRequest.getRequestId();
            RpcFuture<RpcResponse> objectRpcFuture = new RpcFuture<>();
            requestMap.put(requestId, objectRpcFuture);
            byte[] bytes = messageProtocol.marshallingRequest(rpcRequest);
            ByteBuf reqBuf = Unpooled.buffer(bytes.length);
            channel.writeAndFlush((Object) reqBuf);
            // 等待返回值
            RpcResponse rpcResponse = objectRpcFuture.get(5, TimeUnit.SECONDS);
            return rpcResponse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
