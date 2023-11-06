package com.mini.rpc.processor;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.mini.rpc.annotation.InjectService;
import com.mini.rpc.annotation.RpcService;
import com.mini.rpc.discovery.objects.MiniRpcServiceObject;
import com.mini.rpc.discovery.regist.ServerRegister;
import com.mini.rpc.proxy.ProxyFactory;
import com.mini.rpc.server.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * spring-rpc处理器
 * spring 容器上下文完成刷新后
 *
 * @author zy
 * @date 2023/10/31
 */
public class SpringRpcProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private ServerRegister serverRegister;
    private RpcServer rpcServer;
    private ProxyFactory proxyFactory;
    public SpringRpcProcessor(ServerRegister serverRegister,RpcServer rpcServer,ProxyFactory proxyFactory) {
        this.serverRegister = serverRegister;
        this.rpcServer = rpcServer;
        this.proxyFactory = proxyFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Spring启动完毕过后会收到一个事件通知(大型架构应用可能会出现父子容器的情况)
        if (Objects.isNull(event.getApplicationContext().getParent())) {
            ApplicationContext context = event.getApplicationContext();
            // 开启服务
            startServer(context);
            // 注入Service
            injectService(context);
        }
    }

    private void injectService(ApplicationContext context) {
        final List<String> serviceNameList = Lists.newArrayList();
        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            Class<?> clazz = context.getType(name);
            if (Objects.isNull(clazz)) {
                continue;
            }

            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                // 找出标记了InjectService注解的属性
                InjectService injectService = field.getAnnotation(InjectService.class);
                if (injectService == null) {
                    continue;
                }

                Class<?> fieldClass = field.getType();
                Object object = context.getBean(name);
                field.setAccessible(true);
                try {
                    Object proxy = proxyFactory.newProxyInstance(fieldClass);
                    field.set(object, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                serviceNameList.add(fieldClass.getName());
            }
        }
//        // 注册子节点监听
//        serviceNameList.forEach(i -> {
//            serverDiscoveryManager.registerChangeListener(i);
//        });
//        logger.info("register service change listener successfully");
//


    }

    private void startServer(ApplicationContext context) {

        Map<String, Object> beans = context.getBeansWithAnnotation(RpcService.class);
        if (!beans.isEmpty()) {
            boolean startServerFlag = true;
            for (Object obj : beans.values()) {

                try {
                    Class<?> clazz = obj.getClass();
                    Class<?>[] interfaces = clazz.getInterfaces();
                    MiniRpcServiceObject mrj = null;
                    /**
                     * 如果只实现了一个接口就用父类的className作为服务名
                     * 如果该类实现了多个接口，则用注解里的value作为服务名
                     */
                    if (interfaces.length != 1) {
                        RpcService rpcService = clazz.getAnnotation(RpcService.class);
                        String value = rpcService.value();
                        if (interfaces.length==0&& value.isEmpty()){
                            value = clazz.getName();
                        }
                        if (value.isEmpty()) {
                            startServerFlag = false;
                            throw new UnsupportedOperationException("The exposed interface is not specific with '" + obj.getClass().getName() + "'");
                        }
                        mrj = new MiniRpcServiceObject(value, Class.forName(value), obj);
                    } else {
                        Class<?> supperClass = interfaces[0];
                        mrj = new MiniRpcServiceObject(supperClass.getName(), supperClass, obj);
                    }
                        serverRegister.register(mrj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (startServerFlag){

               rpcServer.start();

            }
        }


    }
}
