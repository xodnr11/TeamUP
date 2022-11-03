package com.example.TeamUP.Service;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Config.Token;
import com.example.TeamUP.DTO.ResponseUserInfoDTO;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TeamService teamService;

    /**
     * 엑세스 토큰이 만료 됐을 때 리프레쉬 토큰을 활용하여 리프레쉬 토큰과 엑세스 토큰을 재발급 해주는 함수
     * @param userId
     * @param token
     * @return
     */
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

    /**
     * 회원가입의 기능을 동작하는 함수
     * @param userInfo
     * @return
     */
    @Override
    public boolean join(UserInfo userInfo) {
        if (userRepository.existsByUsername(userInfo.getUsername())) {
            return false;
        }else {
            System.out.println(userInfo);
            userInfo.setRole(Role.USER);
            userInfo.setRefreshtoken("Not issued");
            userInfo.setEmail(userInfo.getEmail());
            userInfo.setGender(userInfo.getGender());
            userInfo.setName(userInfo.getName());
            userInfo.setNickname(userInfo.getNickname());
            userInfo.setPhone(userInfo.getPhone());
            userInfo.setBirthday(userInfo.getBirthday());
            String rawPassword = userInfo.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            userInfo.setPassword(encPassword);

            userRepository.save(userInfo);
            return true;
        }
    }

    /**
     * 회원정보를 수정하는 기능을 동작하는 함수
     * @param userInfo
     * @param principalDetails
     */
    @Override
    public void updateUserInformation(UserInfo userInfo,
                                      PrincipalDetails principalDetails) {
        principalDetails.getUserInfo().setNickname(userInfo.getNickname());
        userRepository.save(principalDetails.getUserInfo());
    }

    /**
     * Mypage 회원정보 출력 함수
     * @param userInfo
     * @return
     */
    @Override
    public ResponseUserInfoDTO getUserInfo(UserInfo userInfo) {
        ResponseUserInfoDTO userInfoDTO = new ResponseUserInfoDTO();
        if (userInfo.getUsername().contains("naver")) {
            userInfoDTO.setUsername("NAVER 계정으로 로그인 중");
        } else if (userInfo.getUsername().contains("kakao")) {
            userInfoDTO.setUsername("KAKAO 계정으로 로그인 중");
        } else {
            userInfoDTO.setUsername(userInfo.getUsername());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.setTime(userInfo.getBirthday());

        // 하루 전
        cal.add(Calendar.DATE, +1);
        String birthday = dateFormat.format(cal.getTime());
        Date date = null;
        try {
            date = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        userInfoDTO.setUser_name(userInfo.getName());
        userInfoDTO.setUser_birthday(date);
        userInfoDTO.setUser_email(userInfo.getEmail());
        userInfoDTO.setUser_gender(userInfo.getGender());
        userInfoDTO.setUser_phone(userInfo.getPhone());
        userInfoDTO.setUser_nickname(userInfo.getNickname());

        List<Map<String, Object>> teamList = teamService.getMyTeams(userInfo);

        if (teamList != null) {

            userInfoDTO.setTeam(teamList);
            return userInfoDTO;
        } else {

            userInfoDTO.setTeam(null);
            return userInfoDTO;
        }
    }

    @Override
    @Transactional
    public void deleteUserInfo(UserInfo userInfo) {

        if (!userRepository.existsById(userInfo.getId())) {
            throw new IllegalStateException("회원정보가 없습니다");
        }

        userRepository.deleteById(userInfo.getId());

    }
}
