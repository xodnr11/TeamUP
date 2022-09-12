package com.example.TeamUP.Controller.Thymeleaf;

import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserControllerThymeleaf {
    private final UserService userService;

    /**
     * 로그인 페이지 매핑 함수
     * @return
     */
    @GetMapping("/loginPage")
    public String loginPage() {
        return "Login/login";
    }

    /**
     * 회원가입 페이지 매핑 함수
     * @return
     */
    @GetMapping("/joinPage")
    public String joinPage() {
        return "Login/join";
    }

    /**
     * 회원가입 요청 매핑 함수
     * @param userInfo
     * @return
     */
    @PostMapping("/thymeleaf/join")
    public ResponseEntity<?> join(@RequestBody UserInfo userInfo) {
        if(userService.join(userInfo)){
            return ResponseEntity.ok("회원가입 완료");
        }else {
            return ResponseEntity.ok("이미 존재하는 회원");
        }
    }
}
