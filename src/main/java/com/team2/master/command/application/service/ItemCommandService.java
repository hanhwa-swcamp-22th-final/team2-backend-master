package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateItemRequest;
import com.team2.master.command.application.dto.UpdateItemRequest;
import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemCommandService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item createItem(CreateItemRequest request) {
        if (itemRepository.existsByItemCode(request.getItemCode())) {
            throw new IllegalStateException("이미 사용 중인 품목 코드입니다.");
        }

        Item item = Item.builder()
                .itemCode(request.getItemCode())
                .itemName(request.getItemName())
                .itemNameKr(request.getItemNameKr())
                .itemSpec(request.getItemSpec())
                .itemUnit(request.getItemUnit())
                .itemPackUnit(request.getItemPackUnit())
                .itemUnitPrice(request.getItemUnitPrice())
                .itemWeight(request.getItemWeight())
                .itemHsCode(request.getItemHsCode())
                .itemCategory(request.getItemCategory())
                .itemStatus(ItemStatus.ACTIVE)
                .itemRegDate(request.getItemRegDate())
                .build();

        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Integer id, UpdateItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("품목을 찾을 수 없습니다."));
        item.updateInfo(
                request.getItemName(), request.getItemNameKr(),
                request.getItemSpec(), request.getItemUnit(),
                request.getItemPackUnit(), request.getItemUnitPrice(),
                request.getItemWeight(), request.getItemHsCode(),
                request.getItemCategory()
        );
        return item;
    }

    @Transactional
    public Item changeStatus(Integer id, ItemStatus status) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("품목을 찾을 수 없습니다."));
        item.changeStatus(status);
        return item;
    }

}
