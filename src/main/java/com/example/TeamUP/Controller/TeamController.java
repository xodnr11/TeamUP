package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.Tag;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Repository.TagRepository;
import com.example.TeamUP.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final TagRepository tagRepository;

    @PostMapping("/api/post/complete")
    public void createTeam(
            @RequestBody RequestCreateTeamDTO teamInfo,
            @AuthenticationPrincipal Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        System.out.println("팀 생성 요청 : " + teamInfo.getTitle());
        System.out.println("팀 생성 요청자 : " + principalDetails.getUserInfo().getUsername());

        Team team = new Team();

        team.setTeamName(teamInfo.getTeamName());
        team.setUserInfo(principalDetails.getUserInfo());
        team.setTeamPresent(teamInfo.getPresent());
        team.setCategory(teamInfo.getCategory());
        team.setContent(teamInfo.getContent());
        team.setMaxMember(teamInfo.getMax_mamber());
        team.setCurrentMember(1);

        teamRepository.save(team);

        List<Tag> tags = new ArrayList<>();

        for (String tag :teamInfo.getTags()){
            Tag teamTag = Tag.builder()
                    .team(team)
                    .tagName(tag)
                    .build();

            tags.add(teamTag);
        }

        tagRepository.saveAll(tags);
    }


}
