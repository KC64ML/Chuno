package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.util.MyFileUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.item.ItemRepository;
import com.leesfamily.chuno.item.model.ItemEntity;
import com.leesfamily.chuno.user.model.*;
import com.leesfamily.chuno.user.model.dto.UserInventoryResponse;
import com.leesfamily.chuno.user.model.dto.UserRankingListDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    final private MyFileUtils fileUtils;

    final private UserRepository userRepository;
    final private FriendRepository friendRepository;
    final private ItemRepository itemRepository;
    final private EntityManagerFactory emFactory;
    final private MyFileUtils myFileUtils;
    final private TokenUtils tokenUtils;
    private final InventoryEntityRepository inventoryEntityRepository;


    public UserInventoryResponse getProfile(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        List<InventoryEntity> inventoryList = inventoryEntityRepository.findAllByUser(user.get());
        UserInventoryResponse userInventoryResponse = UserInventoryResponse.toUserInventoryResponse(user.get());
        userInventoryResponse.countingItems(inventoryList);
        if(user.isEmpty()) {
            return null;
        }else {
            return userInventoryResponse;
        }
    }

    public UserEntity findUserByEmail(String email) {
        Optional<UserEntity> option = userRepository.findByEmail(email);
        if(option.isPresent() && !option.get().isDeleted()) {
            return option.get();
        }else {
            return null;
        }
    }

    public Long register(UserEntity user, MultipartFile file) {
        String originPhone = user.getPhone();

        user.setPhone(phoneCheck(originPhone));
        Optional<UserEntity> option = userRepository.findByEmail(user.getEmail());
        if(option.isPresent()) {
            String userNickname = user.getNickname();
            user = option.get();
            user.setDeleted(false);
            user.setMoney(0);
            user.setNickname(userNickname);
        }
        user.setLevel(1);
        UserEntity userEntity = userRepository.saveAndFlush(user);
        putMyProfileImg(userEntity.getId(), file, user.getNickname(), user.getPhone());
        return userEntity.getId();
    }

    public UserEntity putMyProfile(UserEntity user) {
        Optional<UserEntity> userOptional = userRepository.findById(user.getId());
        if(userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            userEntity.setNickname(user.getNickname());
            return userRepository.saveAndFlush(userEntity);
        }else {
            return null;
        }
    }

    public int requestFriend(FriendDTO friend) {
        FriendEntity input = new FriendEntity(userRepository.getOne(friend.getFromUserId()), userRepository.getOne(friend.getToUserId()));
        FriendEntity result = friendRepository.saveAndFlush(input);
        if(result != null) {
            return 1;
        }else {
            return 0;
        }
    }

    public List<UserEntity> getMyFriends(Long userId) {
        Optional<UserEntity> option = userRepository.findById(userId);
        if(option.isPresent()) {
            Optional<List<FriendEntity>> myFriendsOption = friendRepository.findFriendEntitiesByFromUser(option.get());
            if(myFriendsOption.isPresent()) {
                List<UserEntity> myFriends = new ArrayList<>();
                myFriendsOption.get().forEach((friendEntity) -> {
                    myFriends.add(friendEntity.getToUser());
                });
                return myFriends;
            }
        }
        return null;
    }

    public List<UserEntity> getMyFriends(Long userId, String nickname) {
        Optional<List<FriendEntity>> myFriendsOption = friendRepository.findFriendEntitiesByFromUserId(userId);
        if(myFriendsOption.isPresent()) {
            List<UserEntity> myFriends = new ArrayList<>();
            myFriendsOption.get().forEach((friendEntity) -> {
                UserEntity friend = friendEntity.getToUser();
                if(friend.getNickname().contains(nickname)) {
                    myFriends.add(friend);
                }
            });
            return myFriends;
        }
        return null;
    }

    public int isMyFriend(Long myId, Long userId) {
        Optional<FriendEntity> option = friendRepository
                .findByFromUserAndToUser(
                        userRepository.findById(myId).get(),
                        userRepository.findById(userId).get()
                );
        if(option.isPresent()) {
            return 1;
        }else {
            return 0;
        }
    }

    public UserEntity putMyProfileImg( Long userId, MultipartFile img, String nickname, String phone) {
        Optional<UserEntity> option = userRepository.findById(userId);
        UserEntity userEntity = option.get();
        if(userEntity.getProfile() == null) {
            userEntity.setProfile(new UserProfile());
        }
        userEntity.setPhone(phoneCheck(phone));
        userEntity.setNickname(nickname);
        if(img != null) {
            String target = "profile/"+userId;

            String saveFileNm = fileUtils.transferTo(img, target);
            String path = target + "/" + saveFileNm;
            log.info("path : " + path);
            log.info("userEntity.getProfile() : " + userEntity.getProfile());
            if(saveFileNm != null){
                userEntity.getProfile().setPath(path);
                userEntity.getProfile().setSaveName(saveFileNm);
            }
        }
        if(img == null) {
            userEntity.setProfile(null);
        }
        userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public int buyItem(Long userId, Long itemId) {
        UserEntity user = userRepository.findById(userId).get();
        ItemEntity item = itemRepository.findById(itemId).get();
        int curMoney = user.getMoney();
        if(curMoney - item.getPrice() < 0) {
            return 0;
        }
        user.setMoney(curMoney - item.getPrice());
        userRepository.saveAndFlush(user);
        InventoryEntity inven = new InventoryEntity();
        inven.setItem(item);
        inven.setUser(user);
        inventoryEntityRepository.saveAndFlush(inven);
        return 1;
    }

    public Long isExistNickname(String nickname) {
        Long result = userRepository.countByNicknameIs(nickname);
        return result;
    }


    // 랭킹 얻기
    public List<UserRankingListDto> getRankingList(){
        return userRepository.getRankingList();
    }

    // 회원 탈퇴 처리
    public UserEntity deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).get();
        user.setDeleted(true);
        user.setNickname(null);
        try {
            userRepository.saveAndFlush(user);
        }catch (Exception e) {
            return null;
        }
        return user;
    }

    public void deleteUserByNickname(String nickname) {
        UserEntity user = userRepository.findByNickname(nickname).get();
        userRepository.delete(user);
    }

    public int deleteFriend(long userId, Long myId) {
        UserEntity you = userRepository.findById(userId).get();
        UserEntity me = userRepository.findById(myId).get();
        FriendEntity friendEntity = friendRepository.findByFromUserAndToUser(me, you).get();
        friendRepository.delete(friendEntity);
        return 1;
    }


    public UserInventoryResponse useItem(Long userId, Long itemId) {
        UserEntity user = userRepository.findById(userId).get();
        ItemEntity item = itemRepository.findById(itemId).get();
        Optional<InventoryEntity> inven = inventoryEntityRepository.findByUserAndItem(user, item);
        if(inven.isPresent()) {
            inventoryEntityRepository.delete(inven.get());
            return getProfile(user.getId());
        }else {
            return null;
        }
    }



    private String phoneCheck(String phone) {
        for(int i = 0; i < phone.length(); i++) {
            char curNum = phone.charAt(i);
            if(curNum < '0' || curNum > '9') {
                phone.replace(curNum, '\0');
            }
        }
        return phone;
    }
}
