package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class RoomGameStartDecideChaserRunnerDto {
    String nickname; // nickname
    List<Integer> items; // 4개
}
