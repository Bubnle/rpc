package com.lxz.server;

import com.lxz.model.RpcResponse;
import com.lxz.serializer.JdkSerializer;
import com.lxz.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendResponse {
    public void sendResponse(RpcResponse rpcResponse , Socket connectionSocket,Integer times) throws IOException {
        Serializer serializer = new JdkSerializer();
        byte[] res= serializer.serializer(rpcResponse) ;
        byte[] res_length = changeIntToByte(res.length);
        OutputStream outputStream = connectionSocket.getOutputStream();
        long writeTime = System.currentTimeMillis();
        outputStream.write(res_length);
        outputStream.write(res);
        long sendTime = System.currentTimeMillis();
        long time = sendTime-writeTime;
        if(time>5000){
            throw new RuntimeException("server write data time out") ;
        }
        // 发送
        outputStream.flush();
        System.out.println("send successfully");
        connectionSocket.close();
        System.out.println(times++);
        connectionSocket.close();
    }
    public byte[] changeIntToByte(int value){  //整数转换成字节数组
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }
}
