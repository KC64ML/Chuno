package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoomGameStartRequestDto {

    // 방 정보 id, 사용자 정보 id들
    Long roomId;  // 방 ID
    List<Long> userIdList;  // 사용자 ID List

    // ex) 방 ID: 감자튀김, 유저정보
    // - 유저 A : 1233
    // - 유저 B : 32323
}
