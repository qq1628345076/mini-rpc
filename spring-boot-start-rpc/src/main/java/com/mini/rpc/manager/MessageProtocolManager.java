package com.mini.rpc.manager;

import com.mini.rpc.annotation.MessageProtocolAnnotation;
import com.mini.rpc.exception.RpcException;
import com.mini.rpc.netty.NetClient;
import com.mini.rpc.protocol.MessageProtocol;
import com.mini.rpc.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 消息协议管理器
 *
 * @author zy
 * @date 2023/11/11
 */
public class MessageProtocolManager {
    private static Logger logger = LoggerFactory.getLogger(MessageProtocolManager.class);
    public static HashMap<String, MessageProtocol> protocolHashMap = new HashMap<String, MessageProtocol>();

    static {
        ServiceLoader<MessageProtocol> load = ServiceLoader.load(MessageProtocol.class);
        for (MessageProtocol messageProtocol : load) {
            MessageProtocolAnnotation annotation = messageProtocol.getClass()
                    .getAnnotation(MessageProtocolAnnotation.class);
            if (annotation == null || annotation.value().isEmpty()) {
                throw new RpcException("协议名称不能为空");
            }
            protocolHashMap.put(annotation.value(), messageProtocol);
        }
        logger.info("加载消息协议成功");
    }

    /**
     * @param protocolName 协议名称
     * @return {@link MessageProtocol }
     * @Description :
     * 获取消息协议
     * @author Zy
     * @date 2023/11/11
     */
    public static  MessageProtocol getMessageProtocol(String protocolName){
        return protocolHashMap.get(protocolName);
    }
}
