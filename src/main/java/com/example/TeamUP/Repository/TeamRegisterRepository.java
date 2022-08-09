package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.TeamRegister;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRegisterRepository extends JpaRepository<TeamRegister, Long> {
    TeamRegister findByUserInfo(UserInfo userInfo);
}
