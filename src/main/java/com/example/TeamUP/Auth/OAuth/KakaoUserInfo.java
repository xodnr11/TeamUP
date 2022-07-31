package com.example.TeamUP.Auth.OAuth;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    private Map<String, Object> kakao_account;
    private Map<String, Object> kakao_profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        kakao_profile = (Map<String, Object>) kakao_account.get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakao_account.get("email")); //카카오 어카운트 안에
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getGender() {
        return String.valueOf(kakao_account.get("gender"));    //카카오 어카운트 안에
    }

    @Override
    public String getPhone() {
        return String.valueOf(attributes.get("phone_number"));
    }

    @Override
    public String getBirthday() {
        return String.valueOf(kakao_account.get("birthday"));  //카카오 어카운트 안에
    }

    @Override
    public String getBirthyear() {
        return String.valueOf(attributes.get("birthyear"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(kakao_profile.get("nickname"));    //프로필 안에 닉네임
    }

    @Override
    public String toString() {
        return "KakaoUserInfo{" +
                "attributes=" + attributes +
                '}';
    }
}
