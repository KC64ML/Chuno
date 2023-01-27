package com.leesfamily.chuno.user;

import com.leesfamily.chuno.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";





    @GetMapping
    public ResponseEntity<UserEntity> getMyPropile(@RequestBody String tokken) {
        return null;
    }

}
