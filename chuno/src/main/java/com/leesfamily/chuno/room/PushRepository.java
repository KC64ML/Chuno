package com.leesfamily.chuno.room;

import com.leesfamily.chuno.room.model.PushEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushRepository extends JpaRepository<PushEntity, Long> {

    Optional<PushEntity> findByRoomIdAndUserId(long roomId, Long userId);
}
