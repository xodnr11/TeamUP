package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface TeamService {
    public Team createTeam(RequestCreateTeamDTO team, UserInfo user);
    public void insertTag(Team team, List<Map<String,String>> rawTags);
    public void joinTeam(Team team, UserInfo userInfo, Role role);

    public boolean createTeamRegister(Map<String, Object> map,UserInfo userInfo);

    public List<Map<String, Object>> findTeams(UserInfo userInfo);
}
