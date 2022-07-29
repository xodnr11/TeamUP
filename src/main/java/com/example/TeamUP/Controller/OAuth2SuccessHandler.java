package com.example.TeamUP.Controller;

import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import com.example.TeamUP.Service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //userDto를 만들지 않으면 oAuth2User.getAttributes().get("id").toString() 형식으로 하나하나 뽑아야함
        UserDto userDto = userRequestMapper.toDto(oAuth2User);                                      //UserRequestMapper로 유저정보 저장

        //소셜 로그인에서 제공하는 id값 호출
        String nameValidation = ((OAuth2User) authentication.getPrincipal()).getName();
        log.info("authentication principal getName() 값 확인 : " + nameValidation);

        UserInfo userInfo = new UserInfo();
        // 최초 로그인이라면 회원가입 처리를 한다.
        if (!userRepository.existsByOauth2id(nameValidation)) {
            Role role = Role.USER;                                                                  //유저 역할 enum
            String birthYearAddbirthDay = userDto.getBirthyear() + "-" + userDto.getBirthday();     //태어난 년과 생일을 합침
            char gender = userDto.getGender().charAt(0);                                            //젠더 문자형으로 변환
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");                //날짜 날짜 형식 지정
            Date BirthYearAddbirthDay = null;
            try {
                BirthYearAddbirthDay = formatter.parse(birthYearAddbirthDay);                       //날짜 형식으로 변환
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userInfo = new UserInfo(userDto.getId(), gender, userDto.getNickname(),
                    userDto.getEmail(), userDto.getName(), userDto.getMobile(), BirthYearAddbirthDay, role);
            userRepository.save(userInfo);                                                          //유저인포 테이블 저장
        }else {
            //유저 정보 업데이트 필요
        }

        Authentication auth = getAuthentication(userInfo);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Token token = tokenService.generateToken(userInfo.getId(), "USER");                   //토큰 생성
        log.info("{}", token);                                                                       //토큰 값 확인

        writeTokenResponse(response, token);                                                         //토큰 전달(?)
    }

    private void writeTokenResponse(HttpServletResponse response, Token token)                      //의미 확인 필요
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", ""+token.getToken());                            //토큰약속 문자열 Bearer 포함 해야함
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }

    //Authentication 객체에 담아서 자체적으로 전역으로 불러올 수 있도록 담음
    public Authentication getAuthentication(UserInfo member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}