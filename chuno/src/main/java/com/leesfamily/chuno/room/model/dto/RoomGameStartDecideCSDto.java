package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoomGameStartDecideCSDto {
    String userId; // userId
    boolean checkSlave; // 추노 : false, 노비 : true
}
