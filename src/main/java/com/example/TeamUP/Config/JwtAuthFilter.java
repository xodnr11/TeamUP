package com.example.TeamUP.Config;

import com.example.TeamUP.Service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Auth");

        //토큰이 만료 되었을 때
        if (token != null && !tokenService.verifyToken(token)) {
            ((HttpServletResponse)response).sendError(401,"Expired Token !!!");
            log.info("토큰 만료 실행 확인");
        }


        chain.doFilter(request, response);
    }


}