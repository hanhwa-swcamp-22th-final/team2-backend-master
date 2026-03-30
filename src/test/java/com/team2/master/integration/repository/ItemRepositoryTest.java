package com.team2.master.integration.repository;

import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item savedItem;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .itemNameKr("테스트 제품")
                .itemUnit("EA")
                .itemUnitPrice(new BigDecimal("1500.00"))
                .itemCategory("전자부품")
                .itemStatus(ItemStatus.활성)
                .build();
        savedItem = itemRepository.save(item);
    }

    @Test
    @DisplayName("품목 코드로 조회할 수 있다")
    void findByItemCode() {
        // when
        Optional<Item> result = itemRepository.findByItemCode("ITM001");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getItemName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("존재하지 않는 품목 코드로 조회하면 빈 Optional을 반환한다")
    void findByItemCode_notFound() {
        // when
        Optional<Item> result = itemRepository.findByItemCode("NOTEXIST");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("품목 코드 존재 여부를 확인할 수 있다")
    void existsByItemCode() {
        // when & then
        assertThat(itemRepository.existsByItemCode("ITM001")).isTrue();
        assertThat(itemRepository.existsByItemCode("NOTEXIST")).isFalse();
    }

    @Test
    @DisplayName("상태별로 품목을 조회할 수 있다")
    void findByItemStatus() {
        // given
        Item inactiveItem = Item.builder()
                .itemCode("ITM002")
                .itemName("Inactive Product")
                .itemStatus(ItemStatus.비활성)
                .build();
        itemRepository.save(inactiveItem);

        // when
        List<Item> activeItems = itemRepository.findByItemStatus(ItemStatus.활성);
        List<Item> inactiveItems = itemRepository.findByItemStatus(ItemStatus.비활성);

        // then
        assertThat(activeItems).hasSize(1);
        assertThat(inactiveItems).hasSize(1);
    }
}
