package com.mmall.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponseByFile<T> implements Serializable {
    private int errno;
    private T[] data;

    public int getErrno() {
        return errno;
    }

    public T[] getData() {
        return data;
    }

    public ServerResponseByFile(int errno, T[] data) {
        this.errno = errno;
        this.data = data;
    }

    public ServerResponseByFile(int errno) {
        this.errno = errno;
    }

    public static <T> ServerResponseByFile<T> createBySuccess( T[] data){
        return new ServerResponseByFile<>(0,data);
    }
    public static <T>ServerResponseByFile<T> createByErrno(){
        return new ServerResponseByFile<>(1);
    }
}
