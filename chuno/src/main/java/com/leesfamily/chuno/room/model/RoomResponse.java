package com.leesfamily.chuno.room.model;

import com.leesfamily.chuno.user.model.UserEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
public class RoomResponse {

        private Long id;

        private String title;
        private String password;
        private double lat;
        private double lng;
        private int currentPlayers;
        private int maxPlayers;
        private int radius;
        private double distance;
        private boolean isPushed;
        private UserEntity host;
        public RoomResponse(RoomEntity entity) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.password = entity.getPassword();
            this.lat = entity.getLat();
            this.lng = entity.getLng();
            this.currentPlayers = entity.getCurrentPlayers();
            this.maxPlayers = entity.getMaxPlayers();
            this.radius = entity.getRadius();
            this.host = entity.getHost();
            this.isPushed = entity.isPushed();
            this.distance = entity.getDistance();
        }

    @Getter
    @NoArgsConstructor
    private static class PointDto {
        private Double lng;	// 경도
        private Double lat;	// 위도

        public PointDto(Point entity) {
            this.lng = entity.getX();
            this.lat = entity.getY();
        }
    }
}
