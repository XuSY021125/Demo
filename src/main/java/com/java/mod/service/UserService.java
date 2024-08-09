package com.java.mod.service;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.JwtToken;
import com.java.mod.dto.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserByUsername(String username);
    User getUserByUserId(String userId);
    List<User> getAllUsers();
    User insertUser(User user);
    User updateUser(String userId, User user);
    void updateUerRole(User user);
    boolean deleteUser(String userId);
    Boolean register(String username, String password);
    ApiResponse<Map<String, Object>> login(String username, String password);
    ApiResponse<Map<String, Object>> loginAnonymous(String username);

    String  generateToken(User user);
}
