package com.lxz.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class ConfigUtils {
        // 默认配置！！！！！！
        public static <T> T loadConfig(Class<T> tClass, String prefix) {

            return loadConfig(tClass, prefix,"");
        }

        // 非默认配置
        public static <T> T loadConfig(Class<T> tClass, String prefix,String environment) {
            String configFile = "application" ;
            if(environment!=null&&!environment.isEmpty()){
                configFile+="-"+environment ;
            }
            configFile+= ".properties" ;
            System.out.println("configFile name is "+configFile);
            Properties properties = new Properties() ;
             // 加载配置文件 application.properties   并进行异常处理
            try(InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(configFile)){
                properties.load(inputStream);
                if(inputStream==null){
                    throw new IOException("configFile " + configFile + " load failure " + configFile);
                }
            }catch (IOException e){
                throw new RuntimeException("load configFile failure " + configFile, e);
            }
            System.out.println("prefix is:"+prefix);
            // 进行映射将配置文件映射到Java对象当中
            try {
                T configObject = tClass.getDeclaredConstructor().newInstance();
                System.out.println("name is"+properties.stringPropertyNames());
                for (String name : properties.stringPropertyNames()) {
                    if (name.startsWith(prefix + ".")) {
                        String key = name.substring(prefix.length() + 1);
                        String value = properties.getProperty(name);
                        System.out.println("key = "+key + "   name = " + name);
                        System.out.println("method name is: set"+capitalize(key));
                        tClass.getMethod("set" + capitalize(key), String.class).invoke(configObject, value);
                        System.out.println("set successfully");
                    }
                }
                System.out.println("reflect successfully");
                return configObject;
            } catch (Exception e) {
                throw new RuntimeException("reflect failure " + tClass.getName(), e);
            }
        }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}


