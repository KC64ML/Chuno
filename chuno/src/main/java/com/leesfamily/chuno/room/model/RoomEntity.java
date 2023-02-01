package com.leesfamily.chuno.room.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="rooms")
@DynamicInsert
@DynamicUpdate
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String password;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isPublic;
    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int currentPlayers;

    @Column(nullable = false)
    @ColumnDefault("10")
    private int maxPlayers;

    @Column(nullable = false, columnDefinition = "GEOMETRY")
    @JsonDeserialize(as = Point.class)
    private Point location;

    @Column(nullable = false)
    private int radius;

    @Embedded
    private DateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "host_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity host;

}
