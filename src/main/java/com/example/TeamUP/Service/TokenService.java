package com.example.TeamUP.Service;

import com.example.TeamUP.Config.Token;

public interface TokenService {
    Token generateToken(Long uid, String role);

    boolean verifyToken(String token);

    String getUid(String token);
}
