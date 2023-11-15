package com.mini.rpc.config;

import com.alibaba.nacos.api.naming.NamingService;
import com.mini.rpc.annotation.EnableMiniRpc;
import com.mini.rpc.config.properties.MiniRpcConfig;
import com.mini.rpc.config.selector.EnableStart;
import com.mini.rpc.discovery.ServerDiscovery;
import com.mini.rpc.discovery.imp.NacosServerDiscovery;
import com.mini.rpc.discovery.regist.ServerRegister;
import com.mini.rpc.exception.RpcException;
import com.mini.rpc.manager.MessageProtocolManager;
import com.mini.rpc.netty.NetClient;
import com.mini.rpc.processor.SpringRpcProcessor;
import com.mini.rpc.protocol.MessageProtocol;
import com.mini.rpc.protocol.imp.JavaSerializeMessageProtocol;
import com.mini.rpc.proxy.ProxyFactory;
import com.mini.rpc.server.NettyRpcServer;
import com.mini.rpc.server.RequestHandler;
import com.mini.rpc.server.RpcServer;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * spring boot rpc自动配置
 *
 * @author zy
 * @date 2023/10/31
 */
@Configuration
@ConditionalOnBean(annotation = EnableMiniRpc.class)
@EnableConfigurationProperties(MiniRpcConfig.class)
@Import(EnableStart.class)
public class RpcAutoConfiguration {

    @Resource
    private MiniRpcConfig rpcConfig;


    @Bean
    public SpringRpcProcessor springRpcProcessor(ServerRegister serverRegister , RpcServer rpcServer, ProxyFactory proxyFactory){
        return new SpringRpcProcessor(serverRegister,rpcServer,proxyFactory);
    }
    @Bean
    public ProxyFactory proxyFactory(ServerDiscovery serverDiscovery, MessageProtocol messageProtocol, NetClient netClient){
        return new ProxyFactory(serverDiscovery,  messageProtocol,netClient);
    }

    @Bean
    public MessageProtocol messageProtocol(){
        MessageProtocol messageProtocol = MessageProtocolManager.getMessageProtocol(rpcConfig.getProtocol());
        //todo  实现多种消息协议
        return messageProtocol;
    }

    @Bean
    public NetClient netClient(){
        return new NetClient();
    }

    @Bean
    public ServerDiscovery serverDiscovery(){
        //todo  实现多种服务发现模式
        return new NacosServerDiscovery(rpcConfig.getRegisterAddress());
    }

    @Bean
    public ServerRegister serverRegister( ServerDiscovery serverDiscovery){
        return new ServerRegister(serverDiscovery,rpcConfig);
    }

    @Bean
    public RpcServer rpcServer(RequestHandler requestHandler) {
        return new NettyRpcServer(rpcConfig.getServerPort(), rpcConfig.getProtocol(), requestHandler);
    }
    @Bean
    public RequestHandler requestHandler(ServerRegister serverRegister) {
        MessageProtocol messageProtocol = new JavaSerializeMessageProtocol();
        if (messageProtocol == null) {
            throw new RpcException("invalid message protocol config!");
        }
        return new RequestHandler(messageProtocol, serverRegister);
    }
}
