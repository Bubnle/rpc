package com.lxz.serializer;

import java.io.IOException;

public interface Serializer {

    // 序列化   将对象转为字节流
    <T> byte[] serializer(T object) throws IOException;

    // 反序列化  将字节流转化为对象
    <T> T  deserializer(byte[] bytes , Class<T> type) throws IOException;
}
