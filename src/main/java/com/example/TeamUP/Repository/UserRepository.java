package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

    Boolean existsByOauth2id(String oauthid);

    UserInfo findByOauth2id(String oauthid);

    UserInfo save(UserInfo userInfo);
}
