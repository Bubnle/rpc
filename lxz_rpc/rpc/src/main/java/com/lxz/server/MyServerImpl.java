package com.lxz.server;

import com.lxz.model.RpcRequest;
import com.lxz.model.RpcResponse;
import com.lxz.registry.LocalRegistry;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



    public class MyServerImpl implements MyServer {
        private final ExecutorService threadPool = Executors.newFixedThreadPool(10); // 创建一个固定大小的线程池
        public int times = 0;
        public void dostart(int port) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("listening port : " + port + " ...");

                while (true) {
                    Socket connectionSocket = serverSocket.accept();
                    System.out.println("connecting and receiving request ...");

                    threadPool.execute(new WorkerThread(connectionSocket)); // 使用线程池执行任务
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    public byte[] changeIntToByte(int value) {  //整数转换成字节数组
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }

    public int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    private class WorkerThread implements Runnable {
        private Socket connectionSocket;

        public WorkerThread(Socket socket) {
            this.connectionSocket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = connectionSocket.getInputStream();
                byte[] length = new byte[4];
                inputStream.read(length);
                int readBytes = bytesToInt(length);
                System.out.println("server read data length is :  " + readBytes);
                RpcResponse rpcResponse = new RpcResponse();

                if (readBytes == 0) {
                    System.out.println("server read data fail");
                    rpcResponse.setMessage("server read data fail");
                    SendResponse sendResponse = new SendResponse();
                    sendResponse.sendResponse(rpcResponse, connectionSocket, times);
                } else {
                    byte[] data = new byte[readBytes];
                    inputStream.read(data);

                    HandlerBytes handlerBytes = new HandlerBytes();
                    RpcRequest rpcRequest = handlerBytes.handlerBytes(data);
                    System.out.println(" print request method is : " + rpcRequest.getServiceName());

                    try {
                        Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                        Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParametersTypes());
                        long startTime = System.currentTimeMillis();
                        Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                        long endTime = System.currentTimeMillis();
                        long time = endTime - startTime;
                        if (time > 5000) {
                            SendResponse sendResponse = new SendResponse();
                            sendResponse.sendResponse(rpcResponse, connectionSocket, times);
                            throw new RuntimeException("method use time out  , use time is:" + time + " ms");
                        }

                        if (result == null) {
                            rpcResponse.setMessage("method use fail");
                        } else {
                            rpcResponse.setData(result);
                            rpcResponse.setDataType(method.getReturnType());
                            rpcResponse.setMessage("ok");
                            System.out.println("response success");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        rpcResponse.setMessage(e.getMessage());
                        rpcResponse.setException(e);
                    }

                    SendResponse sendResponse = new SendResponse();
                    sendResponse.sendResponse(rpcResponse, connectionSocket, times);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    connectionSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
