package com.lxz.provider;

import com.lxz.RpcApplication;
import com.lxz.registry.LocalRegistry;
import com.lxz.server.MyServer;
import com.lxz.server.MyServerImpl;
import com.lxz.service.UserService;

public class Provider {
    public static void main(String[] args) {

        // 配置文件初始化
        RpcApplication.init();

        // 服务注册
        LocalRegistry.register(UserService.class.getName() , UserServiceImpl.class);
        System.out.println("register name is "+UserService.class.getName()+" implement class is"+UserServiceImpl.class);
        //启动我的服务器
        MyServer myServer = new MyServerImpl() ;
        myServer.dostart(RpcApplication.getRpcConfig().getServerPort());

    }
}
