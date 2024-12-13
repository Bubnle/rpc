package com.lxz.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class LocalRegistry {
    private static final Map<String,Class<?> > map = new ConcurrentHashMap<>() ;  // 线程安全的哈希表 键是服务名称  值是类


    // !!!注册服务     实现类和对应名字存放到哈希表  通过服务名字就可以调用对应服务了   放进去就是注册了
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass) ;
    }

    // 得到实现类
    public static Class<?> get(String ServiceName ){
        return map.get(ServiceName) ;
    }

    // 删除实现类
    public static void removeClass(String ServiceName){
        map.remove(ServiceName) ;
    }

}
