package com.leesfamily.chuno.user;

import com.leesfamily.chuno.common.util.MyFileUtils;
import com.leesfamily.chuno.common.util.TokenUtils;
import com.leesfamily.chuno.item.ItemRepository;
import com.leesfamily.chuno.item.model.ItemEntity;
import com.leesfamily.chuno.user.model.FriendDTO;
import com.leesfamily.chuno.user.model.FriendEntity;
import com.leesfamily.chuno.user.model.UserEntity;
import com.leesfamily.chuno.user.model.UserProfile;
import com.leesfamily.chuno.user.model.dto.UserRankingListDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public UserEntity getProfile(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return null;
        }else {
            return user.get();
        }
    }

    public UserEntity findUserByEmail(String email) {
        Optional<UserEntity> option = userRepository.findByEmail(email);
        if(option.isPresent()) {
            return option.get();
        }else {
            return null;
        }
    }

    public Long register(UserEntity user) {
        UserEntity userEntity = userRepository.saveAndFlush(user);
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

    public UserEntity putMyProfileImg( Long userId, MultipartFile img, String nickname) {
        Optional<UserEntity> option = userRepository.findById(userId);
        UserEntity userEntity = option.get();
        if(userEntity.getProfile() == null) {
            userEntity.setProfile(new UserProfile());
        }
        userEntity.setNickname(nickname);

        String target = "profile/"+userId;

        String saveFileNm = fileUtils.transferTo(img, target);
        String path = target + "/" + saveFileNm;
        log.info("path : " + path);
        log.info("userEntity.getProfile() : " + userEntity.getProfile());
        if(saveFileNm != null){
            userEntity.getProfile().setPath(path);
            userEntity.getProfile().setSaveName(saveFileNm);
        }
        userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public int buyItem(Long userId, Long itemId) {
        UserEntity user = userRepository.findById(itemId).get();
        ItemEntity item = itemRepository.findById(itemId).get();
        UserEntity result = userRepository.saveAndFlush(user);
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
    public UserEntity deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).get();
        try {
            userRepository.delete(user);
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


}
