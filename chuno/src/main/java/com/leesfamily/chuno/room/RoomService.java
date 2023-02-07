package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Direction;
import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.common.util.GeometryUtils;
import com.leesfamily.chuno.room.model.*;
import com.leesfamily.chuno.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        Double longitude = loc.getLat();
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

    public RoomResponse getRoomById(Long roomId) {
        return new RoomResponse(roomRepository.findById(roomId).get());
    }

    public RoomEntity insRoom(RoomRequest room, Long host_id) {
        DateTime dt = new DateTime();
        dt.setHour(room.getHour());
        dt.setMinute(room.getMinute());
        LocalDate now = LocalDate.now();
        dt.setYear(now.getYear());
        dt.setMonth(now.getMonthValue());
        dt.setDay(now.getDayOfMonth());

        RoomEntity roomEntity = RoomEntity.builder()
                .lng(room.getLng())
                .lat(room.getLat())
                .title(room.getTitle())
                .isPublic(room.isPublic())
                .radius(room.getRadius())
                .password(room.getPassword())
                .dateTime(dt)
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


    public List<RoomResponse> getRoomsByConditinos(Location loc, String condition, String keyword) {
        return null;
    }

    public PushEntity pushRoom(long roomId, Long userId) {
        PushEntity pushEntity = PushEntity.builder()
                .room(roomRepository.findById(roomId).get())
                .user(userRepository.findById(userId).get())
                .build();
        PushEntity res = pushRepository.saveAndFlush(pushEntity);
        return res;
    }
}
