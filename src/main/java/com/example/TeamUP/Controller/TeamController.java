package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.DTO.ResponseBoardDTO;
import com.example.TeamUP.Entity.Calendar;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;
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
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody Calendar calendar,
            @RequestParam("teamId") Long teamId) {

        Long userId = principalDetails.getUserInfo().getId();
        String resultCreateCalendar = teamService.createCalendar(userId,teamId,calendar);

        if (resultCreateCalendar.equals("일정 생성 완료")) {
            return ResponseEntity.ok(resultCreateCalendar);
        }else if(resultCreateCalendar.equals("일정 수정 완료")){
            return ResponseEntity.ok(resultCreateCalendar);
        } else {
            return ResponseEntity.ok("일정 생성 권한이 없음");
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerTeam(@RequestBody Map<String, Object> map,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserInfo userInfo = principalDetails.getUserInfo();
        String resultRegister = teamService.createTeamRegister(map, userInfo);
        if (resultRegister.equals("이미 팀원")) {
            return ResponseEntity.ok("이미 팀원입니다.");
        } else if (resultRegister.equals("신청 내용 수정 완료")) {
            return ResponseEntity.ok("신청 내용 수정이 완료 되었습니다");
        } else {
            return ResponseEntity.ok("신청 완료");
        }
    }

    public ResponseEntity<?> responseJoinTeam(@RequestBody Map<String, Object> map){
        return ResponseEntity.ok("");
    }

    @GetMapping("api/board")
    public ResponseEntity<?> responseBoard(@PageableDefault(size = 10, sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable) {

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
