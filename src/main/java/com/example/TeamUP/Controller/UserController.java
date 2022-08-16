package com.example.TeamUP.Controller;

import com.example.TeamUP.DTO.ResponseUserInfoDTO;
import com.example.TeamUP.Service.TeamService;
import com.example.TeamUP.Service.UserService;
import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final TeamService teamService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserInfo user) {

        if (userService.join(user)) {
            return ResponseEntity.ok("회원가입 완료");

        } else {
            return ResponseEntity.ok("이미 존재하는 회원");
        }

    }

    @GetMapping("/api/v1/user/mypage")
    @ResponseBody
    public ResponseEntity<?> userInformation(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserInfo userInfo = principalDetails.getUserInfo();
        ResponseUserInfoDTO userInfoDTO = new ResponseUserInfoDTO();

        if (principalDetails.getUserInfo().getUsername().contains("naver")) {
            userInfoDTO.setID("NAVER 계정으로 로그인 중");
        } else if (principalDetails.getUserInfo().getUsername().contains("kakao")) {
            userInfoDTO.setID("KAKAO 계정으로 로그인 중");
        } else {
            userInfoDTO.setID(userInfo.getUsername());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.setTime(userInfo.getBirthday());

        // 하루 전
        cal.add(Calendar.DATE, +1);
        String birthday = dateFormat.format(cal.getTime());
        Date date = null;
        try {
            date = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        userInfoDTO.setUser_birthday(date);
        userInfoDTO.setUser_email(userInfo.getEmail());
        userInfoDTO.setUser_gender(userInfo.getGender());
        userInfoDTO.setUser_phone(userInfo.getPhone());
        userInfoDTO.setUser_nickname(userInfo.getNickname());

        List<Map<String, Object>> teamList = teamService.getMyTeams(userInfo);

        if (teamList != null) {
            userInfoDTO.setTeam(teamList);

            return ResponseEntity.ok(userInfoDTO);
        } else {
            userInfoDTO.setTeam(null);

            return ResponseEntity.ok(userInfoDTO);
        }
    }

    @PostMapping("/api/v1/user/update")
    @ResponseBody
    public ResponseEntity<?> updateUserInformation(@RequestBody UserInfo userInfo,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        userService.updateUserInformation(userInfo, principalDetails);

        return ResponseEntity.ok("회원정보 업데이트 완료");
    }
}