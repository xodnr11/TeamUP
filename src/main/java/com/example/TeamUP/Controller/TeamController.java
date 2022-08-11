package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.DTO.ResponseBoardDTO;
import com.example.TeamUP.DTO.ResponsePostDTO;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.TeamMember;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.TeamMemberRepository;
import com.example.TeamUP.Repository.TeamRepository;
import com.example.TeamUP.Repository.UserRepository;
import com.example.TeamUP.Service.TeamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final TeamServiceImpl teamService;

    @PostMapping("/api/post/complete")
    public ResponseEntity<?> responseCreateTeam(
            @RequestBody RequestCreateTeamDTO teamInfo,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println("팀 생성 요청 : " + teamInfo.getTitle());
        System.out.println("팀 생성 요청자 : " + principalDetails.getUserInfo().getUsername());

        Team team = teamService.createTeam(teamInfo, principalDetails.getUserInfo());

        teamService.insertTag(team, teamInfo.getTags());

        return ResponseEntity.ok("팀 생성 완료");
    }

    @GetMapping("/api/post")
    public ResponseEntity<?> responsePostInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("teamId") Long teamId) {

        Optional<Team> team = teamRepository.findById(teamId);
        String writer = userRepository.findById(team.get().getUserInfo().getId()).get().getNickname();
        ResponsePostDTO responsePostDTO = new ResponsePostDTO();

        boolean registered = false;
        //DTO에 담을거
        List<Map<String, Object>> memberList = new ArrayList<>();

        //팀 멤버 찾아온거
        List<TeamMember> members = teamMemberRepository.findAllByTeam_Id(teamId);

        for (TeamMember m : members) {
            //잠깐 담을거
            Map<String, Object> member = new HashMap<>();
            member.put("user_id", m.getUserInfo().getId());
            member.put("nickname", m.getUserInfo().getNickname());
            member.put("role", m.getRole());

            memberList.add(member);

            if (member.containsValue(principalDetails.getUserInfo().getId())) {
                registered = true;
            }
        }

        responsePostDTO.setTeamId(teamId);
        responsePostDTO.setTitle(team.get().getTitle());
        responsePostDTO.setContent(team.get().getContent());
        responsePostDTO.setWriter(writer);
        responsePostDTO.setMax_member(team.get().getMaxMember());
        responsePostDTO.setCurrent_member(team.get().getCurrentMember());
        responsePostDTO.setTeam_member(memberList);
        responsePostDTO.setRegistered(registered);

        return ResponseEntity.ok(responsePostDTO);
    }

    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> registerTeam(@RequestBody Map<String, Object> map,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserInfo userInfo = principalDetails.getUserInfo();
        if (teamService.createTeamRegister(map, userInfo)) {
            return ResponseEntity.ok("신청 내용 수정완료");
        } else {
            return ResponseEntity.ok("팀 신청 완료");
        }
    }

    @GetMapping("api/board")
    public ResponseEntity<?> responseBoard(@PageableDefault(size = 10, sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Team> teamList = teamService.getTeamList(pageable);
        ResponseBoardDTO responseBoardDTO = new ResponseBoardDTO();

        responseBoardDTO.setTotal_page(teamList.getTotalPages());       //전체 페이지

        List<Map<String, Object>> posts = new ArrayList<>();

        for (int i = 0; i < teamList.getContent().size(); i++) {

            Map<String, Object> map = new HashMap<>();
            map.put("team_id", teamList.getContent().get(i).getId());
            map.put("title", teamList.getContent().get(i).getTitle());
            map.put("writer", teamList.getContent().get(i).getUserInfo().getNickname());
            posts.add(map);

        }

        responseBoardDTO.setPosts(posts);

        return ResponseEntity.ok(responseBoardDTO);
    }
}
