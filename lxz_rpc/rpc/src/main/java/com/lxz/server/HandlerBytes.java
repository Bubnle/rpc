package com.lxz.server;

import com.lxz.model.RpcRequest;
import com.lxz.serializer.JdkSerializer;

import java.io.IOException;
// 处理字节流 转换为对象
public class HandlerBytes {
    public RpcRequest handlerBytes(byte[] bytes){
        JdkSerializer jdkSerializer = new JdkSerializer();
        RpcRequest rpcRequest = new RpcRequest();
        try {
            rpcRequest = jdkSerializer.deserializer(bytes,rpcRequest.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rpcRequest ;
    }
}
