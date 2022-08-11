package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.DTO.ResponseBoardDTO;
import com.example.TeamUP.DTO.ResponsePostDTO;
import com.example.TeamUP.Entity.Calendar;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.TeamMember;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.TagRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamServiceImpl teamService;

    @PostMapping("/api/post/complete")
    public ResponseEntity<?> responseCreateTeam(
            @RequestBody RequestCreateTeamDTO teamInfo,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Team team = teamService.createTeam(teamInfo, principalDetails.getUserInfo());

        teamService.insertTag(team, teamInfo.getTags());

        return ResponseEntity.ok("팀 생성 완료");
    }

    @GetMapping("/api/post")
    public ResponseEntity<?> responsePostInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("teamId") Long teamId){

        Long userId = principalDetails.getUserInfo().getId();

        return ResponseEntity.ok(teamService.getPostInfo(userId, teamId));
    }

    @GetMapping("/api/team")
    public ResponseEntity<?> responseTeamInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("teamId") Long teamId) {

        Long userId = principalDetails.getUserInfo().getId();

        return ResponseEntity.ok(teamService.getTeamInfo(userId, teamId));
    }

    @PostMapping("/api/team/calendar/create")
    public ResponseEntity<?> responseCreateCalendar(
            @RequestBody Calendar calendar,
            @RequestParam("teamId") Long teamId) {

        teamService.createCalendar(teamId, calendar);

        return ResponseEntity.ok("일정 생성 완료");
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
