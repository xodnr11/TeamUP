package com.example.TeamUP.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.TeamUP.Config.Token;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class TokenService {
    private String secretKey = "secret-key";              //복호화에 필요한 토큰 키

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(Long uid, String role) {
        long tokenPeriod = 1000L * 60L * 60 * 24L * 30L;                   //1달
//        long tokenPeriod = 1000L * 1L;                   //1초
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L;     //1달
//        long refreshPeriod = 1000L * 1L;
        log.info("uid값 확인 : "+uid);
        Claims accessClaims = Jwts.claims().setSubject("userInformation").setAudience(String.valueOf(uid));          //sub, role key 만들어서 토큰 payload에 저장
        accessClaims.put("role", role);
        Claims refreshClaims = Jwts.claims().setIssuer("admin");


        return new Token(
                JWT.create()
                        .withSubject("userInformation")
                        .withExpiresAt(new Date(System.currentTimeMillis() + (tokenPeriod)))
                        .withAudience(String.valueOf(uid))
                        .sign(Algorithm.HMAC512(secretKey)),
                JWT.create()
                        .withSubject("refreshToken")
                        .withExpiresAt(new Date(System.currentTimeMillis() + (refreshPeriod)))
                        .withAudience(String.valueOf(uid))
                        .sign(Algorithm.HMAC512(secretKey)));
    }

    public boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getExpiresAt().after(new Date());
            log.info("토큰 만료 되지 않음");
            return true;
        } catch (TokenExpiredException e) {//토큰 만료 캐치문
            log.info("토큰 만료 작동 확인");
            e.printStackTrace();
            return false;
//            throw new JwtException("토큰 기한 만료");
        }
    }

    public String getUid(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getAudience().toString();        //getAudience로 대상자 이름 가져오기
    }
}