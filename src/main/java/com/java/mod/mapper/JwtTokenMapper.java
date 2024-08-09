package com.java.mod.mapper;

import com.java.mod.dto.JwtToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JwtTokenMapper {

    @Insert("INSERT INTO user_tokens (user_id, token, expiration_time, role) VALUES (#{userId}, #{token}, #{expirationTime}, #{role})")
    void insertUserToken(JwtToken jwtToken);

    @Insert("INSERT INTO admin_tokens (user_id, token, expiration_time, role) VALUES (#{userId}, #{token}, #{expirationTime}, #{role})")
    void insertAdminToken(JwtToken jwtToken);

    @Delete("DELETE FROM admin_tokens WHERE user_id=#{userId}")
    void deleteAdminToken(String userId);

    @Delete("DELETE FROM user_tokens WHERE user_id=#{userId}")
    void deleteUserToken(String userId);

    @Select("SELECT * FROM user_tokens WHERE token = #{token}")
    @Results(id = "getUserTokenByToken", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "expirationTime", column = "expiration_time"),
    })
    JwtToken getUserTokenByToken(String token);

    @Select("SELECT * FROM user_tokens WHERE user_id=#{userId}")
    @Results(id = "getUserTokenByUserId", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "expirationTime", column = "expiration_time"),
    })
    JwtToken getUserTokenByUserId(String userId);

    @Select("SELECT * FROM admin_tokens WHERE token = #{token}")
    @Results(id = "getAdminTokenByToken", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "expirationTime", column = "expiration_time"),
    })
    JwtToken getAdminTokenByToken(String token);

    @Select("SELECT * FROM admin_tokens WHERE user_id = #{userId}")
    @Results(id = "getAdminTokenByUserId", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "expirationTime", column = "expiration_time"),
    })
    JwtToken getAdminTokenByUserId(String userId);
}