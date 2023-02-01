package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.util.StatusCodeGeneratorUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.user.model.FriendDTO;
import com.leesfamily.chuno.user.model.FriendEntity;
import com.leesfamily.chuno.user.model.UserEntity;
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
        UserEntity user = userService.getMyProfile(userId);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "내 프로필 수정하기(닉네임)", description = "")
    @Parameters({
            @Parameter(name = "nickname", description = "닉네임", example = "내가전설이다"),
    })
    @PutMapping("/nickname")
    public ResponseEntity<Map<String, Object>> putMyNickname(
            @RequestBody UserEntity user,
            @RequestHeader HttpHeaders requestHeader) {
        user.setId(tokenUtils.getUserIdFromHeader(requestHeader));
        UserEntity updatedUser = userService.putMyProfile(user);

        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByObject(updatedUser);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "내 프로필 수정하기(프로필사진)", description = "")
    @Parameters( {
            @Parameter(name = "imgFile", description = "이미지 파일")
    })
    @PutMapping("/profileImg")
    public ResponseEntity<Map<String, Object>> putMyProfileImg(MultipartFile file, @RequestHeader HttpHeaders requestHeader) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        UserEntity result = userService.putMyProfileImg(file, userId);
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

    @PostMapping("/friend")
    public ResponseEntity<Map<String, Object>> requestFriend(
            @RequestBody FriendDTO friend,
            @RequestHeader HttpHeaders requestHeader
            ) {
        Long userId = tokenUtils.getUserIdFromHeader(requestHeader);
        friend.setFromUserId(userId);
        int result = userService.requestFriend(friend);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByInteger(result);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
