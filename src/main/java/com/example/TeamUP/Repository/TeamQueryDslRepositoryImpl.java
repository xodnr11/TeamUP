package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.TeamUP.Entity.QTeam.*;


public class TeamQueryDslRepositoryImpl implements TeamQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public TeamQueryDslRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 들어온 태그들을 한번에 검색
     */
    @Override
    public List<Team> searchTeams(List<Long> teamIds) {

        if (teamIds.size() > 0) {
            return queryFactory
                    .select(team)
                    .from(team)
                    .where(teamIdEq(teamIds))
                    .fetch();
        } else {
            return null;
        }
    }

    public BooleanBuilder teamIdEq(List<Long> tags) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < tags.size(); i++) {
            builder.or(team.id.eq(tags.get(i)));
        }
        return builder;
    }

}
