package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.QTag;
import com.example.TeamUP.Entity.Tag;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.TeamUP.Entity.QTag.*;

public class TagQueryDslRepositoryImpl implements TagQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public TagQueryDslRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 들어온 태그들을 한번에 검색
     */
    @Override
    public List<Tag> searchTagInTeam(List<String> tags) {

        if (tags.size() > 0) {
            return queryFactory
                    .select(tag)
                    .from(tag)
                    .where(tagsNameEq(tags))
                    .fetch();
        } else {
            return null;
        }
    }

    public BooleanBuilder tagsNameEq(List<String> tags) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < tags.size(); i++) {
            builder.or(tag.tagName.eq(tags.get(i)));
        }
        return builder;
    }
}
