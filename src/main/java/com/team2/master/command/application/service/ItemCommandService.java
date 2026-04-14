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

        String assembledSpec = assembleSpec(request.getItemWidth(), request.getItemDepth(),
                request.getItemHeight(), request.getItemSpec());

        Item item = Item.builder()
                .itemCode(request.getItemCode())
                .itemName(request.getItemName())
                .itemNameKr(request.getItemNameKr())
                .itemSpec(assembledSpec)
                .itemWidth(request.getItemWidth())
                .itemDepth(request.getItemDepth())
                .itemHeight(request.getItemHeight())
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
        String assembledSpec = assembleSpec(request.getItemWidth(), request.getItemDepth(),
                request.getItemHeight(), request.getItemSpec());
        item.updateInfo(
                request.getItemName(), request.getItemNameKr(),
                assembledSpec,
                request.getItemWidth(), request.getItemDepth(), request.getItemHeight(),
                request.getItemUnit(),
                request.getItemPackUnit(), request.getItemUnitPrice(),
                request.getItemWeight(), request.getItemHsCode(),
                request.getItemCategory()
        );
        return item;
    }

    /**
     * W/D/H → "100 × 200 × 300 mm" 형식 조립.
     * 세 값 모두 없으면 클라이언트가 보낸 itemSpec 원본 유지.
     */
    private String assembleSpec(Integer w, Integer d, Integer h, String fallback) {
        if (w != null && d != null && h != null) {
            return w + " × " + d + " × " + h + " mm";
        }
        return fallback;
    }

    @Transactional
    public Item changeStatus(Integer id, ItemStatus status) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("품목을 찾을 수 없습니다."));
        item.changeStatus(status);
        return item;
    }

}
