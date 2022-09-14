package com.example.TeamUP.Controller;

import com.example.TeamUP.Auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 네이버 로그인 기본 로직 연습 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class Controller {

    private String CLIENT_ID = "qhlli0LOvYNyZETvCNZc"; //애플리케이션 클라이언트 아이디값";
    private String CLI_SECRET = "t_tq8x9bmH"; //애플리케이션 클라이언트 시크릿값";

    @GetMapping("/")
    public String index(
            Model model,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String CALLBACK_URL = "http://localhost:8080/callback";

        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += String.format("&client_id=%s&redirect_uri=%s&state=%s",
                CLIENT_ID, CALLBACK_URL, 200);

//        if (principalDetails != null) {
//            System.out.println("프린시펄 널 아닐 때 계정 아이디 : " + principalDetails.getUserInfo().getUsername());
//            model.addAttribute("username", principalDetails.getUserInfo().getUsername());
//            System.out.println(model.toString());
//        } else {
//            System.out.println("프린시펄 널일 떄");
//            model.addAttribute("username", null);
//            System.out.println(model.toString());
//        }
        model.addAttribute("apiURL", apiURL);
        return "index";
    }

    @GetMapping("/oauth2LoginPage")
    public String login() {
        return "Test/oauth2login";
    }

    //리액트 서버와 연동 테스트 메소드 (GetMapping, No Security)
    @GetMapping("/api/react_test_login")
    public ResponseEntity<?> front_login_test(@RequestParam(name = "username") String username,
                                              @RequestParam(name = "password") String password) {
        log.info(username);
        log.info(password);
        Map<String, Object> map = new HashMap<>();
        map.put("success", "응답 성공");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/callback")
    public String callback(HttpServletRequest httpServletRequest) throws IOException, ParseException {

        String code = httpServletRequest.getParameter("code");
        String state = httpServletRequest.getParameter("state");
        String error = httpServletRequest.getParameter("error");
        String error_description = httpServletRequest.getParameter("error_description");

        String redirectURI = URLEncoder.encode("http://localhost:8080/callback", "UTF-8");

        log.info("코드 값 확인 : " + code);
        log.info("상태 값 확인 : " + state);
        log.info("에러 값 확인 : " + error);
        log.info("에러 네용 확인 : " + error_description);

        //-------
        //엑세스 토큰 받는 곳
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLI_SECRET;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        String headerStr = "";

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", headerStr);

        String inputLine = "";
        int responseCode = con.getResponseCode();
        BufferedReader br;

        StringBuffer res = new StringBuffer();
        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }

        br.close();
        log.info("리턴 값 확인 : " + res);

        Map<String, Object> parsedJson = new JSONParser(res.toString()).parseObject();
        log.info("제이슨 값 확인 : " + parsedJson);
        String accessToken = parsedJson.get("access_token").toString();
        access(accessToken);
        log.info("엑세스 토큰 값 확인 : " + accessToken);


        //---------
        //토큰으로 프로필 받기

        return "callback";
    }

    public void access(String accessToken) throws IOException, ParseException {
        String apiURL1 = "https://openapi.naver.com/v1/nid/me";
        String headerStr = "Bearer " + accessToken; // Bearer 다음에 공백 추가

        URL url1 = new URL(apiURL1);

        HttpURLConnection con1 = (HttpURLConnection) url1.openConnection();
        con1.setRequestMethod("GET");
        con1.setRequestProperty("Authorization", headerStr);

        String inputLine = "";
        int responseCode = con1.getResponseCode();
        BufferedReader br1;

        StringBuffer res1 = new StringBuffer();
        if (responseCode == 200) {
            br1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
        } else {
            br1 = new BufferedReader(new InputStreamReader(con1.getInputStream()));
        }
        while ((inputLine = br1.readLine()) != null) {
            res1.append(inputLine);
        }

        br1.close();
        log.info("리턴 값 확인 : " + res1);

        Map<String, Object> parsedJson1 = new JSONParser(res1.toString()).parseObject();
        log.info("제이슨 값 확인 : " + parsedJson1);

    }
}