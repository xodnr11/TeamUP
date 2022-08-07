package com.example.TeamUP.Service;

import com.example.TeamUP.Auth.OAuth.KakaoUserInfo;
import com.example.TeamUP.Auth.OAuth.NaverUserInfo;
import com.example.TeamUP.Auth.OAuth.OAuth2UserInfo;
import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {            //OAuth2를 통해 회원정보 불러오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            log.info("네이버 오스 전체 값 확인 " + oAuth2UserInfo.toString());
            log.info("네이버 로그인 중");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            log.info("카카오 오스 전체 값 확인 " + oAuth2UserInfo.toString());
            log.info("카카오 로그인 중");
        } else {
            log.info("이게 작동 ?");
        }

        log.info("겟 젠더 확인 "+oAuth2UserInfo.getGender());

        //소셜 로그인에서 제공하는 회원정보 파싱
        String nameValidation = oAuth2UserInfo.getProviderId();
        String socialName = oAuth2UserInfo.getProvider();
        log.info("authentication principal getName() 값 확인 : " + nameValidation);
        String username = socialName+"_"+nameValidation;
        char gender = oAuth2UserInfo.getGender().charAt(0);                                            //젠더 문자형으로 변환
        String birth = oAuth2UserInfo.getBirthyear()+"-"+oAuth2UserInfo.getBirthday();                  //태어난 년과 생일을 합침
        String nickname = oAuth2UserInfo.getNickname();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String phone = oAuth2UserInfo.getPhone();
        Role role = Role.USER;                                                                          //유저 역할 enum

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");                        //날짜 날짜 형식 지정
        Date BirthYearAddBirthDay = null;
        try {
            BirthYearAddBirthDay = formatter.parse(birth);                       //날짜 형식으로 변환
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserInfo userInfo = UserInfo.builder()
                .username(username)
                .gender(gender)
                .nickname(nickname)
                .email(email)
                .name(name)
                .phone(phone)
                .birthday(BirthYearAddBirthDay)
                .role(role)
                .build();

        return PrincipalDetails.builder()
                .userInfo(userInfo)
                .build();
    }

}