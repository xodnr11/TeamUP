package com.example.TeamUP.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestTags {
    private List<String> tag = new ArrayList<>();

    public void addTag(String tag) {
        this.tag.add(tag);
    }
}
