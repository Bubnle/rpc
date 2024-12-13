package com.lxz.proxy;

import com.lxz.RpcApplication;
import com.lxz.model.RpcRequest;

import com.lxz.model.RpcResponse;
import com.lxz.serializer.JdkSerializer;
import com.lxz.serializer.Serializer;
import com.lxz.server.SendResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;


public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Serializer serializer = new JdkSerializer();

        // 构造请求 rpcRequest
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParametersTypes(method.getParameterTypes());
        rpcRequest.setArgs(args);


        // 从配置文件中得到的  服务器ip
        String serverIp = RpcApplication.getRpcConfig().getServerHost();
        int serverPort = RpcApplication.getRpcConfig().getServerPort();

        System.out.println("client get serverPort and serverIp is :"  +serverPort+"  " +serverIp);

        try (Socket socket = new Socket()) {

            socket.connect(new java.net.InetSocketAddress(serverIp, serverPort), 5000);

            // 消息体 字符数组！
            byte[] requestBytes = serializer.serializer(rpcRequest);
            long time_first = System.currentTimeMillis();
            OutputStream outputStream = socket.getOutputStream();
            // 消息头部的发送
            outputStream.write(intToBytes(requestBytes.length));
            // 消息体的发送
            outputStream.write(requestBytes);
            // 发送的
            System.out.println("send length is:"+requestBytes.length);
            long time_second = System.currentTimeMillis();
            long result_send = time_second-time_first ;
            if (result_send>5000){
                throw new RuntimeException("send data runtime out:" + result_send + "ms");
            }
            outputStream.flush();

            // 获取响应！！！！！！！！！！！！！
            long start = System.currentTimeMillis();
            InputStream inputStream = socket.getInputStream();
            long end = System.currentTimeMillis();
            long result_time = end - start ;
            byte[] lengthBytes = new byte[4];
            inputStream.read(lengthBytes);
            int length = bytesToInt(lengthBytes);
            System.out.println("client succeed response length is:"+length);
            byte[] responseBytes = new byte[length];
            inputStream.read(responseBytes);
            // 等待服务端处理时，等待处理导致的异常/超时（比如服务端已挂死，迟迟不响应）
            if(result_time>5000){
                throw new RuntimeException("method use error time out :" + result_time + " ms");
            }
            if(responseBytes.length==0){
               throw new IOException("client read data error") ;
            }
            RpcResponse rpcResponse = new RpcResponse() ;
            rpcResponse = serializer.deserializer(responseBytes,rpcResponse.getClass()) ;
            // 读取时失败!!!!!!!!!!
            if(!rpcResponse.getMessage().equals("ok")){
                System.out.println("client read data error");
                return null ;
            }
            return rpcResponse.getData();

            // 与服务端建立连接时产生的异常/超时
        } catch (SocketTimeoutException e) {
            System.out.println("connection fail" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception" + e.getMessage());
        }

        return null;
    }

    private byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }

    private int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}

