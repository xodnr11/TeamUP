package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Tag;
import com.example.TeamUP.Entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.TeamUP.Entity.QTag.tag;

public interface TeamQueryDslRepository {
    Page<Team> searchTeams(List<Long> teamIds, Pageable pageable);


}
