package com.example.TeamUP.Controller.Thymeleaf;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.ResponseBoardDTO;
import com.example.TeamUP.DTO.ResponsePostDTO;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Service.TeamService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeamControllerThymeleaf {

    private final TeamService teamService;

    /**
     * 게시판 pageable 매핑 함수
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/board")
    public String responseBoard(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

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
        model.addAttribute("teamList", teamList);
        return "Board/board";
    }

    /**
     * 게시글 작성 화면 매핑 함수
     *
     * @return
     */
    @GetMapping("/board/create")
    public String boardCreate() {
        return "Board/createTeam";
    }

    /**
     * 게시판 상세내용 페이지 매핑함수
     *
     * @return
     */
    @GetMapping("/board/boardDetail")
    public String boardDetil(
            @RequestParam("teamId") Long teamId,
            Model model) {

        Map<String, String> map = new HashMap<>();
        map.put("teamId", String.valueOf(teamId));
        model.addAllAttributes(map);

        return "Board/boardDetail";
    }

    @GetMapping("/team/teamDetail")
    public String teamDetail(
            Model model,
            @RequestParam("teamId") Long teamId) {

        Map<String, String> map = new HashMap<>();
        map.put("teamId", String.valueOf(teamId));
        model.addAllAttributes(map);

        return "Team/teamDetail";
    }
}
