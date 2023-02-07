package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {

}