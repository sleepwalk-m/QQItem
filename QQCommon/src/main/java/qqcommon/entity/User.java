package qqcommon.entity;

import java.io.Serializable;

/**
 * @author: Mask.m
 * @create: 2022/01/30 18:03
 * @description: 用户对象
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String password;


    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public User() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
