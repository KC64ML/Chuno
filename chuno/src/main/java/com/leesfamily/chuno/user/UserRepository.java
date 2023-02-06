package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom{
    Optional<UserEntity> findByEmail(String email);
    Long countByNicknameIs(String nickname);

}
