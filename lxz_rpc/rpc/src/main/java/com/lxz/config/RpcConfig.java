package com.lxz.config;

public class RpcConfig {

    private String name ;
    private String version ;
    private String serverHost = "localhost";
    private Integer serverPort =8081 ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        char [] newA = serverPort.toCharArray() ;
        int realServerPort = 0;
        for(int i = 0 ; i<serverPort.length();i++){
            realServerPort*=10 ;
            realServerPort += (newA[i] - '0') ;

        }
        this.serverPort = realServerPort ;
    }

}
