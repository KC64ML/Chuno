package com.leesfamily.chuno.item;

import com.leesfamily.chuno.item.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByForRunner(int forRunner);
}
