package com.leesfamily.chuno.room.model.dto;

import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.room.model.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RoomGameStartResponseDto {

    // (1) 방 정보
    RoomStartDto roomStartDto;


    // (2) 노비 문서 위치 (경도, 위도를 인원 수 x 2로 반환해준다.)
    List<Location>  roomSlaveDocumentList;


    // (3) 추노, 노비 결정된 배열
    List<RoomGameStartDecideChaserRunnerDto> roomDecideChunoOrSlaveList;

    public static RoomGameStartResponseDto of(RoomStartDto room,
                                              List<Location>  roomSlaveDocumentList,
                                              List<RoomGameStartDecideChaserRunnerDto> roomDecideChunoOrSlaveList
    ) {
        return new RoomGameStartResponseDto(
                room, roomSlaveDocumentList, roomDecideChunoOrSlaveList
        );
    }

}
