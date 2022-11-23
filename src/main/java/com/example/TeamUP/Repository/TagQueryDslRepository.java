package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Tag;

import java.util.List;

public interface TagQueryDslRepository {
    List<Tag> searchTagInTeam(List<String> tags);
}
