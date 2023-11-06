package com.mini.rpc.discovery.imp;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.mini.rpc.discovery.ServerDiscovery;
import com.mini.rpc.exception.RpcException;
import com.mini.rpc.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * nacos服务器发现
 *
 * @author zy
 * @date 2023/11/02
 */
public class NacosServerDiscovery implements ServerDiscovery {

    private static Logger logger  = LoggerFactory.getLogger(NacosServerDiscovery.class);
    private NamingService namingService;
    private String serverAddress;

    public NacosServerDiscovery(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void exportService(Service serviceResource) {
        //将服务注册到nacos、首次加载再进行对象
    if (namingService==null){
        synchronized (NacosServerDiscovery.class) {
            if (namingService == null) {
                try {
                    namingService = NamingFactory.createNamingService(serverAddress);
                } catch (NacosException e) {
                    throw new RpcException("nacos 服务初始化失败");
                }
            }
        }
    }
        Instance instance = new Instance();
        instance.setIp(serviceResource.getIp());
        instance.setPort(serviceResource.getPort());
        instance.setEphemeral(true);
        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("protocol", serviceResource.getProtocol());
        instance.setMetadata(metadataMap);
        try {
            namingService.registerInstance(serviceResource.getName(), instance);
        } catch (NacosException e) {
            logger.error("nacos 服务注册失败",e);
            throw new RpcException("nacos 服务注册失败");
        }

    }

    @Override
    public List<Service> findServiceList(String serviceName) {
        if (namingService==null){
            synchronized (NacosServerDiscovery.class) {
                if (namingService == null) {
                    try {
                        namingService = NamingFactory.createNamingService(serverAddress);
                    } catch (NacosException e) {
                        throw new RpcException("nacos 服务初始化失败");
                    }
                }
            }
        }
        List<Instance> instanceList = null;
        try {
            instanceList = namingService.getAllInstances(serviceName);
        } catch (NacosException e) {
            logger.error("获取所以服务失败", e);
            throw new RpcException("获取所以服务失败");
        }
        if (CollectionUtils.isEmpty(instanceList)) {
            return Lists.newArrayList();
        }
        return instanceList.stream()
                .filter(i -> i.isHealthy())
                .map(instance -> convertToService(instance)).collect(Collectors.toList());
    }
    private Service convertToService(Instance instance) {
        Service service = new Service();
        service.setWeight((int) instance.getWeight());
        //@@cn.sp.UserService 转成 cn.sp.UserService
        service.setName(instance.getServiceName().replace("@@", ""));
        Map<String, String> metadata = instance.getMetadata();
        service.setProtocol(metadata.get("protocol"));
        service.setIp(instance.getIp());
        service.setPort(instance.getPort());
        return service;
    }

    @Override
    public void registerChangeListener(String serviceName) {

    }
}
