package com.mini.rpc.discovery.regist;

import com.mini.rpc.config.properties.MiniRpcConfig;
import com.mini.rpc.discovery.ServerDiscovery;
import com.mini.rpc.discovery.objects.MiniRpcServiceObject;
import com.mini.rpc.model.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author zy
 * @date 2023/11/01
 */
public class ServerRegister {
    private Map<String, MiniRpcServiceObject> serviceMap = new HashMap<>();

    private ServerDiscovery serverDiscovery;

    private MiniRpcConfig rpcConfig;


    public ServerRegister(ServerDiscovery serverDiscovery, MiniRpcConfig rpcConfig) {
        this.serverDiscovery = serverDiscovery;
        this.rpcConfig = rpcConfig;
    }

    public void register(MiniRpcServiceObject miniRpcServiceObject) throws UnknownHostException {
        if (miniRpcServiceObject == null) {
            throw new IllegalArgumentException("parameter cannot be empty");
        }
        serviceMap.put(miniRpcServiceObject.getName(), miniRpcServiceObject);
        Service service = new Service();
        //是否设置自定义暴露ip
        service.setIp(rpcConfig.getExportIp());
        service.setPort(rpcConfig.getServerPort());
        service.setName(miniRpcServiceObject.getClazz().getName());
        service.setProtocol(rpcConfig.getProtocol());
        serverDiscovery.exportService(service);

    }

    public MiniRpcServiceObject getServiceObject(String serviceName) {

        return  serviceMap.get(serviceName);
    }
}
