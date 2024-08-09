package com.java.mod.aspect;

import com.java.mod.dto.JwtToken;
import com.java.mod.dto.User;
import com.java.mod.security.JwtTokenProvider;
import com.java.mod.service.JwtTokenService;
import com.java.mod.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;


/**
 * CheckJwtAspect 类是一个切面（Aspect），用于在调用AdminController中的方法前检查JWT令牌的有效性
 */
@Aspect
@Component
public class CheckJwtAspect {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    /**
     * 环绕通知方法，用于在目标方法执行前后执行额外的操作
     * 这个方法会在所有匹配的方法执行之前运行，检查JWT令牌的有效性
     *
     * @param joinPoint 代表正在执行的方法调用
     * @return 方法执行的结果或者错误响应
     * @throws Throwable 如果方法执行过程中抛出异常
     */
//    @Around("@annotation(com.java.mod.annotation.Authentication)")
    @Around("execution(* com.java.mod.controller.AdminController.*(..))")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        // 检查JWT令牌是否存在
        if (token == null || jwtTokenService.getAdminTokenByToken(token) == null) {
            return ResponseEntity.status(401).body("Unauthorized access");
        }
        // 检查JWT令牌是否有效
        if (!jwtTokenProvider.validateToken(token)){
            return ResponseEntity.status(401).body("Login status is invalid, please login again");
        }

        Claims claims = jwtTokenProvider.getClaims(token);
        // 验证用户角色是否为ADMIN
        if (claims.containsKey("role ") && "ADMIN".equals(claims.get("role "))) {

            Object proceed = joinPoint.proceed();

            String userId = (String) claims.get("userId ");
            User user = userService.getUserByUserId(userId);

            jwtTokenService.deleteToken(userId);
            String newToken = jwtTokenProvider.generateToken(user);
            // 创建JwtToken对象
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(newToken);
            jwtToken.setUserId(user.getUserId());
//          jwtToken.setCreatedTime(new Date());
            jwtToken.setExpirationTime(jwtTokenProvider.getExpiration());
            jwtToken.setRole(user.getRole());
            jwtTokenService.saveToken(jwtToken);

            ResponseEntity<?> response = (ResponseEntity<?>) proceed;

            // 添加新的JWT令牌到响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, newToken);

            return ResponseEntity.status(response.getStatusCode()).headers(headers).body(response.getBody());
        } else {
            return ResponseEntity.status(403).body("Forbidden: ADMIN role required");
        }
    }
}