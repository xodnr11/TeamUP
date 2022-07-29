package com.example.TeamUP.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRequestMapper {
    public UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        UserDto userDto = UserDto.builder()
                .id((String)attributes.get("id"))
                .nickname((String)attributes.get("nickname"))
                .birthday((String)attributes.get("birthday"))
                .birthyear((String)attributes.get("birthyear"))
                .gender((String)attributes.get("gender"))
                .mobile((String)attributes.get("mobile"))
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .build();

        log.info("UserRequestMapper 실행 위치 확인 : "+userDto.getId());
        return userDto;
    }
/*
    public UserFindRequest toFindDto(UserDto userDto) {
        return new UserFindRequest(userDto.getEmail());
    }
*/
}