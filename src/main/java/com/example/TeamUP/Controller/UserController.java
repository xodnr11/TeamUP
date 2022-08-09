package com.example.TeamUP.Controller;

import com.example.TeamUP.Entity.Role;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public String join(@RequestBody UserInfo user) {
        System.out.println(user);
        user.setRole(Role.USER);
        user.setRefreshtoken("123123");
        user.setEmail("123123");
        user.setGender('m');
        user.setName("name");
        user.setNickname("nickname");
        user.setPhone("0101");
        user.setBirthday(new Date(2020-10-10));
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);
        return "회원가입 완료";
    }

    @GetMapping("/api/v1/user")
    public @ResponseBody String user(){
        return "user";
    }
}