package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TeamService {
    Team createTeam(RequestCreateTeamDTO team, UserInfo user);
    void insertTag(Team team, List<Map<String,String>> rawTags);
    void joinTeam(Team team, UserInfo userInfo, Role role);

    boolean createTeamRegister(Map<String, Object> map,UserInfo userInfo);

    List<Map<String, Object>> getMyTeams(UserInfo userInfo);
    Page<Team> getTeamList(Pageable pageable);
}
