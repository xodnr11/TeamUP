package com.example.TeamUP.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  //내서버가 응답할때, json을 자바스크립트에서 처리할수있게 설정
        config.addExposedHeader("");  //모든 ip에 응답을 허용하겠다.
        config.addAllowedOrigin("");  //모든 헤더에 응답을 허용하겠다.
        config.addAllowedMethod("*");  //모든 post, get, put, patch 요청을 허용하겠다.
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}