package com.lxz.model;

import java.io.Serializable;

public class User implements Serializable {     // 方便以后序列化
    private String name;          // 用户名
    private Integer age ;         // 年龄
    public String getName() {    // 获取用户名
        return name;
    }

    public void setName(String name) {   // 设置用户名
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
