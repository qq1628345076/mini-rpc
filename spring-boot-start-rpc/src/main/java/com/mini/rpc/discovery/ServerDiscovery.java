package com.mini.rpc.discovery;



import com.mini.rpc.model.Service;

import java.util.List;


/**
 * 服务发现抽象类
 *
 * @author zy
 * @date 2023/10/04
 */
public interface ServerDiscovery {

    /**
     * 服务暴露
     * @param serviceResource
     */
    void exportService(Service serviceResource);

    /**
     * 根据服务名查找服务列表
     * @param serviceName
     * @return
     */
    List<Service> findServiceList(String serviceName);

    /**
     * 注册服务监听
     */
    void registerChangeListener(String serviceName);
}
