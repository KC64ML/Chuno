package com.leesfamily.chuno.user.model;

import com.leesfamily.chuno.common.model.BaseTimeEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "friends")
public class FriendEntity extends BaseTimeEntity implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "from_user_key", referencedColumnName = "user_key")
//    @Column(name = "from_user_key")
    private UserEntity fromUser;
    @Id
    @ManyToOne
    @JoinColumn(name = "to_user_key", referencedColumnName = "user_key")
//    @Column(name = "to_user_key")
    private UserEntity toUser;
}
