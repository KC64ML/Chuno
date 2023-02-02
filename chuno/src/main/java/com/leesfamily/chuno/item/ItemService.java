package com.leesfamily.chuno.item;

import com.leesfamily.chuno.item.model.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    final private ItemRepository itemRepository;

    public List<ItemEntity> getItemList() {
        List<ItemEntity> list = itemRepository.findAll();
        return list;
    }

    public List<ItemEntity> getTypeItemList(int forRunner) {
        List<ItemEntity> list = itemRepository.findByForRunner(forRunner);
        return list;
    }

}
