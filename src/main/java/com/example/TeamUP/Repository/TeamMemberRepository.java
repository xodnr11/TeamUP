package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.TeamMember;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByTeam_Id(Long team_id);

    //엔티티 그래프로 함께 조회 = join과 같음
    @EntityGraph(attributePaths = {"userInfo","team"}, type = EntityGraph.EntityGraphType.LOAD)
    List<TeamMember> findByUserInfo(UserInfo userInfo);

    TeamMember findByUserInfo_IdAndTeam_Id(Long userInfo_id, Long team_id);

    boolean existsByTeam_IdAndUserInfo_Id(Long team_id, Long userInfo_id);
}

