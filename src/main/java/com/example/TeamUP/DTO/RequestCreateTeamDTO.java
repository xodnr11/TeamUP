package com.example.TeamUP.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RequestCreateTeamDTO {
    String title;
    String content;
    int max_member;
    List<Map<String,String>> tags;
    String category;
    String present;
    String teamName;
}
