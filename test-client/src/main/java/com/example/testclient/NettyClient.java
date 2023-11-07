package com.example.testclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 在这里可以添加客户端处理器
                            // ch.pipeline().addLast(new YourClientHandler());
                        }
                    });

            String serverHost = "192.168.2.97";
            int serverPort = 6531;

            ChannelFuture channelFuture = bootstrap.connect(serverHost, serverPort);

            // 添加连接成功的监听器
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("Connected to server successfully");
                    Channel channel = future.channel();

                    while (true){
                        Scanner scanner = new Scanner(System.in);
                        byte[] bytes = scanner.nextLine().getBytes();
                        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
                        channel.writeAndFlush(byteBuf).sync();
                        System.out.println(channel.isActive());
                    }



                } else {
                    System.err.println("Failed to connect to server");
                }
            });

            // 等待连接关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}