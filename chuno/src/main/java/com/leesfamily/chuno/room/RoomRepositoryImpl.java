package com.leesfamily.chuno.room;

import com.leesfamily.chuno.room.model.dto.RoomGameEndRequestDto;
import com.leesfamily.chuno.room.model.dto.RoomGameEndResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import static com.leesfamily.chuno.user.model.QUserEntity.*;

@Slf4j
public class RoomRepositoryImpl implements RoomRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public RoomRepositoryImpl(EntityManager em){jpaQueryFactory = new JPAQueryFactory(em);}

    @Override
    public RoomGameEndResponseDto endRoom(RoomGameEndRequestDto roomGameEndRequestDto, Long userId) {


        // 게임 종료 후, 횟수와 이겼을 경우 숫자 증가
        long res = jpaQueryFactory
                .update(userEntity)
                .set(userEntity.chaserPlayCount, userEntity.chaserPlayCount.add(roomGameEndRequestDto.getChaserCnt()))
                .set(userEntity.runnerPlayCount, userEntity.runnerPlayCount.add(roomGameEndRequestDto.getRunnerCnt()))
                .set(userEntity.chaserWinCount, userEntity.chaserWinCount.add(roomGameEndRequestDto.getChaserWin()))
                .set(userEntity.runnerWinCount, userEntity.runnerWinCount.add(roomGameEndRequestDto.getRunnerWin()))
                .set(userEntity.catchCount, userEntity.catchCount.add(roomGameEndRequestDto.getCatchCount()))
                .set(userEntity.paperCount, userEntity.paperCount.add(roomGameEndRequestDto.getPaperCount()))
                .set(userEntity.exp, userEntity.exp.add(roomGameEndRequestDto.getExp()))
                .set(userEntity.money, userEntity.money.add(roomGameEndRequestDto.getMoney()))
                .where(userEntity.id.eq(userId))
                .execute();

        log.info("결과 : " + res);

        return new RoomGameEndResponseDto(res);
    }

    // 게임 level
    @Override
    public int updateUserLevel(Long userId, int level) {
        long res = jpaQueryFactory
                .update(userEntity)
                .set(userEntity.level, level)
                .where(userEntity.id.eq(userId))
                .execute();

        System.out.println("res : " + res);

        return 1;
    }
}
