package com.mini.rpc.config.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启用启动
 * todo 如有需要初始化的类，在这里添加即可，作为扩展空间
 * @author zy
 * @date 2023/11/12
 */
public class EnableStart implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        //返回需要注入的类，或者进行一些初始化也可以
        return new String[0];
    }
}
