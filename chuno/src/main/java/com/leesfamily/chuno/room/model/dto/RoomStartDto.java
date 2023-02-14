package com.leesfamily.chuno.room.model.dto;

import com.leesfamily.chuno.room.model.DateTime;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RoomStartDto {

    private Long id;
    private String title;
    private String password;
    private Boolean isPublic;
    private double lat;
    private double lng;
    private int currentPlayers;
//    @Column(nullable = false, columnDefinition = "GEOMETRY")
//    @JsonDeserialize(as = Point.class)
//    private Point location;

    private double radius;

    private DateTime dateTime;

    private UserEntity host;

    private double distance;

    private Boolean isPushed;


    // of 호출시 RoomStartDto 반환
    public static RoomStartDto of (RoomEntity room){
        return new RoomStartDto(
                room.getId(),
                room.getTitle(),
                room.getPassword(),
                room.getIsPublic(),
                room.getLat(),
                room.getLng(),
                room.getCurrentPlayers(),
                room.getRadius(),
                room.getDateTime(),
                room.getHost(),
                room.getDistance(),
                room.getIsPushed()
        );
    }
}
