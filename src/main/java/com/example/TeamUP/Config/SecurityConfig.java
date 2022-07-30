package com.example.TeamUP.Config;

import com.example.TeamUP.Controller.OAuth2SuccessHandler;
import com.example.TeamUP.Service.CustomOAuth2UserService;
import com.example.TeamUP.Service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService, OAuth2SuccessHandler successHandler, TokenService tokenService) {
        this.oAuth2UserService = oAuth2UserService;
        this.successHandler = successHandler;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()      //이거 빼고는
//                .anyRequest().authenticated()                           //토큰을 필요로 한다
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .successHandler(successHandler)
                    .userInfoEndpoint().userService(oAuth2UserService);

        http.addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }
}