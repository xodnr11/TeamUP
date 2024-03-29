package com.example.TeamUP.DTO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel(value = "ResponseTeamDTO : 팀 상세내용 API", description = "팀 상세내용")
@Data
public class ResponseTeamDTO{
    Long teamId;
    String title;
    String content;
    String team_present;
    String team_name;
    boolean manager;
    List<Map<String, Object>> team_member;
    List<Map<String, Object>> registers;
    List<Map<String, Object>> calendar;
}
