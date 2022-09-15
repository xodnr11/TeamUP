package com.example.TeamUP.Controller.Thymeleaf;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.DTO.ResponseUserInfoDTO;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Service.TeamService;
import com.example.TeamUP.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserControllerThymeleaf {
    private final UserService userService;
    private final TeamService teamService;

    /**
     * 로그인 페이지 매핑 함수
     *
     * @return
     */
    @GetMapping("/loginPage")
    public String loginPage() {
        return "Login/login";
    }

    /**
     * 회원가입 페이지 매핑 함수
     *
     * @return
     */
    @GetMapping("/joinPage")
    public String joinPage() {
        return "Login/join";
    }

    /**
     * 마이페이지 매핑함수
     * @return
     */
    @GetMapping("/myPage")
    public String myPage() {

        return "User/myPage";
    }

    /**
     * 회원가입 요청 매핑 함수
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/thymeleaf/join")
    public ResponseEntity<?> join(@RequestBody UserInfo userInfo) {

        if (userService.join(userInfo)) {
            return ResponseEntity.ok("회원가입 완료");
        } else {
            return ResponseEntity.ok("이미 존재하는 회원");
        }
    }

    /**
     * 인덱스 페이지 로그인 여부 랜더링 매핑 함수
     *
     * @param principalDetails
     * @return
     */
    @GetMapping("/thymeleaf/getUsername")
    public ResponseEntity<?> getUsrename(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, String> map = new HashMap<>();
        if (principalDetails != null) {
            map.put("username", principalDetails.getUserInfo().getUsername());
            System.out.println("로그인 상태");
            return ResponseEntity.ok(map);
        } else {
            map.put("username", null);
            System.out.println("비로그인 상태");
            return ResponseEntity.ok(map);
        }
    }
}
