package com.example.TeamUP.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ResponseUserInfoDTO {
    private String ID;
    private String user_email;
    private Date user_birthday;
    private String user_name;
    private String user_nickname;
    private char user_gender;
    private String user_phone;
    private List<Map<String,Object>> team;

}
