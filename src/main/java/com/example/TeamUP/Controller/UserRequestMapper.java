package com.example.TeamUP.Controller;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

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
        return userDto;
    }
/*
    public UserFindRequest toFindDto(UserDto userDto) {
        return new UserFindRequest(userDto.getEmail());
    }
*/

}
