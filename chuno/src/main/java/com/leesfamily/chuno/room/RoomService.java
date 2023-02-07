package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.room.model.RoomResponse;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.room.model.RoomRequest;
import com.leesfamily.chuno.room.model.dto.RoomGameStartRequestDto;
import com.leesfamily.chuno.room.model.dto.RoomGameStartResponseDto;
import com.leesfamily.chuno.room.model.dto.RoomListByConditionsDto;
import com.leesfamily.chuno.room.model.dto.RoomStartDto;
import com.leesfamily.chuno.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    final private EntityManager em;
    final private RoomRepository roomRepository;
    final private UserRepository userRepository;
    final private PushRepository pushRepository;

    @Transactional(readOnly = true)
    public List<RoomResponse> getNearByRooms(Location loc) {
        Double latitude = loc.getLat();
        Double longitude = loc.getLng();
//        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
        Query query = em.createNativeQuery("SELECT r.*, u.*, " +
                        " (6371*acos(cos(radians(" + latitude + ")) " +
                        " * cos(radians(r.lat)) " +
                        " * cos(radians(r.lng) - radians(" + longitude + ")) " +
                        " + sin(radians(" + latitude + ")) * sin(radians(r.lat)))) as distance," +
                        " IF(p.room_id = r.room_id, true, false) as isPushed "
                        + " FROM rooms AS r " +
                        " LEFT JOIN users AS u " +
                        " ON r.host_id = u.user_id " +
                        " LEFT JOIN pushes p " +
                        " ON p.user_id = u.user_id "
//                        + " WHERE distance  " +
                        + " ORDER BY distance ", RoomEntity.class)
                .setMaxResults(20);

        List<RoomEntity> rooms = query.getResultList();

        List<RoomResponse> roomRes = rooms.stream().map(room ->
            new RoomResponse(room, loc)
        ).collect(Collectors.toList());
        return roomRes;
    }

    public RoomEntity insRoom(RoomRequest room, Long host_id) {
        RoomEntity roomEntity = RoomEntity.builder()
                .lng(room.getLng())
                .lat(room.getLat())
                .title(room.getTitle())
                .isPublic(room.isPublic())
                .radius(room.getRadius())
                .password(room.getPassword())
                .host(userRepository.getOne(host_id))
                .build();
        RoomEntity res = roomRepository.save(roomEntity);
        roomRepository.flush();
        return res;
    }

    public Map<String, Object> joinRoom(long roomId, Long userId) {
        Optional<RoomEntity> res = roomRepository.findById(roomId);
        Map<String, Object> resMap = new HashMap<>();
        if(res.isPresent()) {
            RoomEntity room = res.get();
            int currentPlayers = room.getCurrentPlayers();
            int maxPlayers = room.getMaxPlayers();
            if(currentPlayers == maxPlayers) {
                resMap.put("code", "2");
            }else {
                room.setCurrentPlayers(currentPlayers + 1);
                roomRepository.saveAndFlush(room);
                RoomResponse dto = new RoomResponse(room, null);
                resMap.put("result", dto);
                resMap.put("code", "1");
            }
        }else {
            resMap.put("code", 0);
        }
        return resMap;
    }


    public List<RoomResponse> getRoomsByConditinos(RoomListByConditionsDto roomListByConditionDto){
        Double latitude = roomListByConditionDto.getLoc().getLat(); // 위도
        Double longitude = roomListByConditionDto.getLoc().getLat(); // 경도

        // condition
        // 조건은 방제목으로 구현

        Query query = em.createNativeQuery("SELECT r.*, u.*, " +
                        " (6371*acos(cos(radians(" + latitude + ")) " +
                        " * cos(radians(r.lat)) " +
                        " * cos(radians(r.lng) - radians(" + longitude + ")) " +
                        " + sin(radians(" + latitude + ")) * sin(radians(r.lat)))) as distance," +
                        " IF(p.room_id = r.room_id, true, false) as isPushed "
                        + " FROM rooms AS r " +
                        " LEFT JOIN users AS u " +
                        " ON r.host_id = u.user_id " +
                        " LEFT JOIN pushes p " +
                        " ON p.user_id = u.user_id " +
                        " WHERE r.title LIKE '%" + roomListByConditionDto.getKeyword() +"%' " +
                        " ORDER BY distance ", RoomEntity.class)
                .setMaxResults(20);

        List<RoomEntity> rooms = query.getResultList();

        List<RoomResponse> roomRes = rooms.stream().map(room ->
                new RoomResponse(room, roomListByConditionDto.getLoc())
        ).collect(Collectors.toList());
        return roomRes;
    }

    public Map<String, Object> pushRoom(long roomId, Long userId) {
        return null;
    }


    public RoomGameStartResponseDto startRoom(RoomGameStartRequestDto roomStartRequestDto) {
        // (1) 방 테이블 조회
        // json test
        // {
        //    "roomId" : 1,
        //    "userIdList" : [
        //        1, 2
        //    ]
        // }
        RoomStartDto roomStartDto = roomRepository.findById(roomStartRequestDto.getRoomId())
                .map(RoomStartDto::of)
                .orElseThrow(() -> new RuntimeException("room 정보가 조회되지 않는다."));

        log.info("room 정보 조회 : " + roomStartDto.getTitle() + " id : " + roomStartDto.getId() + " 사용자 : " + roomStartDto.getCurrentPlayers());

        // 현재 들어와있는 사용자 수 update
        roomStartDto.setCurrentPlayers(roomStartRequestDto.getUserIdList().size());

        // (2) 노비문서 위도, 경도 회원 x 2
        // - 방 정보에서 반지름과 중심 좌표를 받는다. (위도 : lat, 경도 : lng, 반지름 : radius)
        // 현재 반지름 기준 random

        // 좌표 인원 수 x 2개 위치 구하는 함수
        List<Location> randomLatLng = randomLatLngCoordinate(roomStartDto);
        log.info("현재 좌표 : " + roomStartDto.getLat() + " " + roomStartDto.getLng());
        log.info("반지름 : " + roomStartDto.getRadius());
        log.info("좌표 : " + randomLatLng.get(0).getLat() + " " + randomLatLng.get(0).getLng()
                + " " + randomLatLng.get(1).getLat() + " "  + randomLatLng.get(1).getLng()
                + " " + randomLatLng.get(2).getLat() + " " + randomLatLng.get(2).getLng()
        );

        // (3)


        return null;

    }

    private List<Location> randomLatLngCoordinate(RoomStartDto roomStartDto) {
        // random 좌표를 구한다.
        // random x, y의 범위는 +-(반지름 x 0.9) 범위 지정
        // x : (random x 곱하기 2 - radius) * 0.9
        // y : (random y 곱하기 2 - radius) * 0.9

        int radius = roomStartDto.getRadius();
        double centerLat = roomStartDto.getLat();
        double centerLng = roomStartDto.getLng();

        List<Location> locStoreLatLng = new ArrayList<>();

        while(true){
            double randomLat = (Math.random() * (radius * 2) - radius) * 0.9 + centerLat;
            double randomLng = (Math.random() * (radius * 2) - radius) * 0.9 + centerLng;

            // 거리를 계산
            double distance = (6371*Math.acos(Math.cos(Math.toRadians(centerLat))
                    * Math.cos(Math.toRadians(randomLat)) * Math.cos(Math.toRadians(randomLng) - Math.toRadians(centerLng))
                    + Math.sin(Math.toRadians(centerLat)) * Math.sin(Math.toRadians(randomLat))));

            // 거리가 반지름보다 작다면 추가
            if(distance < radius){
                locStoreLatLng.add(new Location(randomLat, randomLng));
            }

            // 현재 참여한 인원수 x 2일 경우 종료한다.
            if(locStoreLatLng.size() == roomStartDto.getCurrentPlayers() * 2) break;
        }

        return locStoreLatLng;
    }
}
