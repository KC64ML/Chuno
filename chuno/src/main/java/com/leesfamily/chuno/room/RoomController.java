package com.leesfamily.chuno.room;

import com.leesfamily.chuno.common.model.Location;
import com.leesfamily.chuno.common.util.StatusCodeGeneratorUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.room.model.RoomResponse;
import com.leesfamily.chuno.room.model.RoomEntity;
import com.leesfamily.chuno.room.model.RoomRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @PostMapping("/{condition}/{keyword}")
    public ResponseEntity<Map<String, Object>> getRoomListByConditions(
            Location loc,
            @PathVariable("conditino") String condition,
            @PathVariable("keyword") String keyword) {
        List<RoomResponse> roomList = roomService.getRoomsByConditinos(loc, condition, keyword);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByList(roomList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "방 생성하기", description = "필요한 정보들로 방 정보를 생성한다.")
    @Parameters({
            @Parameter(name = "lat", description = "위도", example = "36.10734231483315"),
            @Parameter(name = "lng", description = "경도", example = "128.4168157734013"),
            @Parameter(name = "title", description = "방 제목", example = "너만 오면 고"),
            @Parameter(name = "isPublic", description = "공개방 여부", example = "true"),
            @Parameter(name = "radius", description = "반경", example = "3"),
            @Parameter(name = "password", description = "비밀번호", example = ""),
            @Parameter(name = "hostId", description = "방장 PK(추후 토큰으로 대체 예정)", example = "1"),
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody RoomRequest room) throws ParseException {
//        Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        Long user_id = (Long) claim.get("user_id");
        String pointWKT = String.format("POINT(%s %s)", room.getLng(), room.getLat());
        log.info(String.valueOf(room.getHostId()));
        log.info(pointWKT);
        Point point = (Point) new WKTReader().read(pointWKT);
        log.info(String.valueOf(point));
        RoomEntity res = roomService.insRoom(room, room.getHostId());
        RoomResponse dto = new RoomResponse(res);
        Map<String, Object> response = statusCodeGeneratorUtils.checkResultByObject(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/join/{roomId}")
    public ResponseEntity<Map<String, Object>> joinRoom(
            @PathVariable("roomId") long roomId,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        Map<String, Object> resMap = roomService.joinRoom(roomId, userId);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/push/{roomId}")
    public ResponseEntity<Map<String, Object>> pushRoom(
            @PathVariable("roomId") long roomId,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        Map<String, Object> res = roomService.pushRoom(roomId, userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
