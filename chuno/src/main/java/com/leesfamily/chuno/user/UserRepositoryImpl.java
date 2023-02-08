package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.dto.QUserRankingListDto;
import com.leesfamily.chuno.user.model.dto.UserRankingListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.leesfamily.chuno.user.model.QUserEntity.userEntity;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositoryImpl(EntityManager em){
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    // 사용자 랭킹 조회
    @Override
    public List<UserRankingListDto> getRankingList() {
        // 평균 : 노비 + 추노꾼 게임 이긴 수 / 노비 + 추노꾼 게임 횟수

        return jpaQueryFactory
                .select(new QUserRankingListDto(
                        userEntity.nickname,
                        userEntity.level,
                        userEntity.runnerWinCount,
                        userEntity.chaserWinCount,
                        userEntity.chaserWinCount.add(userEntity.runnerWinCount).divide(
                                userEntity.chaserPlayCount.add(userEntity.runnerPlayCount)
                        ).multiply(100))
                ).from(userEntity)
                .where(userEntity.isDeleted.eq(false))
                .orderBy(userEntity.chaserWinCount.add(userEntity.runnerWinCount).divide(
                        userEntity.chaserPlayCount.add(userEntity.runnerPlayCount)).multiply(100).desc())
                .fetch();
    }
}
