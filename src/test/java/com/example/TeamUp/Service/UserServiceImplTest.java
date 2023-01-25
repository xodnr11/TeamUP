package com.example.TeamUp.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.read.TeamReadRepository;
import com.example.TeamUP.Repository.UserRepository;
import com.example.TeamUP.Service.TeamService;
import com.example.TeamUP.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    TeamService teamService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamReadRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원 추가 테스트")
    void addUserInfoTest() {
        //given
        UserInfo userInfo = new UserInfo();
        String username = "성성성";

        userInfo.setUsername(username);
        userInfo.setPassword("pass");

        //when
        userService.join(userInfo);
        em.clear();


        //then
        Assertions.assertEquals(true, userRepository.existsByUsername(username));
    }

    @Test
    @DisplayName("회원정보 삭제")
    public void deleteUserInfo() {
        // given
        UserInfo userInfo = new UserInfo();
        String username = "성성성";

        userInfo.setUsername(username);
        userInfo.setPassword("pass");
        userService.join(userInfo);

        RequestCreateTeamDTO requestCreateTeamDTO = new RequestCreateTeamDTO();
        String teamTitle = "testTitle";
        requestCreateTeamDTO.setTitle(teamTitle);

        teamService.createTeam(requestCreateTeamDTO, userInfo);
        em.clear();

        UserInfo findUserInfo = userRepository.findByUsername(username);

        // when
        userService.deleteUserInfo(findUserInfo);

        // then
        Assertions.assertEquals(userRepository.existsByUsername(username), false);
    }
}