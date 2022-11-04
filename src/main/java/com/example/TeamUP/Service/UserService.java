package com.example.TeamUP.Service;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Config.Token;
import com.example.TeamUP.DTO.ResponseUserInfoDTO;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface UserService {
    Token updateRefreshToken(Long userId, String token);

    boolean join(UserInfo userInfo);

    void updateUserInformation(UserInfo userInfo, @AuthenticationPrincipal PrincipalDetails principalDetails);

    ResponseUserInfoDTO getUserInfo(UserInfo userInfo);

    void deleteUserInfo(UserInfo userInfo);

}
