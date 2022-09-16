package com.example.TeamUP.Controller.Thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamControllerThymeleaf {
    @GetMapping("/board")
    public String board() {
        return "Board/board";
    }

    @GetMapping("/board/create")
    public String boardCreate() {
        return "Board/createTeam";
    }
}
