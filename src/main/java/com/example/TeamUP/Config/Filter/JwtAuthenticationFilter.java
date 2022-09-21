package com.example.TeamUP.Config.Filter;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Config.Token;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Service.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenServiceImpl tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 실행 - 로그인 시도중");
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo user = null;
        try {
            user = objectMapper.readValue(request.getInputStream(), UserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println("authentication : "+authentication);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUserInfo().getUsername());
            return authentication;
        } catch (NullPointerException e) {
            try {
                response.sendError(401);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication 실행됨 = 인증이 완료 되었다는 뜻");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        Token jwtToken = tokenService.generateToken(principalDetails.getUserInfo().getId(),"USER");

        response.addHeader("Authorization", "Bearer " + jwtToken.getToken());
        response.addHeader("Refresh", "Bearer "+jwtToken.getRefreshToken());
        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.addHeader("Access-Control-Allow-Credentials", String.valueOf(true));
        response.getWriter().write("login response success");
    }
}