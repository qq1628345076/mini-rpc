package com.mini.rpc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.mini.rpc.exception.RpcException;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 响应处理工具类
 *
 * @author zy
 * @date 2023/11/05
 */
public class RpcResponseUtils {

    private static List<Class> JDK_CLASS = Lists.newArrayList(
            Long.class,
            Integer.class,
            Byte.class,
            Short.class,
            Double.class,
            BigDecimal.class,
            String.class
    );

    /**
     * 泛化调用结果处理
     *
     * @param returnValue
     * @return
     */
    public static Object handlerReturnValue(Object returnValue) {
        if (returnValue == null) {
            return null;
        }
        Class<?> returnValueClass = returnValue.getClass();
        if (returnValueClass.isPrimitive()) {
            throw new RpcException("方法返回值不支持私密类型");
        }
        if (JDK_CLASS.contains(returnValueClass)) {
            return returnValue;
        }
        // POJO  转map
        Map<String, Object> dataMap = JSON.parseObject(JSON.toJSONString(returnValue), Map.class);
        return dataMap;
    }

}
