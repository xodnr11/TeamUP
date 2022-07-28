package com.example.TeamUP.Controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Getter
public class UserDto {
    private String id;
    private String nickname;
    private String gender;
    private String mobile;
    private String phone;
    private String birthday;
    private String birthyear;
    private String email;
    private String name;

    @Builder
    public UserDto(String id, String nickname, String gender, String mobile, String birthday, String birthyear, String email, String name, String phone) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.mobile = mobile;
        this.birthday = birthday;
        this.birthyear = birthyear;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}

