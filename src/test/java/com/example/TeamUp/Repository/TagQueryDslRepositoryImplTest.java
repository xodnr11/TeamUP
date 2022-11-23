package com.example.TeamUp.Repository;

import com.example.TeamUP.Entity.Tag;
import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Repository.TagRepository;
import com.example.TeamUP.Repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class TagQueryDslRepositoryImplTest {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void searchTagInTeam() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String tag = "00";
            tags.add(tag);
        }
        List<Tag> findTags = tagRepository.searchTagInTeam(tags);

        List<Long> teamIds = new ArrayList<>();


        if (findTags != null) {
            for (Tag findTag : findTags) {
                System.out.println("findTag = " + findTag.getTeam().getId());
                teamIds.add(findTag.getTeam().getId());
            }
            List<Team> findTeams = teamRepository.searchTeams(teamIds);
            if (findTeams != null) {
                for (Team findTeam : findTeams) {
                    System.out.println("findTeam = " + findTeam.getTeamName());
                }
            }
        }
    }

    @Test
    void tagsNameEq() {
    }
}