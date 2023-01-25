package com.leesfamily.chuno.user.vo;

import com.leesfamily.chuno.common.vo.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="users")
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userKey;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    @ColumnDefault("1")
    private int level;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int paperCount;
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


}
