package com.example.TeamUP.Service;

import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public Token updateRefreshToken(Long userId, String token) {
        System.out.println("유저 서비스의 리스레쉬 토큰 id 값 확인 : " + userId);
        System.out.println("리프레쉬 토큰 값 확인 : " + token);

        Optional<UserInfo> userInfo = userRepository.findById(userId);
        System.out.println("유저 서비스의 유저인포 값 확인"+userInfo.get());

        TokenService tokenService = new TokenService();

        Token returnToken = tokenService.generateToken(userId,"ROLE_USER");
        userInfo.get().setRefreshtoken(returnToken.getRefreshToken());
        userRepository.save(userInfo.get());
        return returnToken;
    }

}
