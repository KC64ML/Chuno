package com.leesfamily.chuno.configure;

import com.leesfamily.chuno.common.util.SMSUtils;
import com.leesfamily.chuno.room.RoomService;
import com.leesfamily.chuno.user.UserRepository;
import com.leesfamily.chuno.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    final private UserService userService;
    final private RoomService service;
    final private SMSUtils smsUtils;


    @Async // 병렬로 Scheduler 를 사용할 경우 @Async 추가
    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateTask() throws InterruptedException {
        
    }
}
