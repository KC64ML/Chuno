package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Direction;
import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.common.util.GeometryUtils;
import com.leesfamily.chuno.room.model.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findById(String id);
}
