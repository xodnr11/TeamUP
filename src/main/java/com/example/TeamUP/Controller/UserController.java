package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Entity.UserInfo;
import com.example.TeamUP.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserInfo user) {
        if(userService.join(user)){
            return ResponseEntity.ok("회원가입 완료");

        }else {
            return ResponseEntity.ok("이미 존재하는 회원");
        }
    }

    @GetMapping("/api/v1/user/mypage")
    @ResponseBody
    public ResponseEntity<?> userInformation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserInfo userInfo = principalDetails.getUserInfo();
        Map<String, Object> map = new HashMap<>();
        if (principalDetails.getUserInfo().getUsername().contains("naver")) {
            map.put("ID","NAVER 계정으로 로그인 중");
            map.put("Email", userInfo.getEmail());
            map.put("Birthday", userInfo.getBirthday());
            map.put("NickName", userInfo.getNickname());
            map.put("name", userInfo.getName());
            map.put("Gender", userInfo.getGender());
            map.put("Phone", userInfo.getPhone());
            return ResponseEntity.ok(map);
        } else if (principalDetails.getUserInfo().getUsername().contains("kakao")) {
            map.put("ID","KAKAO 계정으로 로그인 중");
            map.put("Email", userInfo.getEmail());
            map.put("Birthday", userInfo.getBirthday());
            map.put("NickName", userInfo.getNickname());
            map.put("name", userInfo.getName());
            map.put("Gender", userInfo.getGender());
            map.put("Phone", userInfo.getPhone());
            return ResponseEntity.ok(map);
        }
        map.put("ID",userInfo.getUsername());
        map.put("Email", userInfo.getEmail());
        map.put("Birthday", userInfo.getBirthday());
        map.put("NickName", userInfo.getNickname());
        map.put("name", userInfo.getName());
        map.put("Gender", userInfo.getGender());
        map.put("Phone", userInfo.getPhone());
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/api/v1/user/update")
    @ResponseBody
    public ResponseEntity<?> updateUserInformation(@RequestBody UserInfo userInfo,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUserInformation(userInfo,principalDetails);
        return ResponseEntity.ok("회원정보 업데이트 완료");
    }
}