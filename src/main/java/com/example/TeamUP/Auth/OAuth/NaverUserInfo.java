package com.example.TeamUP.Auth.OAuth;

import lombok.ToString;

import java.util.Map;

@ToString
public class NaverUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getGender() {
        return (String) attributes.get("gender");
    }

    @Override
    public String getPhone() {
        return (String) attributes.get("mobile");
    }

    @Override
    public String getBirthday() {
        return (String) attributes.get("birthday");
    }

    @Override
    public String getBirthyear() {
        return (String) attributes.get("birthyear");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("nickname");
    }


}
