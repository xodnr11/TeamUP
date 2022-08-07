package com.example.TeamUP.Controller;

import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Service.TokenService;
import com.example.TeamUP.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;
    private final UserServiceImpl userService;

    @GetMapping("/token/expired")
    public String auth() {
        return "oauth2login";
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        String refreshToken = request.getHeader("Refresh").replace("Bearer ", "");
        log.info("엑세스 토큰 확인 : "+accessToken);
        log.info("리프레쉬 토큰 확인 : "+refreshToken);

        if (refreshToken != null && tokenService.verifyToken(refreshToken)) {

            String userid = tokenService.getUid(refreshToken).replace("[", "");
            userid = userid.replace("]", "");

            Token token = userService.updateRefreshToken(Long.valueOf(userid),refreshToken);
            log.info("리프레쉬 토큰 만료 되지 않음");
            response.addHeader("Authorization",token.getToken());
            response.addHeader("Refresh",token.getRefreshToken());
            return ResponseEntity.ok("아무내용 응답");
        }else {
            log.info("리프레쉬 토큰 만료 작동 확인 리다이렉트");
            response.sendRedirect("/login1");
        }

        return ResponseEntity.ok(" ? ");
    }
}