package com.mini.rpc.model;


/**
 * 服务
 *
 * @author zy
 * @date 2023/11/02
 */
public class Service {
    /**
     * 服务名称
     */
    private String name;
    /**
     * 服务协议
     */
    private String protocol;
    /**
     * 服务地址ip
     */
    private String ip;
    /**
     * 服务地址端口号
     */
    private Integer port;

    /**
     * 权重，越大优先级越高
     */
    private Integer weight;


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
