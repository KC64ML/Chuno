package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.dto.UserRankingListDto;

import java.util.List;

public interface UserRepositoryCustom {

    // 사용자 랭킹 조회
    List<UserRankingListDto> getRankingList();
}
