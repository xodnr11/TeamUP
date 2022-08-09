package com.example.TeamUP.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponsePostDTO {
    Long teamId;
    String title;
    String content;
    String writer;
    int max_member;
    int current_member;
    List<Map<String, Object>> team_member;
    Boolean registered;
}
