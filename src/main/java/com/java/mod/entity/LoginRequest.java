package com.java.mod.entity;

import lombok.Data;

/**
 * 存储用户登录的用户名和密码
 */
@Data
public class LoginRequest {

    private String username;//登录的用户名

    private String password;//登录的密码
}
