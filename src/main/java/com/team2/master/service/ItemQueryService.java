package com.team2.master.service;

import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.mapper.ItemQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryService {

    private final ItemQueryMapper itemQueryMapper;

    public Item getItem(Integer id) {
        Item item = itemQueryMapper.findById(id);
        if (item == null) {
            throw new ResourceNotFoundException("품목을 찾을 수 없습니다.");
        }
        return item;
    }

    public List<Item> getAllItems() {
        return itemQueryMapper.findAll();
    }

    public List<Item> getItemsByStatus(ItemStatus status) {
        return itemQueryMapper.findByItemStatus(status.name());
    }
}
