package com.example.TeamUP.Service;

import com.example.TeamUP.Config.Token;

public interface UserService {
    public Token updateRefreshToken(Long userId, String token);
}
