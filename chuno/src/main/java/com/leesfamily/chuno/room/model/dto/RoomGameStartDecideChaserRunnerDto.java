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

        // 모카, 창창 일 때 문자열 길이 같을 때는 1로 정렬되기 (오름차순)
        // 인의동, 창창 일 때는 문자열 길이로 반환 (내림차순)
        if(o.nickname.length() == this.nickname.length()) return 1;
        else return o.nickname.length() - this.nickname.length();
    }
}
