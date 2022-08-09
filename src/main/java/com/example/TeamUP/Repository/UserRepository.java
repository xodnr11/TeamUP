package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    Boolean existsByUsername(String username);

    UserInfo findByUsername(String username);

    UserInfo save(UserInfo userInfo);
}
