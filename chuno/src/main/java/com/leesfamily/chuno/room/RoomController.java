package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.common.util.StatusCodeGeneratorUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.room.model.PushEntity;
import com.leesfamily.chuno.room.model.RoomResponse;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.room.model.RoomRequest;
import com.leesfamily.chuno.room.model.dto.*;
import io.swagger.annotations.ResponseHeader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "posts", description = "게시물 API")
@RestController
@CrossOrigin
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private Logger log = LoggerFactory.getLogger(RoomController.class);
    final private RoomService roomService;
    final private TokenUtils tokenUtils;
    final private StatusCodeGeneratorUtils statusCodeGeneratorUtils;

    @Value("${secret.key}")
    String secretKey;


    @Operation(summary = "방 목록 가져오기", description = "사용자의 위치정보를 기반으로 주변 방들을 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RoomEntity.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({
            @Parameter(name = "lat", description = "위도", example = "36.10734231483315"),
            @Parameter(name = "lng", description = "경도", example = "128.4168157734013")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getRoomList(Location loc) {
        List<RoomResponse> roomList = roomService.getNearByRooms(loc);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByList(roomList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "방 제목 검색", description = "(경도, 위도), 방제목 keyword 입력")
    @Parameters({
            @Parameter(name = "lat", description = "위도", example = "36.10734231483315"),
            @Parameter(name = "lng", description = "경도", example = "128.4168157734013")
    })
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> getRoomListByConditions(@RequestBody RoomListByConditionsDto roomListByConditionDto) {
        List<RoomResponse> roomList = roomService.getRoomsByConditinos(roomListByConditionDto);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByList(roomList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Map<String, Object>> getRoomById(
            @PathVariable("roomId") Long roomId
    ) {
        RoomResponse roomResponse = roomService.getRoomById(roomId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(roomResponse);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    
    @Operation(summary = "방 생성하기", description = "필요한 정보들로 방 정보를 생성한다.")
    @Parameters({
            @Parameter(name = "lat", description = "위도", example = "36.10734231483315"),
            @Parameter(name = "lng", description = "경도", example = "128.4168157734013"),
            @Parameter(name = "title", description = "방 제목", example = "너만 오면 고"),
            @Parameter(name = "maxPlayers", description = "최대 인원", example = "4"),
            @Parameter(name = "isPublic", description = "공개방 여부", example = "true"),
            @Parameter(name = "radius", description = "반경", example = "3"),
            @Parameter(name = "password", description = "비밀번호", example = ""),
            @Parameter(name = "isToday", description = "오늘인지 내일인지", example = "true"),
            @Parameter(name = "hour", description = "시간", example = "21"),
            @Parameter(name = "minute", description = "분", example = "54"),
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRoom(
            @RequestBody RoomRequest room,
            @RequestHeader HttpHeaders requestHeader
    ) throws ParseException {
//        Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        Long user_id = (Long) claim.get("user_id");
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        room.setHostId(userId);
        log.info(String.valueOf(room.getHostId()));
        RoomEntity res = roomService.insRoom(room, room.getHostId());
        RoomResponse dto = new RoomResponse(res);
        Long roomId = dto.getId();
        Map<String, Object> response = statusCodeGeneratorUtils.checkResultByNumber(roomId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<Map<String, Object>> joinRoom(
            @PathVariable("roomId") long roomId,
            @RequestBody(required = false) String password,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        Map<String, Object> resMap = roomService.joinRoom(roomId, userId, password);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/push/{roomId}")
    public ResponseEntity<Map<String, Object>> pushRoom(
            @PathVariable("roomId") long roomId,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        PushEntity res = roomService.pushRoom(roomId, userId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByObject(res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }
    @DeleteMapping("/push/{roomId}")
    public ResponseEntity<Map<String, Object>> unPushRoom(
            @PathVariable("roomId") long roomId,
            @RequestHeader HttpHeaders requestHeader
    ) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        int res = roomService.unPushRoom(roomId, userId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByNumber(res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @Operation(summary = "게임 시작", description = "추노 노비 정함, 노비 문서 위치, 방 정보 전달")
    @ApiResponse(
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RoomGameStartResponseDto.class))
                    )
            }
    )
    @PostMapping("/startRoom")
    public ResponseEntity<RoomGameStartResponseDto> startRoom(@RequestBody RoomGameStartRequestDto roomStartRequestDto){
        return ResponseEntity.ok(roomService.startRoom(roomStartRequestDto));
    }


    // 게임 끝났을 때 api 요청
    // - 승리한 사람은 승리 증가
    // - 게임 수 증가 (추노꾼이라면 추노 횟수 증가, 노비라면 노비 횟수 증가
    @Transactional
    @Operation(summary = " 게임 종료 후 정보 업데이트", description = "승리 했을 경우 승리 횟수 + 1, 게임 횟수 + 1 (추노 승 + 1, 추노 게임 횟수 + 1) 넣어주세요. ")
    @PutMapping("/endRoom")
    public ResponseEntity<RoomGameEndRequestDto> endRoom(
            @RequestBody RoomGameEndRequestDto roomGameEndRequestDto,
            @RequestHeader HttpHeaders requestHeader
    ){
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        return ResponseEntity.ok(roomService.endRoom(roomGameEndRequestDto, userId));
    }

    // 노비 문서 위치를 프론트로 받았을 경우, 백엔드에서 노비 문서 위치를 다시 재배치한다.
    @Operation(summary = "노비 문서 위치 재배치")
    @PostMapping("/resladoc")
    public ResponseEntity<List<RoomGameStartSlaveDocumentDto>> relocationSlaveDocument(
            @RequestBody RoomGameRelocationSlaveDocumentRequestDto requestDto
    ){
        return ResponseEntity.ok(roomService.relocationSlaveDocument(requestDto));
    }



    // 게임 level과 해당 레벨 max 경험치 반환해주는 api
    @Operation(summary = "게임 level과 경험치", description = "default level : 0, exp : 0 (+1000단위)")
    @GetMapping("/levelandexp")
    public ResponseEntity<List<RoomGetLevelAndExperienceDto>> getUserLevelAndExperience(
    ){
        return ResponseEntity.ok(roomService.getUserLevelAndExperience());
    }

    // userId, level을 주면 그 userId의 level을 변경해주는 api : update문
    // - userId, level을 주면 해당 userId의 level을 업데이트한다.
    @Transactional
    @Operation(summary = "userId level 변경", description = "user의 LEVEL을 변경해준다.")
    @PostMapping("/updatelevel")
    public ResponseEntity<Map<String, Object>> updateUserLevel(
            int level,
            @RequestHeader HttpHeaders headers
    ){
        Long userId = tokenUtils.getUserIdFromHeader(headers);
        int res = (int)roomService.updateUserLevel(userId, level);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByNumber(res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }
}
