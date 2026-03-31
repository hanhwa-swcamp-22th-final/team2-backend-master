package com.team2.master.unit.service;

import com.team2.master.dto.CreateItemRequest;
import com.team2.master.dto.UpdateItemRequest;
import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.ItemQueryMapper;
import com.team2.master.command.repository.ItemRepository;
import com.team2.master.command.service.ItemCommandService;
import com.team2.master.query.service.ItemQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Nested
    @DisplayName("ItemQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private ItemQueryMapper itemQueryMapper;

        @InjectMocks
        private ItemQueryService itemQueryService;

        private Item item;

        @BeforeEach
        void setUp() {
            item = Item.builder()
                    .itemCode("ITM001")
                    .itemName("Test Product")
                    .itemNameKr("테스트 제품")
                    .itemSpec("100x200mm")
                    .itemUnit("EA")
                    .itemUnitPrice(new BigDecimal("1500.00"))
                    .itemCategory("전자부품")
                    .itemStatus(ItemStatus.ACTIVE)
                    .build();
        }

        @Test
        @DisplayName("ID로 품목을 조회할 수 있다")
        void getItem_success() {
            // given
            given(itemQueryMapper.findById(1)).willReturn(item);

            // when
            Item result = itemQueryService.getItem(1);

            // then
            assertThat(result.getItemName()).isEqualTo("Test Product");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getItem_notFound() {
            // given
            given(itemQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> itemQueryService.getItem(999))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("품목을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("전체 품목 목록을 조회할 수 있다")
        void getAllItems() {
            // given
            given(itemQueryMapper.findAll()).willReturn(List.of(item));

            // when
            List<Item> result = itemQueryService.getAllItems();

            // then
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("ItemCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private ItemRepository itemRepository;

        @InjectMocks
        private ItemCommandService itemCommandService;

        private Item item;

        @BeforeEach
        void setUp() {
            item = Item.builder()
                    .itemCode("ITM001")
                    .itemName("Test Product")
                    .itemNameKr("테스트 제품")
                    .itemSpec("100x200mm")
                    .itemUnit("EA")
                    .itemUnitPrice(new BigDecimal("1500.00"))
                    .itemCategory("전자부품")
                    .itemStatus(ItemStatus.ACTIVE)
                    .build();
        }

        @Test
        @DisplayName("품목을 생성할 수 있다")
        void createItem_success() {
            // given
            CreateItemRequest request = CreateItemRequest.builder()
                    .itemCode("ITM001")
                    .itemName("Test Product")
                    .itemNameKr("테스트 제품")
                    .itemSpec("100x200mm")
                    .itemUnit("EA")
                    .itemUnitPrice(new BigDecimal("1500.00"))
                    .itemCategory("전자부품")
                    .build();
            given(itemRepository.existsByItemCode("ITM001")).willReturn(false);
            given(itemRepository.save(any(Item.class))).willReturn(item);

            // when
            Item result = itemCommandService.createItem(request);

            // then
            assertThat(result.getItemName()).isEqualTo("Test Product");
            assertThat(result.getItemCode()).isEqualTo("ITM001");
            verify(itemRepository).save(any(Item.class));
        }

        @Test
        @DisplayName("중복 품목 코드로 생성 시 예외가 발생한다")
        void createItem_duplicateCode() {
            // given
            CreateItemRequest request = CreateItemRequest.builder()
                    .itemCode("ITM001")
                    .itemName("Test Product")
                    .build();
            given(itemRepository.existsByItemCode("ITM001")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> itemCommandService.createItem(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 사용 중인 품목 코드");
        }

        @Test
        @DisplayName("품목 정보를 수정할 수 있다")
        void updateItem_success() {
            // given
            UpdateItemRequest request = UpdateItemRequest.builder()
                    .itemName("Updated Product")
                    .itemNameKr("수정 제품")
                    .itemUnitPrice(new BigDecimal("2500.00"))
                    .build();
            given(itemRepository.findById(1)).willReturn(Optional.of(item));

            // when
            Item result = itemCommandService.updateItem(1, request);

            // then
            assertThat(result.getItemName()).isEqualTo("Updated Product");
            assertThat(result.getItemNameKr()).isEqualTo("수정 제품");
            assertThat(result.getItemUnitPrice()).isEqualTo(new BigDecimal("2500.00"));
        }

        @Test
        @DisplayName("품목 상태를 변경할 수 있다")
        void changeStatus_success() {
            // given
            given(itemRepository.findById(1)).willReturn(Optional.of(item));

            // when
            Item result = itemCommandService.changeStatus(1, ItemStatus.INACTIVE);

            // then
            assertThat(result.getItemStatus()).isEqualTo(ItemStatus.INACTIVE);
        }

        @Test
        @DisplayName("동일 상태로 변경 시 예외가 발생한다")
        void changeStatus_sameStatus() {
            // given
            given(itemRepository.findById(1)).willReturn(Optional.of(item));

            // when & then
            assertThatThrownBy(() -> itemCommandService.changeStatus(1, ItemStatus.ACTIVE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 ACTIVE 상태입니다");
        }
    }
}
