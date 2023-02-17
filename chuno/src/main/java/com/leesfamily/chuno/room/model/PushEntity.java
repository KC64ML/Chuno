package com.leesfamily.chuno.room.model;

import com.leesfamily.chuno.common.model.BaseTimeEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "pushes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "push_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

}
