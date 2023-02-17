package com.leesfamily.chuno.user.model.dto;

import com.leesfamily.chuno.item.model.ItemEntity;
import com.leesfamily.chuno.user.model.InventoryEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import com.leesfamily.chuno.user.model.UserProfile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embedded;
import java.util.List;

@Getter
@Setter
@Builder
public class UserInventoryResponse {
    private Long id;
    private String email;
    private String nickname;
    private String phone;
    private int level;
    private int paperCount;
    private int catchCount;
    private int runnerPlayCount;
    private int runnerWinCount;
    private int chaserPlayCount;
    private int chaserWinCount;
    private int exp;
    private boolean isManager;
    private int money;
    private UserProfile profile;
    private int[] items;
    public static UserInventoryResponse toUserInventoryResponse(UserEntity user) {
        if(user.getProfile() == null) {
            user.setProfile(new UserProfile());
        }
        return UserInventoryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .level(user.getLevel())
                .phone(user.getPhone())
                .paperCount(user.getPaperCount())
                .catchCount(user.getCatchCount())
                .runnerPlayCount(user.getRunnerPlayCount())
                .runnerWinCount(user.getRunnerWinCount())
                .chaserPlayCount(user.getChaserPlayCount())
                .chaserWinCount(user.getChaserWinCount())
                .exp(user.getExp())
                .money(user.getMoney())
                .isManager(user.isManager())
                .profile(user.getProfile())
                .build();
    }
    public void countingItems(List<InventoryEntity> inventory) {
        this.items = new int[ItemEntity.AMOUNT_OF_ITEMS];
        inventory.forEach((inven) -> {
            int itemNum = Integer.parseInt(Long.toString(inven.getItem().getId()));
            this.items[itemNum - 1]++;
        });
    }
}
