package com.leesfamily.chuno.user.model.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.leesfamily.chuno.user.model.dto.QUserRankingListDto is a Querydsl Projection type for UserRankingListDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUserRankingListDto extends ConstructorExpression<UserRankingListDto> {

    private static final long serialVersionUID = -59370342L;

    public QUserRankingListDto(com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Integer> level, com.querydsl.core.types.Expression<Integer> runnerWinCount, com.querydsl.core.types.Expression<Integer> chaserWinCount, com.querydsl.core.types.Expression<Integer> userCountAvg) {
        super(UserRankingListDto.class, new Class<?>[]{String.class, int.class, int.class, int.class, int.class}, nickname, level, runnerWinCount, chaserWinCount, userCountAvg);
    }

}

