package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.util.StatusCodeGeneratorUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.item.ItemService;
import com.leesfamily.chuno.user.model.FriendDTO;
import com.leesfamily.chuno.user.model.FriendEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import com.leesfamily.chuno.user.model.dto.UserInventoryResponse;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    final private ItemService itemService;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private final TokenUtils tokenUtils;
    private final StatusCodeGeneratorUtils statusCodeGeneratorUtils;

    @Operation(summary = "내 프로필 불러오기", description = "")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMyProfile(
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        log.info("userId : " + userId);
        UserInventoryResponse user = userService.getProfile(userId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "상대 프로필 불러오기", description = "")
    @Parameters(
            @Parameter(name = "userId", example = "1")
    )
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getOtherProfile(@PathVariable("userId") Long userId) {
        UserInventoryResponse user = userService.getProfile(userId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<Map<String, Object>> nestedCheckNickname(@PathVariable("nickname") String nickname) {
        Long result = userService.isExistNickname(nickname);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByNumber(result);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//    @Operation(summary = "내 프로필 수정하기(닉네임)", description = "")
//    @Parameters({
//            @Parameter(name = "nickname", description = "닉네임", example = "내가전설이다"),
//    })
//    @PutMapping("/nickname")
//    public ResponseEntity<Map<String, Object>> putMyNickname(
//            @RequestBody UserEntity user,
//            @RequestHeader HttpHeaders requestHeader) {
//        user.setId(tokenUtils.getUserIdFromHeader(requestHeader));
//        UserEntity updatedUser = userService.putMyProfile(user);
//
//        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(updatedUser);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

    @Operation(summary = "내 프로필 수정하기(프로필사진)", description = "")
    @Parameters( {
            @Parameter(name = "file", description = "이미지 파일"),
            @Parameter(name = "nickname", description = "닉네임")
    })
    @PutMapping(value = "/profile",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> putMyProfileImg(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "nickname") String nickname,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        userService.putMyProfileImg(userId, file, nickname);
        UserInventoryResponse result = userService.getProfile(userId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByObject(result);
        return ResponseEntity.ok().body(resMap);
    }

    @GetMapping("/friend")
    public ResponseEntity<Map<String, Object>> getMyFriends(@RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        List<UserEntity> myFriends = userService.getMyFriends(userId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByList(myFriends);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/friend/{userId}")
    public ResponseEntity<Map<String, Object>> isMyFriend(
            @RequestHeader HttpHeaders requestHeader,
            @PathVariable("userId") Long userId
    ) {
        Long myId = tokenUtils.getUserIdFromHeader(requestHeader);
        int isMyFriend = userService.isMyFriend(myId, userId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByNumber(isMyFriend);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/friend")
    public ResponseEntity<Map<String, Object>> requestFriend(
            @RequestBody FriendDTO friend,
            @RequestHeader HttpHeaders requestHeader
            ) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        friend.setFromUserId(userId);
        int result = userService.requestFriend(friend);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByNumber(result);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @DeleteMapping("/friend/{userId}")
    public ResponseEntity<Map<String, Object>> deleteFriend(
            @PathVariable long userId,
            @RequestHeader HttpHeaders requestHeader
    ) {
        Long myId = tokenUtils.getUserIdFromHeader(requestHeader);
        int result = userService.deleteFriend(userId, myId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByNumber(result);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/shop/{itemId}")
    public ResponseEntity<Map<String, Object>> buyItem(
            @PathVariable("itemId") Long itemId,
            @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        int result = userService.buyItem(userId, itemId);
        Map<String, Object> resMap = statusCodeGeneratorUtils.checkResultByNumber(result);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    // getHighPriorityList
    @Operation(summary = "User Rank 조회", description = "")
    @GetMapping("/rank")
    public ResponseEntity<List> getRankingList(){
        return ResponseEntity.ok(userService.getRankingList());
    }
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteUser(
            @RequestHeader HttpHeaders requestHeader
    ) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        UserEntity user = userService.deleteUser(userId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/dev/delete/{nickname}")
    public ResponseEntity<Map<String, Object>> deleteUserForDev(
            @PathVariable("nickname") String nickname
    ) {
        userService.deleteUserByNickname(nickname);
        return null;
    }

}
