package com.example.TeamUP.Controller;

import com.example.TeamUP.DTO.ResponseUserInfoDTO;
import com.example.TeamUP.Service.UserService;
import com.example.TeamUP.Auth.PrincipalDetails;
import com.example.TeamUP.Entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 매핑 함수
     * @param user
     * @return
     */
    @PostMapping("/api/join")
    public ResponseEntity<?> join(@RequestBody UserInfo user) {

        if (userService.join(user)) {
            return ResponseEntity.ok("회원가입 완료");

        } else {
            return ResponseEntity.ok("이미 존재하는 회원");
        }

    }

    /**
     * 회원 상세정보를 확인하는 매핑 함수
     * @param principalDetails
     * @return
     */
    @PostMapping("/api/v1/user/mypage")
    @ResponseBody
    public ResponseEntity<?> userInformation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails != null) {
            ResponseUserInfoDTO userInfoDTO = userService.getUserInfo(principalDetails.getUserInfo());
            return ResponseEntity.ok(userInfoDTO);
        }else{
            return ResponseEntity.ok("bad");
        }
    }

    /**
     * 회원정보 업데이트 기능을 동작하는 매핑 함수
     * @param userInfo
     * @param principalDetails
     * @return
     */
    @PostMapping("/api/v1/user/update")
    @ResponseBody
    public ResponseEntity<?> updateUserInformation(@RequestBody UserInfo userInfo,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        userService.updateUserInformation(userInfo, principalDetails);

        return ResponseEntity.ok("회원정보 업데이트 완료");
    }
}