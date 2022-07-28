package com.example.TeamUP.Config;

import com.example.TeamUP.Controller.UserDto;
import com.example.TeamUP.Service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Auth");

        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);


            // DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
            UserDto userDto = UserDto.builder()
                    .id("id")
                    .nickname("nickname")
                    .birthday("birthday")
                    .birthyear("birthyear")
                    .gender("M")
                    .mobile("mobile")
                    .email(email)
                    .name("이름이에용")
                    .build();

            log.info("언제 실행되는지 확인 :"+token);

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
