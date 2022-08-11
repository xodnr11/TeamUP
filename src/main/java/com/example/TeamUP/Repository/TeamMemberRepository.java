package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.TeamMember;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByTeam_Id(Long team_id);

    List<TeamMember> findByUserInfo(UserInfo userInfo);
}