package com.leesfamily.chuno.room.model.dto;

import com.leesfamily.chuno.common.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class RoomListByConditionsDto {
    Location loc; // 위치
//    String condition; // 조건
    String keyword; // 입력한 글자
}
