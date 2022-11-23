package com.example.TeamUP.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseTagsInTeam {
    private int total_page;
    private List<Posts> posts = new ArrayList<>();

    public void addPosts(List<Map<String, Object>> maps) {

        for (int i = 0; i < maps.size(); i++) {
            Posts posts = new Posts();
            posts.setTeam_id((Long) maps.get(i).get("team_id"));
            posts.setTitle((String) maps.get(i).get("title"));
            posts.setWriter((String) maps.get(i).get("writer"));
            this.posts.add(posts);
        }


    }
    @Data
    public static class Posts {
        private Long team_id;
        private String title;
        private String writer;
    }
}