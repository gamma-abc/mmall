package com.mmall.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponseByFile implements Serializable {
    private Integer errno;
    private String[] data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public ServerResponseByFile() {
        super();
    }

    public ServerResponseByFile(String[] data) {
        this.data = data;
    }

    public ServerResponseByFile(Integer errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public ServerResponseByFile(Integer errno) {
        this.errno = errno;
    }

    public static  ServerResponseByFile createBySuccess( String[] data){
        return new ServerResponseByFile(0,data);
    }
    public static ServerResponseByFile createByErrno(){
        return new ServerResponseByFile(1);
    }
}
