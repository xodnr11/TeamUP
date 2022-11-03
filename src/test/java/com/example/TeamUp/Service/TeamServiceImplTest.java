package com.example.TeamUp.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.TeamRepository;
import com.example.TeamUP.Service.TeamService;
import com.example.TeamUP.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    TeamService teamService;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager em;

    @Test
    void deleteTeam() {

    }

    @Test
    @DisplayName("팀 생성 테스트")
    public void createTeamTest() {
        // given
        UserInfo userInfo = new UserInfo();
        String username = "성성성";

        userInfo.setUsername(username);
        userInfo.setPassword("pass");
        userService.join(userInfo);

        RequestCreateTeamDTO requestCreateTeamDTO = new RequestCreateTeamDTO();
        String teamTitle = "testTitle";
        requestCreateTeamDTO.setTitle(teamTitle);
        // when
        teamService.createTeam(requestCreateTeamDTO, userInfo);

        // then
        teamRepository.findByTitleContains(teamTitle);
    }

    @Test
    @DisplayName("팀 삭제 테스트")
    public void deleteTeamTest() {
        //given
        UserInfo userInfo = new UserInfo();
        String username = "성성성";

        userInfo.setUsername(username);
        userInfo.setPassword("pass");
        userService.join(userInfo);

        RequestCreateTeamDTO requestCreateTeamDTO = new RequestCreateTeamDTO();
        String teamTitle = "ss";
        requestCreateTeamDTO.setTitle(teamTitle);
        teamService.createTeam(requestCreateTeamDTO, userInfo);
        em.clear();

        Team findTeam = teamRepository.findByTitleContains(teamTitle);
        Long teamId = findTeam.getId();
        //when
        teamService.deleteTeam(teamId);

        //then
        Assertions.assertEquals(false,teamRepository.existsById(teamId));
    }
}