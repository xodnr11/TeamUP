package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public Page<Team> searchTeams(List<Long> teamIds, Pageable pageable) {

        if (teamIds.size() > 0) {
            QueryResults<Team> results = queryFactory
                    .select(team)
                    .from(team)
                    .orderBy(team.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(teamIdEq(teamIds))
                    .fetchResults();

            long total = results.getTotal();                    //총 페이지 수

            List<Team> content = results.getResults();          //실제 내용

            return new PageImpl<>(content, pageable, total);
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
