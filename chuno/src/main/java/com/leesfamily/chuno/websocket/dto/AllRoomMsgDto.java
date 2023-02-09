package com.leesfamily.chuno.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllRoomMsgDto {
	private String type;
	private HashMap<Integer, Integer> room;
}
