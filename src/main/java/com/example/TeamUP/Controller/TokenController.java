package com.example.TeamUP.Controller;

import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/token/expired")
    public String auth() {
        return "oauth2login";
//        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");

        if (token != null && tokenService.verifyToken(token)) {
//            Long uid = Long.valueOf(tokenService.getUid(token));
            Long uid = Long.valueOf(30);
            Token newToken = tokenService.generateToken(uid, "USER");

            response.addHeader("Auth", newToken.getToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            log.info("{}", newToken);                                                                       //토큰 값 확인
            response.setContentType("application/json;charset=UTF-8");

        }
        return ResponseEntity.ok("아무내용 응답");
    }
}