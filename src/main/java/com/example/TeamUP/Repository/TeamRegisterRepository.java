package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.TeamRegister;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRegisterRepository extends JpaRepository<TeamRegister, Long > {
    List<TeamRegister> findByTeam_Id(Long id);
    TeamRegister findByUserInfo(UserInfo userInfo);
}
