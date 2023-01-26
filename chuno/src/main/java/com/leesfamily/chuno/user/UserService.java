package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.file.MyFileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Service
@RequiredArgsConstructor
public class UserService {

    final private MyFileUtils fileUtils;

    final private UserRepository userRepository;

    final private EntityManagerFactory emFactory;


}
