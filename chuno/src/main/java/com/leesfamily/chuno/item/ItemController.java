package com.leesfamily.chuno.item;

import com.leesfamily.chuno.common.util.StatusCodeGeneratorUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.item.model.ItemEntity;
import com.leesfamily.chuno.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    final private ItemService itemService;
    final private UserService userService;
    final private TokenUtils tokenUtils;
    final private StatusCodeGeneratorUtils statusCodeGeneratorUtils;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getItemList() {
        List<ItemEntity> itemList = itemService.getItemList();
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByList(itemList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{forRunner}")
    public ResponseEntity<Map<String, Object>> getItemList(@PathVariable("forRunner") int forRunner) {
        List<ItemEntity> itemList = itemService.getTypeItemList(forRunner);
        Map<String, Object> res = statusCodeGeneratorUtils.checkResultByList(itemList);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
