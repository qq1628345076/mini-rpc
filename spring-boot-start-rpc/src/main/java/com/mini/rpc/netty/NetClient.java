package com.mini.rpc.netty;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.model.Service;
import com.mini.rpc.netty.handler.NettyInboundHandler;
import com.mini.rpc.protocol.MessageProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.*;

public class NetClient {


    /**
     * 已连接的服务缓存
     * key: 服务地址，格式：ip:port
     */
    public static Map<String, NettyInboundHandler> connectedServerNodes = new ConcurrentHashMap<>();
    private EventLoopGroup loopGroup = new NioEventLoopGroup(4);

    public RpcResponse sendRequest(RpcRequest rpcRequest, Service service, MessageProtocol messageProtocol) {

        String addressKey = service.getIp() + ":" + service.getPort();
        synchronized (addressKey.intern()) {
            if (connectedServerNodes.containsKey(addressKey)) {
                NettyInboundHandler nettyInboundHandler = connectedServerNodes.get(addressKey);

                return nettyInboundHandler.sendRequest(rpcRequest);
            }
            NettyInboundHandler nettyInboundHandler = new NettyInboundHandler(addressKey, messageProtocol);

            // 创建客户端启动器
            Bootstrap bootstrap = new Bootstrap();
                // 配置客户端启动器
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(nettyInboundHandler);
                        }
                    });
            ChannelFuture connect = bootstrap.connect(service.getIp(), service.getPort());
            connect.addListener(new ChannelFutureListener() {
                                    @Override
                                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                        connectedServerNodes.put(addressKey, nettyInboundHandler);
                                    }
                                }
            );


           return nettyInboundHandler.sendRequest(rpcRequest);
        }

    }
}
