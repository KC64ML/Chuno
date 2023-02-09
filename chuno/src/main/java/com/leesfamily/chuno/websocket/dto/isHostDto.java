package com.leesfamily.chuno.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class isHostDto {
	private String nickname;
	private boolean is_ready;
	private boolean is_host;
}
