package com.example.TeamUP.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"), MASTER("ROLE_MASTER"), ADMIN("ROLE_ADMIN");

    private final String key;

}
