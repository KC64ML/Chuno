package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoomGameStartSlaveDocumentDto {
    double lat; // 위도
    double lng; // 경도
    boolean real; // 진짜 인지 아닌지
}
