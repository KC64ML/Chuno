package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.file.MyFileUtils;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


@Service
@RequiredArgsConstructor
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    final private MyFileUtils fileUtils;

    final private UserRepository userRepository;
    final private EntityManagerFactory emFactory;


    public UserEntity getMyPropile(Long id) {
        userRepository.findById(id);
        return null;
    }

    public String findUserByEmail(String email) {
        userRepository.findBy
    }
}
