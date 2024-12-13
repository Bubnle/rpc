package com.lxz;

import com.lxz.config.RpcConfig;
import com.lxz.constant.RpcConstant;
import com.lxz.utils.ConfigUtils;

public class RpcApplication {
    private  static RpcConfig rpcConfig ;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig ;
    }

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}

