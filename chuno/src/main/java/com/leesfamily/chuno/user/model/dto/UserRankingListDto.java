package com.leesfamily.chuno.user.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserRankingListDto {

    // 넘겨야할 값 : 닉네임(nickname), 레벨(level), 노비승(runnerWinCount), 추노승(chaserWinCount), 승률( (runnerWinCount + chaserWinCount) / (runnerPlayCount + chaserPlayCount) * 100 : int 형으로
    Long id;
    @Setter
    int rank;
    String nickname; // 닉네임
    int level; // 레벨
    int runnerWinCount; // 노비승
    int chaserWinCount; // 추노승
    int userCountAvg; // 승률

    @QueryProjection
    public UserRankingListDto(Long id, String nickname, int level, int runnerWinCount, int chaserWinCount, int userCountAvg) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
        this.runnerWinCount = runnerWinCount;
        this.chaserWinCount = chaserWinCount;
        this.userCountAvg = userCountAvg;
    }
}
