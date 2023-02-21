package com.leesfamily.chuno.room.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class RoomGameStartDecideChaserRunnerDto implements Comparable<RoomGameStartDecideChaserRunnerDto> {
    String nickname; // nickname
    List<Integer> items; // 4개

    @Override
    public int compareTo(@NotNull RoomGameStartDecideChaserRunnerDto o) {
        return o.nickname.length() - this.nickname.length();
    }
}
