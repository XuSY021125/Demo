package com.java.mod.enums;

import lombok.Getter;

/**
 * Role 枚举类表示系统中的用户角色
 * 定义了两个角色：USER 和 ADMIN
 */
@Getter
public enum Role {
    ANONYMOUS(0),
    USER(1),
    ADMIN(2);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public static Role fromCode(int code) {
        for (Role role : Role.values()) {
            if (role.getCode() == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }
}
