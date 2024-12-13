package com.lxz.consumer;

import com.lxz.config.RpcConfig;
import com.lxz.model.User;

import com.lxz.proxy.ServiceProxyFactory;
import com.lxz.service.UserService;
import com.lxz.utils.ConfigUtils;
import com.lxz.utils.ServiceDiscover;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Consumer {
    public static void main(String[] args) {

        // 加载Rpc 配置
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");

        // 从配置文件中得到服务器ip地址   服务器端口号
        System.out.println("name is :"+rpcConfig.getName()+" ipaddress is:"+rpcConfig.getServerHost()+" port is"+rpcConfig.getServerPort());

        // 得到客户端的 ip地址
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String ipAddress = localHost.getHostAddress();
        System.out.println("client ipaddress is"+ipAddress);

        // 通过代理对象 rpc获取到远程用户服务对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        //  展示接口的服务列表  ！！！！！！
        ServiceDiscover serviceDiscover = new ServiceDiscover();
        serviceDiscover.serviceDiscover(userService);


        //调用方法测试
        User user = new User();
        user.setName("lxz");
        user.setAge(11);

        //调用方法测试
        User newUser = userService.getUser(user);
        Integer year = userService.ret_20_Age(user);
        System.out.println(year);

        if (newUser != null) {
            System.out.println("end!"+newUser.getName() +" age is : "+newUser.getAge());
        } else {
            System.out.println("user == null");
        }
    }
}
