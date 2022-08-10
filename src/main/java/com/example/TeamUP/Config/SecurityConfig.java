package com.example.TeamUP.Config;

import com.example.TeamUP.Config.Filter.JwtAuthenticationFilter;
import com.example.TeamUP.Config.Filter.JwtAuthorizationFilter;
import com.example.TeamUP.Repository.UserRepository;
import com.example.TeamUP.Auth.CustomOAuth2UserService;
import com.example.TeamUP.Service.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final CorsFilter corsFilter;
    private final OAuth2SuccessHandler successHandler;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenServiceImpl tokenService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(),tokenService))
                .authorizeRequests()
//                .antMatchers("/oauth2/**").permitAll()      //이거 빼고는 authenticationManager
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
//                .anyRequest().authenticated()                           //토큰을 필요로 한
        http.oauth2Login()
                .loginPage("/login1")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2UserService);
        http.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userRepository,tokenService), UsernamePasswordAuthenticationFilter.class);

    }
}