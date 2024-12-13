package com.lxz.utils;

import com.lxz.service.UserService;

import java.lang.reflect.Method;

public class ServiceDiscover {

    public void serviceDiscover(Object object){
        Class<?> interfaceClass = object.getClass() ;
        // 获取接口中的所有方法
        Method[] methods = interfaceClass.getDeclaredMethods();
        // 遍历并打印每个方法的信息
        for (Method method : methods) {
            System.out.println("method name is: " + method.getName());
            System.out.println("return type is " + method.getReturnType());
            System.out.print("parameterType is: ");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print(parameterType.getName() + " ");
            }
            System.out.println();
            System.out.println("---------");
        }

    }

}


