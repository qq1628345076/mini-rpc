package com.mini.rpc.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * rpc配置
 *
 * @author zy
 * @date 2023/11/04
 */
@ConfigurationProperties(prefix = "mini.rpc")
public class MiniRpcConfig {

    /**
     * 服务注册中心地址
     */
    private String registerAddress = "127.0.0.1:8848";
    /**
     * 注册中心类型，默认nacos
     */
    private String registerCenterType = "nacos";

    /**
     * 服务暴露端口
     */
    private Integer serverPort = 6531;
    /**
     * 服务暴露ip
     */
    private String exportIp = InetAddress.getLocalHost().getHostAddress();
    /**
     * 服务解析协议     默认为java序列化
     */
    private String protocol="default";

    public MiniRpcConfig() throws UnknownHostException {
    }

    public String getExportIp() {
        return exportIp;
    }
    public String getProtocol() {
        return this.protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public MiniRpcConfig setExportIp(String exportIp) {
        this.exportIp = exportIp;
        return this;
    }

    public String getRegisterCenterType() {
        return registerCenterType;
    }

    public void setRegisterCenterType(String registerCenterType) {
        this.registerCenterType = registerCenterType;
    }


    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }




}
