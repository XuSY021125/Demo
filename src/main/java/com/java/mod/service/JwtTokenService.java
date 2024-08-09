package com.java.mod.service;

import com.java.mod.dto.JwtToken;

public interface JwtTokenService {
    void saveToken(JwtToken jwtToken);

    void deleteToken(String userId);


    JwtToken getAdminTokenByToken(String token);
}
