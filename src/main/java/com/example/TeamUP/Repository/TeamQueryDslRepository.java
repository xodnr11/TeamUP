package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Tag;
import com.example.TeamUP.Entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.TeamUP.Entity.QTag.tag;

public interface TeamQueryDslRepository {
    List<Team> searchTeams(List<Long> teamIds);


}
