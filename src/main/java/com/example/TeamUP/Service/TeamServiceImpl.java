package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.*;
import com.example.TeamUP.Repository.TagRepository;
import com.example.TeamUP.Repository.TeamRegisterRepository;
import com.example.TeamUP.Repository.TeamRepository;
import com.example.TeamUP.Repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRegisterRepository teamRegisterRepository;

    private final TagRepository tagRepository;

    @Override
    public void joinTeam(Team team, UserInfo userInfo, Role role) {

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setUserInfo(userInfo);
        teamMember.setRole(role.getKey());

        teamMemberRepository.save(teamMember);
    }

    @Override
    public Team createTeam(RequestCreateTeamDTO team, UserInfo user) {

        Team teamEntity = new Team();

        teamEntity.setTeamName(team.getTeamName());
        teamEntity.setTitle(team.getTitle());
        teamEntity.setUserInfo(user);
        teamEntity.setTeamPresent(team.getPresent());
        teamEntity.setCategory(team.getCategory());
        teamEntity.setContent(team.getContent());
        teamEntity.setMaxMember(team.getMax_member());
        teamEntity.setCurrentMember(1);

        teamRepository.save(teamEntity);

        joinTeam(teamEntity, user, Role.MASTER);

        return teamEntity;
    }

    @Override
    public void insertTag(Team team, List<Map<String, String>> rawTags) {

        List<Tag> tags = new ArrayList<>();

        for (Map<String, String> tag :rawTags){
            Tag teamTag = Tag.builder()
                    .team(team)
                    .tagName(tag.get("tag"))
                    .build();

            tags.add(teamTag);
        }

        tagRepository.saveAll(tags);
    }

    @Override
    public boolean createTeamRegister(Map<String, Object> map,UserInfo userInfo) {

        Optional<Team> team = teamRepository.findById(Long.valueOf((String) map.get("teamId")));
        TeamRegister teamRegister = teamRegisterRepository.findByUserInfo(userInfo);

        if (teamRegister != null) {                                 //이미 신청한 이력이 있다면
            teamRegister.setContent((String) map.get("content"));
            teamRegisterRepository.save(teamRegister);

            return true;
        } else {                                                    //처음 신청이라면
            teamRegister = TeamRegister.builder()
                    .team(team.get())
                    .userInfo(userInfo)
                    .content((String) map.get("content"))
                    .build();
            teamRegisterRepository.save(teamRegister);

            return false;
        }

    }

    @Override
    public List<Map<String, Object>> findMyTeams(UserInfo userInfo) {

        List<TeamMember> myTeams = teamMemberRepository.findByUserInfo(userInfo);

        List<Map<String, Object>> teamList = new ArrayList<>();

        if (myTeams != null) {

            for(int i=0;i<myTeams.size();i++){

                Map<String, Object> map = new HashMap<>();
                map.put("teamId", myTeams.get(i).getTeam().getId());
                map.put("title", myTeams.get(i).getTeam().getTitle());
                teamList.add(map);

            }
            return teamList;

        } else {

            return null;
        }
    }
}
