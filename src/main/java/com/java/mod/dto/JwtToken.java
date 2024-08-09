package com.java.mod.dto;

import com.java.mod.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

/**
 * JwtToken 类表示 JSON Web Tokens 的数据模型
 * 用于存储 JWT 相关的信息
 */
@Data
public class JwtToken {
    private String id;//令牌的唯一标识符

    @Size(max = 255, message = "token cannot exceed 255 characters.")
    private String token;//token字符串

    private Date createdTime;//令牌的创建日期，在插入时由数据库自动填充为当前时间

    private Date expirationTime;//令牌的过期日期

    @Size(max = 50, message = "user_id cannot exceed 50 characters.")
    private String userId;//与令牌关联的用户ID

    private Role role;//与令牌关联的用户角色身份
}