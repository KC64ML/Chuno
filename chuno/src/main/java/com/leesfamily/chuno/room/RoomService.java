package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Direction;
import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.common.util.GeometryUtils;
import com.leesfamily.chuno.room.model.RoomDto;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.room.model.RoomRequest;
import com.leesfamily.chuno.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    final private EntityManager em;
    final private RoomRepository roomRepository;
    final private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RoomDto> getNearByRooms(Double latitude, Double longitude, Double distance) {
        Location northEast = GeometryUtils
                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtils
                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());

        double x1 = northEast.getLat();
        double y1 = northEast.getLng();
        double x2 = southWest.getLat();
        double y2 = southWest.getLng();

        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
        Query query = em.createNativeQuery("SELECT r.*, u.* "
                        + " FROM rooms AS r " +
                        " LEFT JOIN users AS u " +
                        " ON r.host_id = u.user_id "
                        + " WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", r.location)", RoomEntity.class)
                .setMaxResults(10);

        List<RoomEntity> rooms = query.getResultList();

        List<RoomDto> roomRes = rooms.stream().map(room ->
            new RoomDto(room)
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
                .location(room.getLocation())
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
                resMap.put("result", room);
                resMap.put("code", "1");
            }
        }else {
            resMap.put("code", 0);
        }
        return resMap;
    }
}
