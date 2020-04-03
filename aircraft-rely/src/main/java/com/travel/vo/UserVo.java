package com.travel.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiurunze
 * users表所对应的实体类
 */
@Setter
@Getter
public class UserVo {

    //实体类的属性和表的字段名称一一对应
    private int id;
    private String name;
    private int age;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserVo [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}