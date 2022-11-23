package com.example.TeamUp.Repository;

import com.example.TeamUP.Entity.Tag;
import com.example.TeamUP.Repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagQueryDslRepositoryImplTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    void searchTagInTeam() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String tag = "123";
            tags.add(tag);
        }
        List<Tag> findTags = tagRepository.searchTagInTeam(tags);

        for (Tag findTag : findTags) {
            System.out.println("findTag = " + findTag.getTagName());
        }
    }

    @Test
    void tagsNameEq() {
    }
}