package com.example.TeamUP.Config.Filter;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import com.example.TeamUP.Service.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final TokenServiceImpl tokenService;
    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenServiceImpl tokenService) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }
    //토큰 만료 확인 , 토큰 검증, 토큰 만료 시 리프레쉬 토큰 매핑으로 요청
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청됨");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: "+ jwtHeader);
        if ("/token/refresh".equals(request.getRequestURI()) || "/login1".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        //header가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request,response);
            return ;
        }
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        if(!tokenService.verifyToken(jwtToken)){

            response.sendRedirect("/token/refresh");
        }else {
            System.out.println("서명이 정상적");
            String userid = tokenService.getUid(jwtToken).replace("[", "");
            userid = userid.replace("]", "");
            System.out.println(userid);
            Optional<UserInfo> user = userRepository.findById(Long.valueOf(userid));
            System.out.println(user);

            // JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            try {
                PrincipalDetails principalDetails = new PrincipalDetails(user.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                //강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);

                //회원 탈퇴를 했거나 회원 정보가 없어진 상황에서 만료되지 않은 토큰을 요청할 경우
            } catch (NoSuchElementException e) {
                System.out.println("탈퇴한 회원의 요청");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                ErrorObject errorObject = new ErrorObject(
                        "jwt_token", "탈퇴한 회원의 토큰입니다.", HttpStatus.UNAUTHORIZED.value()
                );

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), errorObject);

            }

        }
    }
}