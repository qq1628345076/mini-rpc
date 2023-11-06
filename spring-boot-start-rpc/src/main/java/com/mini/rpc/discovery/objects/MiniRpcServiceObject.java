package com.mini.rpc.discovery.objects;

/**
 * 迷你rpc服务存储对象
 *
 * @author zy
 * @date 2023/10/31
 */
public class MiniRpcServiceObject {


    /**
     *
     * 服务名称
     */
    private String name;
    /**
     * 服务Class
     */
    private Class<?> clazz;
    /**
     * 具体服务
     */
    private Object obj;

    public String getName() {
        return name;
    }

    public MiniRpcServiceObject setName(String name) {
        this.name = name;
        return this;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public MiniRpcServiceObject setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public MiniRpcServiceObject setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public MiniRpcServiceObject(String name, Class<?> clazz, Object obj) {
        this.name = name;
        this.clazz = clazz;
        this.obj = obj;
    }
}
