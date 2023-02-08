package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.FriendEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    Optional<List<FriendEntity>> findFriendEntitiesByFromUser(UserEntity fromUser);

    Optional<FriendEntity> findByFromUserAndToUser(UserEntity me, UserEntity you);

    Optional<List<FriendEntity>> findFriendEntitiesByFromUserId(Long userId);
}
