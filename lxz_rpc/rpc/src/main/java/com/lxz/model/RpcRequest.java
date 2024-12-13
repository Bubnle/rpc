package com.lxz.model;


import java.io.Serializable;

public class RpcRequest implements Serializable {
    private String serviceName;    // 服务名称
    private String methodName;     // 方法名
    private Class<?> [] parametersTypes;  // 参数类型  int  response之类的
    private Object[] args;  // 参数列表    实际数值
    private String clientName ; // 发送方ip地址;

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(Class<?>[] parametersTypes) {
        this.parametersTypes = parametersTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
