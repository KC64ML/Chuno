package com.leesfamily.chuno.websocket;

import java.util.ArrayList;
import java.util.HashMap;

import com.leesfamily.chuno.websocket.dto.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SocketRoomHandler extends TextWebSocketHandler {

	ArrayList<WebSocketSession> allSession = new ArrayList<>();
	HashMap<Integer, String> room_host = new HashMap<>();
	HashMap<Integer, ArrayList<PlayerDto>> room_map = new HashMap<>();
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("누군과 소켓과 연결됨");
		allSession.add(session);
	}//

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// 온 메시지를 해시맵으로 매핑해줘요
		HashMap<String, String> map = mapper.readValue((String) message.getPayload(), HashMap.class);
		// 온 메시지를 각 임시 변수에 담아줘요 level 은 무조건 1로 할게요
		System.out.println(map);
		String event = (String) map.get("event");
		String nickname = (String) map.get("nickname");
		int room = Integer.parseInt((String) map.get("room"));
		int level = 1;
//		int level = Integer.parseInt((String) map.get("level"));
		System.out.println(level);
//		 각 이벤트에 따라 if문을 실행해 줘요
		if (event.equals("make")) {
			System.out.println("방을 만들어요");
			// 있는방인지 확인해요
			if (room_map.get(room) != null) {
				ErrorMsgDto emd = new ErrorMsgDto("error", "이미 있는 방이에요");
				TextMessage tm = new TextMessage(mapper.writeValueAsString(emd));
				session.sendMessage(tm);
				return;//
			}
			// 룸 호스트를 설정해요
			room_host.put(room, nickname);
			room_map.put(room, new ArrayList<PlayerDto>());
			// 룸에 플레이어를 넣어요
			PlayerDto playerDto = new PlayerDto(session, nickname, false, true);
			room_map.get(room).add(playerDto);

			// 전체 사람들에게 방 목록을 전송할게요
			AllRoomMsgDto dto = new AllRoomMsgDto("all_room", new HashMap<Integer, Integer>());
			for (Integer j : room_map.keySet()) {
				dto.getRoom().put(j, room_map.get(j).size());
			}
			TextMessage msg = new TextMessage(mapper.writeValueAsString(dto));
			for (WebSocketSession w : allSession) {
				w.sendMessage(msg);
			}

			// 나에게 내정보(내가 현재있는 방번호와, 그방의 사람들)을 보여줘요
			MsgDto dto2 = new MsgDto("me", room, new ArrayList<isHostDto>());
			for (PlayerDto p : room_map.get(room)) {
				dto2.getPlayers().add(new isHostDto(p.getNickname(), p.isReady(), p.isHost()));
			}
			TextMessage msg2 = new TextMessage(mapper.writeValueAsString(dto2));
			session.sendMessage(msg2);

		} else if (event.equals("enter")) {
			System.out.println("방에 들어가요");
			// 방이 있는지 확인부터 해요
			if (room_map.get(room) == null) {
				ErrorMsgDto emd = new ErrorMsgDto("error", "없는 방이에요");
				TextMessage tm = new TextMessage(mapper.writeValueAsString(emd));
				session.sendMessage(tm);
				return;//
			}
			// 이번에 들어온 닉네임과 호스트 닉네임과 같은지 확인해요
			boolean is_host = nickname.equals(room_host.get(room));
			PlayerDto playerDto = new PlayerDto(session, nickname, false, is_host);
			// 룸에 플레이어를 넣어요
			room_map.get(room).add(playerDto);
			// 전체 사람들에게 방 목록을 전송할게요
			AllRoomMsgDto dto = new AllRoomMsgDto("all_room", new HashMap<Integer, Integer>());
			for (Integer j : room_map.keySet()) {
				dto.getRoom().put(j, room_map.get(j).size());
			}
			TextMessage msg = new TextMessage(mapper.writeValueAsString(dto));
			for (WebSocketSession w : allSession) {
				w.sendMessage(msg);
			}

			// 방에 있는 사람들에게 내정보(내가 현재있는 방번호와, 그방의 사람들)을 보여줘요
			MsgDto dto2 = new MsgDto("me", room, new ArrayList<isHostDto>());
			for (PlayerDto p : room_map.get(room)) {
				dto2.getPlayers().add(new isHostDto(p.getNickname(), p.isReady(), p.isHost()));
			}
			TextMessage msg2 = new TextMessage(mapper.writeValueAsString(dto2));
			for (PlayerDto p : room_map.get(room)) {
				p.getSession().sendMessage(msg2);
			}
		} else if (event.equals("leave")) {
			System.out.println("방을 떠나요");
			// 방이 있는지 확인부터 해요
			if (room_map.get(room) == null) {
				ErrorMsgDto emd = new ErrorMsgDto("error", "없는 방이에요");
				TextMessage tm = new TextMessage(mapper.writeValueAsString(emd));
				session.sendMessage(tm);
				return;//
			}
			for (PlayerDto p : room_map.get(room)) {
				if (!p.getNickname().equals(nickname))
					continue;
				room_map.get(room).remove(p);
				break;
			}
			// 방에 있는 사람들에게 내정보(내가 현재있는 방번호와, 그방의 사람들)을 보여줘요//
			MsgDto dto2 = new MsgDto("me", room, new ArrayList<isHostDto>());
			for (PlayerDto p : room_map.get(room)) {
				dto2.getPlayers().add(new isHostDto(p.getNickname(), p.isReady(), p.isHost()));
			}
			TextMessage msg2 = new TextMessage(mapper.writeValueAsString(dto2));
			for (PlayerDto p : room_map.get(room)) {
				p.getSession().sendMessage(msg2);
			}
			// 나에게 내가 방을 떠났단 정보를 보내요
			HashMap<String, String> dto3 = new HashMap<>();
			dto3.put("type", "leave");
			TextMessage msg3 = new TextMessage(mapper.writeValueAsString(dto3));
			session.sendMessage(msg3);
		} else if (event.equals("clear")) {
			System.out.println("방을 없애요");
			// 방이 있는지 확인부터 해요
			if (room_map.get(room) == null) {
				ErrorMsgDto emd = new ErrorMsgDto("error", "없는 방이에요");
				TextMessage tm = new TextMessage(mapper.writeValueAsString(emd));
				session.sendMessage(tm);
				return;//
			}
			// 방장인지 확인을 해요
			boolean is_host = nickname.equals(room_host.get(room));
			if (is_host) {
				System.out.println("방장이 맞네요. 방을 없앨게요");
				for (PlayerDto p : room_map.get(room)) {
					WebSocketSession w = p.getSession();
					TextMessage msg = new TextMessage("leave");
					w.sendMessage(msg);
				}
				// 우리방 사람들에게 내가 방을 떠났단 정보를 보내요
				HashMap<String, String> dto3 = new HashMap<>();
				dto3.put("type", "leave");
				TextMessage msg3 = new TextMessage(mapper.writeValueAsString(dto3));
				for (PlayerDto p : room_map.get(room)) {
					p.getSession().sendMessage(msg3);
				}//
				room_host.remove(room);
				room_map.remove(room);
				// 전체 사람들에게 방 목록을 전송할게요
				AllRoomMsgDto dto = new AllRoomMsgDto("all_room", new HashMap<Integer, Integer>());
				for (Integer j : room_map.keySet()) {
					dto.getRoom().put(j, room_map.get(j).size());
				}
				TextMessage msg = new TextMessage(mapper.writeValueAsString(dto));
				for (WebSocketSession w : allSession) {
					w.sendMessage(msg);
				}
			} else {
				System.out.println("방장이 아니라서 방을 없앨 수 없어요");
				ErrorMsgDto emd = new ErrorMsgDto("error", "방장이 아니라서 방을 없앨수 없어요");
				TextMessage tm = new TextMessage(mapper.writeValueAsString(emd));
				session.sendMessage(tm);
			}
		} else if (event.equals("ready")) {
			System.out.println("방에서 레디를 해요");
			for (PlayerDto p : room_map.get(room)) {
				if (!p.getNickname().equals(nickname))
					continue;
				p.setReady(!p.isReady());
				break;
			}
			MsgDto dto2 = new MsgDto("me", room, new ArrayList<isHostDto>());
			for (PlayerDto p : room_map.get(room)) {
				dto2.getPlayers().add(new isHostDto(p.getNickname(), p.isReady(), p.isHost()));
			}
			TextMessage msg2 = new TextMessage(mapper.writeValueAsString(dto2));
			for (PlayerDto p : room_map.get(room)) {
				p.getSession().sendMessage(msg2);
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("누군가 소켓이 끊어짐");
		for (WebSocketSession w : allSession) {
			if (w.equals(session)) {
				allSession.remove(w);
			}
		}
	}
//
}
