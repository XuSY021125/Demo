package com.java.mod.service.impl;

import com.java.mod.dto.JwtToken;
import com.java.mod.dto.User;
import com.java.mod.enums.Role;
import com.java.mod.mapper.JwtTokenMapper;
import com.java.mod.mapper.UserMapper;
import com.java.mod.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * JWT Token服务实现类
 * 提供与JWT Token相关的业务逻辑操作
 */
@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    @Autowired
    private JwtTokenMapper jwtTokenMapper;

    @Autowired
    private UserMapper userMapper;
    /**
     * 添加JWT Token到数据库
     *
     * @param jwtToken 要添加的JWT Token对象
     */
    @Override
    public void saveToken(JwtToken jwtToken) {
        if (jwtToken.getRole() == Role.USER) {
            jwtTokenMapper.insertUserToken(jwtToken);
        } else if (jwtToken.getRole() == Role.ADMIN) {
            jwtTokenMapper.insertAdminToken(jwtToken);
        }
    }

    public void deleteToken(String userId){
        User user = userMapper.getUserByUserId(userId);
        if (user.getRole().name().equalsIgnoreCase("admin")) {
            jwtTokenMapper.deleteAdminToken(userId);
        }
        jwtTokenMapper.deleteUserToken(userId);
    }

    /**
     * 根据Token字符串查询管理员的JWT Token
     *
     * @param token Token字符串
     * @return 查找到的管理员JWT Token对象，如果没有找到则返回null
     */
    @Override
    public JwtToken getAdminTokenByToken(String  token) {
        return jwtTokenMapper.getAdminTokenByToken(token);
    }
}
