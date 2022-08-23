package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.DTO.ResponsePostDTO;
import com.example.TeamUP.DTO.ResponseTeamDTO;
import com.example.TeamUP.Entity.Calendar;
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
    void acceptMember(Long teamId, Long userId);
    void rejectMember(Long teamId, Long userId);
    ResponsePostDTO getPostInfo(Long id, Long teamId);
    ResponseTeamDTO getTeamInfo(Long userId, Long teamId);
    List<Map<String,Object>> getTeamMember(Long teamId);
    List<Map<String,Object>> getTeamCalendar(Long teamId);
    List<Map<String,Object>> getTeamRegister(Long teamId);
    String createCalendar(Long userId,Long teamId, Calendar calendar);
    String createTeamRegister(Map<String, Object> map,UserInfo userInfo);
    List<Map<String, Object>> getMyTeams(UserInfo userInfo);
    Page<Team> getTeamList(Pageable pageable);
}
