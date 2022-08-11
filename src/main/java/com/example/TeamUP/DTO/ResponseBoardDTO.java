package com.example.TeamUP.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseBoardDTO {
    int total_page;
    List<Map<String, Object>> posts;
}