package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.InventoryEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {

    List<InventoryEntity> findAllByUser(UserEntity user);
}