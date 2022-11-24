package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.*;
import com.example.TeamUP.Entity.Calendar;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Service.TeamService;
import com.example.TeamUP.Service.TeamServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

@Api(description = "팀 관련 요청 API")
@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * 게시글 작성완료 매핑 함수
     */
    @ApiOperation(value = "팀 생성 요청 URL")
    @PostMapping("/api/post/complete")
    public String responseCreateTeam(
            @RequestBody RequestCreateTeamDTO teamInfo,
            @ApiIgnore @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (principalDetails != null) {
            Team team = teamService.createTeam(teamInfo, principalDetails.getUserInfo());
            teamService.insertTag(team, teamInfo.getTags());
            return "팀 생성 완료";
        } else {
            return "미 로그인 상태";
        }
    }

    /**
     * 게시글 상세내용 매핑 함수
     */
    @ApiOperation(value = "게시글 상세내용 요청 URL")
    @GetMapping("/api/post")
    public ResponsePostDTO responsePostInfo(
            @ApiIgnore @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("teamId") String teamId) {

        Long userId = null;

        if (principalDetails != null) {
            userId = principalDetails.getUserInfo().getId();
        }
        ResponsePostDTO responsePostDTO = teamService.getPostInfo(userId, Long.valueOf(teamId));
        return responsePostDTO;
    }

    /**
     * 팀 상세내용 매핑 함수
     */
    @ApiOperation(value = "팀 상세내용 요청 URL")
    @GetMapping("/api/team")
    public ResponseTeamDTO responseTeamInfo(
            @ApiIgnore @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("teamId") Long teamId) {

        Long userId = principalDetails.getUserInfo().getId();
        ResponseTeamDTO responseTeamDTO = teamService.getTeamInfo(userId, teamId);

        return responseTeamDTO;
    }

    /**
     * 팀 캘린더 작성 완료 매핑함수
     */
    @ApiOperation(value = "팀 캘린더 작성 완료 요청 URL")
    @PostMapping("/api/team/calendar/create")
    public String responseCreateCalendar(
            @ApiIgnore @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody Calendar calendar,
            @RequestParam("teamId") Long teamId) {

        Long userId = principalDetails.getUserInfo().getId();
        String resultCreateCalendar = teamService.createCalendar(userId, teamId, calendar);

        if (resultCreateCalendar.equals("일정 생성 완료")) {
            return resultCreateCalendar;
        } else if (resultCreateCalendar.equals("일정 수정 완료")) {
            return resultCreateCalendar;
        } else {
            return "일정 생성 권한이 없음";
        }
    }

    /**
     * 특정 팀에 팀원 신청 기능 동작을 위한 매핑 함수
     */
    @ApiOperation(value = "특정 팀에 팀원 신청 기능 동작을 위한 매핑 URL")
    @PostMapping("/api/register")
    public String registerTeam(
            @RequestBody Map<String, Object> map,
            @ApiIgnore @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserInfo userInfo = principalDetails.getUserInfo();
        String resultRegister = teamService.createTeamRegister(map, userInfo);

        if (resultRegister.equals("이미 팀원")) {
            return "이미 팀원입니다.";
        } else if (resultRegister.equals("신청 내용 수정 완료")) {
            return "신청 내용 수정이 완료 되었습니다";
        } else {
            return "신청 완료";
        }
    }

    /**
     * 팀장이 신청 현황을 확인하여 팀원 신청을 수락하는 매핑 함수
     */
    @ApiOperation(value = "팀장이 신청 현황을 확인하여 팀원 신청을 수락, 거절 하는 URL")
    @PostMapping("/api/team/join")
    public String responseJoinTeam(
            @RequestBody Map<String, Object> map) {

        Long teamId = Long.valueOf(String.valueOf(map.get("team_id")));
        Long userId = Long.valueOf(String.valueOf(map.get("user_id")));

        boolean accept = (boolean) map.get("accept");

        if (accept) {
            teamService.acceptMember(teamId, userId);
            return "수락 완료";
        } else {
            teamService.rejectMember(teamId, userId);
            return "거절 완료";
        }
    }

    /**
     * 전체 게시판 출력을 위한 매핑 함수
     */
    @ApiOperation(value = "전체 게시판 출력을 위한 매핑 URL")
    @GetMapping("/api/board")
    public Page<Team> responseBoard(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("category") String category) {

        Page<Team> teamList = null;
        if (category != "") {
            teamList = teamService.getTeamList(pageable, category);
        } else {
            teamList = teamService.getTeamList(pageable);
        }
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

        return teamList;
    }

    /**
     * 팀 삭제 매핑 함수
     */
    @ApiOperation(value = "팀 삭제 매핑 URL")
    @DeleteMapping("/api/team/delete")
    public String deleteTeam(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(value = "teamId") Long teamId) {

        teamService.deleteTeam(teamId, principalDetails.getUserInfo());

        return "팀 삭제 완료";
    }


    /**
     * 태그로 전체 게시판 검색
     */
    @ApiOperation(value = "태그로 전체 게시판 조회 URL")
    @PostMapping("/api/tags")
    public ResponseTagsInTeam tagsInTeam(
            @RequestBody RequestTags requestTags,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        ResponseTagsInTeam tagsInTeam = teamService.getTagsInTeam(requestTags, pageable);

        return tagsInTeam;
    }
}
