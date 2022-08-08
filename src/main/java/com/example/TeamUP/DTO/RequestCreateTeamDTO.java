package com.example.TeamUP.DTO;

import com.example.TeamUP.Entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class RequestCreateTeamDTO {
    String title;
    String content;
    int max_mamber;
    List<String> tags;
    String category;
    String present;
    String teamName;
}
