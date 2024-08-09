package com.java.mod.controller;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.entity.LoginRequest;
import com.java.mod.entity.RegisterRequest;
import com.java.mod.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * HomeController类用于处理与用户注册和登录的HTTP请求
 */
@RestController
@Slf4j
public class HomeController {

    @Autowired
    private UserService userService;//用户服务接口的实例，用于执行业务逻辑。

    /**
     * 处理用户注册的POST请求
     *
     * @param registerRequest 包含用户注册所需信息的对象，包括用户名和密码
     * @return 如果注册成功，返回一个带有成功消息的HTTP响应，状态码为200（OK）；
     *         如果注册失败，返回一个带有错误消息的HTTP响应，状态码为400（Bad Request）
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        Boolean flag = userService.register(username,password);
        if (flag){
            log.info("Successful registration request at {} for username: {}. ", LocalDateTime.now(), username);
            return ResponseEntity.ok("User registered successfully");
        }else {
            log.error("Failed registration request at {} for username: {}", LocalDateTime.now(), username);
            return ResponseEntity.badRequest().body("Registration failed! Please reenter.");
        }
    }

    /**
     * 处理用户登录的POST请求
     *
     * @param loginRequest 包含用户登录所需信息的对象，包括用户名和密码
     * @return 如果登录成功，返回一个带有成功消息的HTTP响应，状态码为200（OK）；
     *         如果登录失败，返回一个带有错误消息的HTTP响应，状态码为401（Unauthorized）
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        ApiResponse<Map<String, Object>> apiResponse = userService.login(username, password);
        if (apiResponse.getCode() == 401){
            return ResponseEntity.status(401).body(apiResponse);
        }
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/loginAnonymous")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginAnonymous(String username){
        ApiResponse<Map<String, Object>> apiResponse = userService.loginAnonymous(username);
        if (apiResponse.getCode() == 400){
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok(apiResponse);
    }
}