package com.java.mod.security;

import com.java.mod.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

/**
 * JWT Token提供者类，用于生成和验证JWT Tokens。
 * 实现了ApplicationRunner接口，以便在应用程序启动时初始化密钥
 */
@Component
@Slf4j
public class JwtTokenProvider implements ApplicationRunner {
    private static final String PREFIX = "Bearer ";
    private static final String ROLE = "role ";
    private static final String USER_ID = "userId ";

    private Key key = null;

    //将密钥配置在配置文件中（yml/properties）
    @Value("${jwt.secret}")
    private String jwtSecret;

    //将密钥配置在配置文件中（yml/properties）
    @Value("${jwt.token.expiration.time:360000}")
    private Long expirationTime;

    public Date getExpiration(){
        LocalDateTime localDateTime = LocalDateTime.now();
        long nowMillis = localDateTime.toInstant(ZoneOffset.of("+0")).toEpochMilli();
        return new Date(nowMillis + expirationTime);
    }

    public void setExpirationTime(Date date){
        expirationTime = date.getTime();
    }

    public String generateToken(User user) {
        //使用当前时间作为签发时间
        LocalDateTime localDateTime = LocalDateTime.now();
        long nowMillis = localDateTime.toInstant(ZoneOffset.of("+0")).toEpochMilli();
        Date now = new Date(nowMillis);

        // 计算过期时间
        Date expiration = new Date(nowMillis + expirationTime);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(USER_ID, user.getUserId())
                .claim(ROLE, user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) throws Exception {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody();
        } catch (Exception e) {
            log.error("Invalid JWT token,token:{}", token, e);
            throw new Exception("Invalid JWT token", e);
        }
    }
    public boolean validateToken(String token) {
        try {
            //使用当前时间作为签发时间
            LocalDateTime localDateTime = LocalDateTime.now();
            long nowMillis = localDateTime.toInstant(ZoneOffset.of("+0")).toEpochMilli();

            Claims claims = getClaims(token);
            return claims != null && !claims.getExpiration().before(new Date(nowMillis));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 使用Base64编码的密钥以增强安全性
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Base64.getDecoder().decode(jwtSecret);
        } catch (Exception e) {
            log.error("Base64编码加密密钥失败", e);
            throw e;
        }
        key = Keys.hmacShaKeyFor(keyBytes);
    }
}