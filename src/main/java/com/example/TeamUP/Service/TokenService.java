package com.example.TeamUP.Service;

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
//        long tokenPeriod = 1000L * 60L * 1L;                   //1분
        long tokenPeriod = 1000L * 30L;                   //1초
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L;     //1달

        Claims accessClaims = Jwts.claims().setSubject("userInformation").setAudience(String.valueOf(uid));          //sub, role key 만들어서 토큰 payload에 저장
        accessClaims.put("role", role);
        Claims refreshClaims = Jwts.claims().setIssuer("admin");
        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setClaims(accessClaims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(refreshClaims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (ExpiredJwtException e) {//토큰 만료 캐치문
            log.info("토큰 만료 작동 확인");
            return false;
//            throw new JwtException("토큰 기한 만료");
        }
    }

    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getAudience();        //getAudience로 대상자 이름 가져오기
    }
}