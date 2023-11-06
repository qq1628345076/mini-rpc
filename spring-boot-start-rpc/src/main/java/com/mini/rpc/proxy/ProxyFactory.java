package com.mini.rpc.proxy;

import com.mini.rpc.discovery.ServerDiscovery;
import com.mini.rpc.discovery.imp.NacosServerDiscovery;
import com.mini.rpc.exception.RpcException;
import com.mini.rpc.model.RpcRequest;
import com.mini.rpc.model.RpcResponse;
import com.mini.rpc.model.Service;
import com.mini.rpc.netty.NetClient;
import com.mini.rpc.protocol.MessageProtocol;
import com.mini.rpc.utils.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理工厂
 *
 * @author zy
 * @date 2023/11/06
 */
public class ProxyFactory {
    private Map<Class<?>, Object> objectCache = new ConcurrentHashMap<>();

    private ServerDiscovery serverDiscovery;
    private MessageProtocol messageProtocol;
    private NetClient netClient;
    public ProxyFactory(ServerDiscovery serverDiscovery, MessageProtocol messageProtocol, NetClient netClient) {
        this.serverDiscovery = serverDiscovery;
        this.messageProtocol = messageProtocol;
        this.netClient = netClient;
    }


    public Object newProxyInstance(Class clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ClientInvocationHandler(clazz));
    }

    private class ClientInvocationHandler implements InvocationHandler {

        private Class<?> clazz;

        public ClientInvocationHandler(Class<?> clazz) {
            this.clazz = clazz;
        }
        private static Random random = new Random();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("toString")) {
                return proxy.toString();
            }
            if (method.getName().equals("hashCode")) {
                return 0;
            }
            // 1.获得服务信息
            String serviceName = clazz.getName();
            List<Service> services = serverDiscovery.findServiceList(serviceName);
            //随机负载均衡
            Service service = services.get(random.nextInt(services.size()));
            // 2.构造request对象
            RpcRequest request = new RpcRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setServiceName(service.getName());
            request.setMethod(method.getName());
            request.setParameters(args);
            request.setParameterTypeNames(ReflectUtils.getParameterTypeNames(method));
            request.setGeneric(false);

            //todo 协议层编组
            RpcResponse response = netClient.sendRequest(request, service, messageProtocol);
            if (response == null) {
                throw new RpcException("the response is null");
            }
            // 6.结果处理
            if (response.getRpcStatus().equals(500)) {
                throw response.getException();
            }
            if (response.getRpcStatus().equals(404)) {
                throw new RpcException(" service not found!");
            }
            return response.getReturnValue();
        }
    }
}



