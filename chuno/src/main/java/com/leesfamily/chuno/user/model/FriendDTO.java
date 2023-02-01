package com.leesfamily.chuno.user.model;

import lombok.Data;

@Data
public class FriendDTO {
    private Long toUserId;
    private Long fromUserId;
}
