package com.leesfamily.chuno.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity(name="profile_img")
//@DynamicInsert
//@DynamicUpdate
//@PrimaryKeyJoinColumn(name = "user_key")
@Embeddable
public class UserProfile {

//    @Column
    private String path;
//    @Column
    private String saveName;
}
