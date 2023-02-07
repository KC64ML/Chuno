package com.leesfamily.chuno.room.model;

import lombok.Data;

@Data
public class RoomRequest extends RoomEntity{

    private Long hostId;
    private boolean isToday;
    private int hour;
    private int minute;

}
