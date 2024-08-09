package com.java.mod.mapper;


import com.java.mod.dto.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {
    /**
     * 插入新用户到数据库
     *
     * @param user 要插入的用户对象
     * @return 影响的行数，>0则代表成功
     */
    @Insert("INSERT INTO table_users (user_id, username, password, register_date, login_recent, role) VALUES (#{userId}, #{username}, #{password}, #{registerDate},#{loginRecent}, #{role})")
    int insertUser(User user);

    /**
     * 根据用户userId删除用户
     *
     * @param userId 用户的唯一标识符
     * @return 影响的行数，>0则代表成功
     */
    @Delete("DELETE FROM table_users WHERE user_id = #{userId}")
    int deleteUser(String userId);

    /**
     * 更新用户信息，包括密码和角色
     *
     * @param user 包含要更新信息的用户对象
     * @return 影响的行数，>0则代表成功
     */
    @Update("UPDATE table_users SET username = #{username}, password = #{password},login_recent = #{loginRecent} WHERE user_id = #{userId}")
    int updateUser(User user);

    /**
     * 更新用户角色身份
     *
     * @param user 包含要更新信息的用户对象
     * @return 影响的行数，>0则代表成功
     */
    @Update("UPDATE table_users SET role = #{role} WHERE user_id = #{userId}")
    int updateUserRole(User user);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 匹配用户名的用户对象，如果未找到则返回null
     */
    @Select("SELECT user_id, username, password, register_date, login_recent, role FROM table_users WHERE username = #{username}")
    @Results(id = "getUserByUsername", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "registerDate", column = "register_date"),
            @Result(property = "loginRecent", column = "login_recent"),
    })
    User getUserByUsername(String username);

    /**
     * 根据用户userId查找用户
     *
     * @param userId 用户的唯一标识符
     * @return 匹配userId的用户对象，如果未找到则返回null
     */
    @Select("SELECT user_id, username, password, register_date, login_recent, role FROM table_users WHERE user_id = #{userId}")
    @Results(id = "getUserByUserId", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "registerDate", column = "register_date"),
            @Result(property = "loginRecent", column = "login_recent"),
    })
    User getUserByUserId(String userId);

    /**
     * 获取所有用户列表
     *
     * @return 包含所有用户的列表
     */
    @Select("SELECT user_id, username, password, register_date, login_recent, role FROM table_users")
    @Results(id = "getAllUsers", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "registerDate", column = "register_date"),
            @Result(property = "loginRecent", column = "login_recent"),
    })
    List<User> getAllUsers();
}
