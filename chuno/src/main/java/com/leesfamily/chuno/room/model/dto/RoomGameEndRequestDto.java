package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoomGameEndRequestDto {
    int chaserWin; // 추노꾼이 이겼을 경우
    int chaserCnt;
    int runnerWin; // 노비가 이겼을 경우
    int runnerCnt;
}
