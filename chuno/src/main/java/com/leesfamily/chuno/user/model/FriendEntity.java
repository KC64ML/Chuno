package com.leesfamily.chuno.user.model;

import com.leesfamily.chuno.common.model.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "friends")
public class FriendEntity extends BaseTimeEntity implements Serializable {

    public FriendEntity() {}

    public FriendEntity(UserEntity fromUser, UserEntity toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", referencedColumnName = "user_id")
//    @Column(name = "from_user_key")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", referencedColumnName = "user_id")
//    @Column(name = "to_user_key")
    private UserEntity toUser;

    /*@Column(nullable = false)
    @ColumnDefault("false")
    private boolean isFriend;*/
}
