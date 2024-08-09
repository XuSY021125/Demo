package com.java.mod.service.impl;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.JwtToken;
import com.java.mod.dto.User;
import com.java.mod.enums.Role;
import com.java.mod.exceptions.ResourceNotFoundException;
import com.java.mod.mapper.JwtTokenMapper;
import com.java.mod.mapper.UserMapper;
import com.java.mod.security.JwtTokenProvider;
import com.java.mod.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 用户服务实现类，提供对用户相关操作的服务方法
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenMapper jwtTokenMapper;

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 查询到的User对象，如果没有找到则返回null
     */
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return 查询到的User对象，如果没有找到则返回null
     */
    public User getUserByUserId(String userId) {
        return userMapper.getUserByUserId(userId);
    }

    /**
     * 获取所有用户记录
     *
     * @return 所有的User对象列表
     */
    public List<User> getAllUsers(){
        return userMapper.getAllUsers();
    }

    /**
     * 插入新的用户记录
     *
     * @param user 要插入的User对象
     * @return 如果插入成功返回插入后的User对象，否则返回null
     */
    public User insertUser(User user) {
        String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setUserId(UUID.randomUUID().toString().replace("-", "").substring(0,11));
        user.setPassword(password);
        int result = userMapper.insertUser(user);
        if (result > 0) {
            return user;
        }
        return null;
    }

    /**
     * 更新用户记录
     *
     * @param userId 用户ID
     * @param user   要更新的User对象
     * @return 如果更新成功返回更新后的User对象，否则抛出ResourceNotFoundException
     */
    public User updateUser(String userId, User user) {
        User updateUser = userMapper.getUserByUserId(userId);
        if (updateUser == null){
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        updateUser.setUsername(user.getUsername());
        updateUser.setPassword(password);
        userMapper.updateUser(updateUser);
        return  updateUser;
    }

    /**
     * 更新用户的角色身份
     *
     * @param user 要更新角色的User对象
     */
    public void updateUerRole(User user) {

        userMapper.updateUserRole(user);
    }

    /**
     * 删除用户记录
     *
     * @param userId 用户ID
     * @return 如果删除成功返回true，否则返回false
     */
    public boolean deleteUser(String userId) {
        return userMapper.deleteUser(userId) > 0;
    }

    /**
     * 用户注册服务方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果注册成功返回true，否则返回false
     */
    public Boolean register(String username, String password) {
        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(username);
        if (existingUser != null) {
            // 用户名已存在，注册失败
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        long nowMillis = LocalDateTime.now().toInstant(ZoneOffset.of("+0")).toEpochMilli();
        // 创建新用户
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID().toString().replace("-", "").substring(0,11));
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setRegisterDate(new Date(nowMillis));
        newUser.setLoginRecent(null);
        newUser.setRole(Role.USER);
        // 插入新用户到数据库
        int rowsAffected = userMapper.insertUser(newUser);

        if (rowsAffected > 0) {
            // 插入成功
            return true;
        } else {
            // 插入失败
            return false;
        }
    }

    public ApiResponse<Map<String, Object>> login(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            return new ApiResponse<>(401, "Invalid username or password", null);
        }
        if (user.getRole().name().equalsIgnoreCase("anonymous")){
            return new ApiResponse<>(401, "Unregistered", null);
        }
        String token = generateToken(user);
        // 使用Map来构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("message", user.getRole().name().equalsIgnoreCase("admin") ? "Admin login successful" : "User login successful");
        response.put("token", token);

        log.info("Login successfully at {} for username: {}. Token generated: {}", LocalDateTime.now(), username, token);

        long nowMillis = LocalDateTime.now().toInstant(ZoneOffset.of("+0")).toEpochMilli();
        user.setLoginRecent(new Date(nowMillis));
        userMapper.updateUser(user);
        return new ApiResponse<>(200, "Success", response);
    }

    @Override
    public ApiResponse<Map<String, Object>> loginAnonymous(String username) {
        User user = userMapper.getUserByUsername(username);
        if (user != null && user.getRole().name().equalsIgnoreCase("anonymous")) {

            long nowMillis = LocalDateTime.now().toInstant(ZoneOffset.of("+0")).toEpochMilli();
            user.setLoginRecent(new Date(nowMillis));
            userMapper.updateUser(user);
            String token = generateToken(user);

            // 使用Map来构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Anonymous login successful");
            response.put("token", token);

            log.info("Login successfully at {} for username: {}. Token generated: {}", LocalDateTime.now(), username, token);

            return new ApiResponse<>(200, "Success", response);
        }
        // 创建新用户
        User newUser = new User();
        long nowMillis = LocalDateTime.now().toInstant(ZoneOffset.of("+0")).toEpochMilli();
        String password = BCrypt.hashpw(username, BCrypt.gensalt());
        newUser.setUserId(UUID.randomUUID().toString().replace("-", "").substring(0,11));
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRegisterDate(new Date(nowMillis));
        newUser.setLoginRecent(new Date(nowMillis));
        newUser.setRole(Role.ANONYMOUS);
        // 插入新用户到数据库
        int rowsAffected = userMapper.insertUser(newUser);
        if (rowsAffected > 0) {
            String jwtToken = generateToken(newUser);
            // 使用Map来构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Anonymous login successful");
            response.put("token", jwtToken);
            log.info("Login successfully at {} for username: {}. Token generated: {}", LocalDateTime.now(), username, jwtToken);

            // 插入成功
            return new ApiResponse<>(200, "Success", response);
        }
        return new ApiResponse<>(400, "Anonymous login failed", null);
    }

    @Override
    public String generateToken(User user) {
        String token = jwtTokenProvider.generateToken(user);
        // 创建JwtToken对象
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setUserId(user.getUserId());
//        jwtToken.setCreatedTime(new Date());
        jwtToken.setExpirationTime(jwtTokenProvider.getExpiration());
        jwtToken.setRole(user.getRole());
        if (jwtToken.getRole().name().equalsIgnoreCase("admin")){
            jwtTokenMapper.insertAdminToken(jwtToken);
            return token;
        }
        // 保存Token到数据库
        jwtTokenMapper.insertUserToken(jwtToken);
        return token;
    }
}

