package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.*;
import com.example.TeamUP.Repository.TagRepository;
import com.example.TeamUP.Repository.TeamRepository;
import com.example.TeamUP.Repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public void joinTeam(Team team, UserInfo userInfo, Role role) {
        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setUserInfo(userInfo);
        teamMember.setRole(role.getKey());

        teamMemberRepository.save(teamMember);
    }

    private final TagRepository tagRepository;
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
}
