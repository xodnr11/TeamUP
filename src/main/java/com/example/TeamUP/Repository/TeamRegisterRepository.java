package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.TeamRegister;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRegisterRepository extends JpaRepository<TeamRegister, Long > {
    List<TeamRegister> findByTeam_Id(Long id);
    TeamRegister findByUserInfo(UserInfo userInfo);

    TeamRegister findByTeam_IdAndUserInfo_Id(Long team_id, Long userInfo_id);
    boolean existsByTeam_idAndUserInfo_id(Long team_id, Long userInfo_id);
}
