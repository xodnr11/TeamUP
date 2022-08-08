package com.example.TeamUP.Auth.OAuth;

public interface OAuth2UserInfo {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getGender();

    String getPhone();

    String getBirthday();

    String getBirthyear();

    String getNickname();
}
