package com.java.mod.entity;

import lombok.Data;

/**
 * 存储用户注册的用户名和密码
 */
@Data
public class RegisterRequest {

    private String username;//注册的用户名

    private String password;//注册的密码
}
