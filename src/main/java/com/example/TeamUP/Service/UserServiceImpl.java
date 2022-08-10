package com.example.TeamUP.Service;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Token updateRefreshToken(Long userId, String token) {
        System.out.println("유저 서비스의 리스레쉬 토큰 id 값 확인 : " + userId);
        System.out.println("리프레쉬 토큰 값 확인 : " + token);

        Optional<UserInfo> userInfo = userRepository.findById(userId);
        System.out.println("유저 서비스의 유저인포 값 확인"+userInfo.get());

        TokenServiceImpl tokenService = new TokenServiceImpl();

        Token returnToken = tokenService.generateToken(userId,"ROLE_USER");
        userInfo.get().setRefreshtoken("Bearer "+returnToken.getRefreshToken());
        userRepository.save(userInfo.get());
        return returnToken;
    }

    @Override
    public boolean join(UserInfo userInfo) {
        if (userRepository.existsByUsername(userInfo.getUsername())) {
            return false;
        }else {
            System.out.println(userInfo);
            userInfo.setRole(Role.USER);
            userInfo.setRefreshtoken("123123");
            userInfo.setEmail("123123");
            userInfo.setGender('m');
            userInfo.setName("name");
            userInfo.setNickname("niname");
            userInfo.setPhone("0101");
            userInfo.setBirthday(new Date(2020-10-10));
            String rawPassword = userInfo.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            userInfo.setPassword(encPassword);

            userRepository.save(userInfo);
            return true;
        }
    }

    @Override
    public void updateUserInformation(UserInfo userInfo,
                                      PrincipalDetails principalDetails) {
        principalDetails.getUserInfo().setNickname(userInfo.getNickname());
        userRepository.save(principalDetails.getUserInfo());
    }
}
