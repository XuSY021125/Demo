package com.java.mod.dto;

import com.java.mod.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * 存储用户的信息
 */
@Data
public class User {
    private String userId;//用户的唯一标识


    @Size(max = 20, message = "username cannot exceed 20 characters.")
    private String username;//用户名，不存在重复的用户名

    @Size(max = 255, message = "password cannot exceed 255 characters.")
    private String password;//密码

    private Date registerDate;

    private Date loginRecent;

    private Role role;//角色身份

}

