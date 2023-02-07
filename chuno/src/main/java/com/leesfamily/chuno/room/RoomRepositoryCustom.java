package com.leesfamily.chuno.room;

import com.leesfamily.chuno.room.model.dto.RoomGameEndRequestDto;
import com.leesfamily.chuno.room.model.dto.RoomGameEndResponseDto;
import com.leesfamily.chuno.user.model.UserEntity;

public interface RoomRepositoryCustom {

    // 게임이 종료되었을 때
    RoomGameEndResponseDto endRoom(RoomGameEndRequestDto roomGameEndRequestDto, Long userId);
}
