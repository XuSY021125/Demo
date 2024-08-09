package com.java.mod.apiResponse;


import lombok.Data;

/**
 * 通用的API响应类，用于封装HTTP响应数据
 *
 * @param <T> 响应数据的具体类型
 */
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    /**
     * 构造函数，用于创建一个新的响应对象
     *
     * @param code    响应码
     * @param message 响应消息
     * @param data    响应数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
