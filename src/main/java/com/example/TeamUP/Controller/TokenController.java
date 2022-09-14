package com.example.TeamUP.Controller;

import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Service.TokenServiceImpl;
import com.example.TeamUP.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenServiceImpl tokenService;
    private final UserServiceImpl userService;

    @GetMapping("/token/expired")
    public String auth() {
        return "oauth2login";
    }

    /**
     * 리프레쉬 토큰을 활용하여 엑세스 토큰과 함께 리프레쉬 토큰을 재발급 해주는 함수
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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
            response.addHeader("Authorization","Bearer "+token.getToken());
            response.addHeader("Refresh","Bearer "+token.getRefreshToken());
            return ResponseEntity.ok("Token 재발급 완료");
        }else {
            log.info("리프레쉬 토큰 만료 작동 확인 리다이렉트");
            response.sendRedirect("/loginPage");
        }

        return ResponseEntity.ok(" ? ");
    }
}