package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByUserInfo(UserInfo userInfo);
}
