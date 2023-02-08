package com.leesfamily.chuno.user.model;

import com.leesfamily.chuno.common.model.BaseTimeEntity;
import com.leesfamily.chuno.item.model.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="users")
@DynamicInsert
@DynamicUpdate
//@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, length = 40, unique = true)
    private String email;
    @Column(nullable = true, length = 7, unique = true)
    private String nickname;
    @Column(nullable = true)
    private String phone;
    @Column(nullable = false)
    @ColumnDefault("1")
    private int level;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int paperCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int catchCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int runnerPlayCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int runnerWinCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int chaserPlayCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int chaserWinCount;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int exp;
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isManager;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int money;

    @Embedded
    private UserProfile profile;

    @Column
    @ColumnDefault("false")
    private boolean isDeleted;


//    @OneToMany(mappedBy = "fromUser")
//    private List<FriendEntity> friendsOut = new ArrayList<>();
//
//    @OneToMany(mappedBy = "toUser")
//    private List<FriendEntity> friendsIn = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(name = "inventory",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "item_id")
//    )
//    private List<ItemEntity> inventory = new ArrayList<>();
}
