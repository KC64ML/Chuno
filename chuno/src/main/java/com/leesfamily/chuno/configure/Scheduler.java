package com.leesfamily.chuno.configure;

import com.leesfamily.chuno.common.util.SMSUtils;
import com.leesfamily.chuno.room.PushRepository;
import com.leesfamily.chuno.room.RoomService;
import com.leesfamily.chuno.room.model.DateTime;
import com.leesfamily.chuno.room.model.PushEntity;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.user.UserRepository;
import com.leesfamily.chuno.user.UserService;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    final private UserService userService;
    final private RoomService roomService;
    final private PushRepository pushRepository;
    final private SMSUtils smsUtils;


    @Async // 병렬로 Scheduler 를 사용할 경우 @Async 추가
    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateTask() throws InterruptedException {
        log.info("scheduler exec");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later10m = now.plusMinutes(10l);
        List<PushEntity> pushList = pushRepository.findAll();
        pushList.forEach((push) -> {
            DateTime roomDt = push.getRoom().getDateTime();
            int roomYear = roomDt.getYear();
            int roomMonth = roomDt.getMonth();
            int roomDay = roomDt.getDay();
            int roomHour = roomDt.getHour();
            int roomMinute = roomDt.getMinute();
            int nowYear = later10m.getYear();
            int nowMonth = later10m.getMonthValue();
            int nowDay = later10m.getDayOfMonth();
            int nowHour = later10m.getHour();
            int nowMinute = later10m.getMinute();
            if(nowMinute == roomMinute && nowHour == roomHour
                    && nowDay == roomDay && nowMonth == roomMonth && nowYear == roomYear) {
                UserEntity user = push.getUser();
                RoomEntity room = push.getRoom();
                log.info("send sms");
                smsUtils.sendOne(user.getPhone(), room.getTitle());
                return;
            }
            nowYear = now.getYear();
            nowMonth = now.getMonthValue();
            nowDay = now.getDayOfMonth();
            nowHour = now.getHour();
            nowMinute = now.getMinute();
            if(nowMinute == roomMinute && nowHour == roomHour
                    && nowDay == roomDay && nowMonth == roomMonth && nowYear == roomYear) {
                pushRepository.delete(push);
            }
        });
    }
}
