package com.team2.master.unit.entity;

import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item createDefaultItem() {
        return Item.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .itemNameKr("테스트 제품")
                .itemSpec("100x200mm")
                .itemUnit("EA")
                .itemPackUnit("BOX")
                .itemUnitPrice(new BigDecimal("1500.00"))
                .itemWeight(new BigDecimal("2.500"))
                .itemHsCode("8541.10")
                .itemCategory("전자부품")
                .itemStatus(ItemStatus.활성)
                .itemRegDate(LocalDate.of(2025, 1, 1))
                .build();
    }

    // === 생성 테스트 ===

    @Test
    @DisplayName("품목 생성 성공: 필수 필드가 정상 설정된다.")
    void createItem_Success() {
        // given & when
        Item item = createDefaultItem();

        // then
        assertEquals("ITM001", item.getItemCode());
        assertEquals("Test Product", item.getItemName());
        assertEquals("테스트 제품", item.getItemNameKr());
        assertEquals("100x200mm", item.getItemSpec());
        assertEquals("EA", item.getItemUnit());
        assertEquals("BOX", item.getItemPackUnit());
        assertEquals(new BigDecimal("1500.00"), item.getItemUnitPrice());
        assertEquals(new BigDecimal("2.500"), item.getItemWeight());
        assertEquals("8541.10", item.getItemHsCode());
        assertEquals("전자부품", item.getItemCategory());
        assertEquals(ItemStatus.활성, item.getItemStatus());
    }

    @Test
    @DisplayName("품목 생성 시 상태 미지정이면 기본값 활성으로 설정된다.")
    void createItem_DefaultStatus() {
        // given & when
        Item item = Item.builder()
                .itemCode("ITM002")
                .itemName("Default Product")
                .build();

        // then
        assertEquals(ItemStatus.활성, item.getItemStatus());
    }

    // === 상태 변경 ===

    @Test
    @DisplayName("상태 변경 성공: 활성에서 비활성으로 변경된다.")
    void changeStatus_ToInactive_Success() {
        // given
        Item item = createDefaultItem();

        // when
        item.changeStatus(ItemStatus.비활성);

        // then
        assertEquals(ItemStatus.비활성, item.getItemStatus());
    }

    @Test
    @DisplayName("상태 변경 성공: 비활성에서 활성으로 변경된다.")
    void changeStatus_ToActive_Success() {
        // given
        Item item = Item.builder()
                .itemCode("ITM003")
                .itemName("Inactive Product")
                .itemStatus(ItemStatus.비활성)
                .build();

        // when
        item.changeStatus(ItemStatus.활성);

        // then
        assertEquals(ItemStatus.활성, item.getItemStatus());
    }

    @Test
    @DisplayName("상태 변경 실패: 동일 상태로 변경 시 예외가 발생한다.")
    void changeStatus_SameStatus_ThrowsException() {
        // given
        Item item = createDefaultItem();

        // when & then
        assertThrows(IllegalStateException.class,
                () -> item.changeStatus(ItemStatus.활성));
    }

    // === 활성 확인 ===

    @Test
    @DisplayName("활성 확인: 활성 상태이면 true를 반환한다.")
    void isActive_ActiveItem_ReturnsTrue() {
        // given
        Item item = createDefaultItem();

        // when & then
        assertTrue(item.isActive());
    }

    @Test
    @DisplayName("활성 확인: 비활성 상태이면 false를 반환한다.")
    void isActive_InactiveItem_ReturnsFalse() {
        // given
        Item item = Item.builder()
                .itemCode("ITM004")
                .itemName("Inactive Product")
                .itemStatus(ItemStatus.비활성)
                .build();

        // when & then
        assertFalse(item.isActive());
    }

    // === 정보 수정 ===

    @Test
    @DisplayName("정보 수정 성공: 품목 정보를 수정할 수 있다.")
    void updateInfo_Success() {
        // given
        Item item = createDefaultItem();

        // when
        item.updateInfo("Updated Product", "수정 제품", "200x300mm",
                "SET", "PALLET", new BigDecimal("2500.00"),
                new BigDecimal("5.000"), "8541.20", "반도체");

        // then
        assertEquals("Updated Product", item.getItemName());
        assertEquals("수정 제품", item.getItemNameKr());
        assertEquals("200x300mm", item.getItemSpec());
        assertEquals("SET", item.getItemUnit());
        assertEquals("PALLET", item.getItemPackUnit());
        assertEquals(new BigDecimal("2500.00"), item.getItemUnitPrice());
        assertEquals(new BigDecimal("5.000"), item.getItemWeight());
        assertEquals("8541.20", item.getItemHsCode());
        assertEquals("반도체", item.getItemCategory());
    }

    @Test
    @DisplayName("정보 수정: null 값은 기존 값을 유지한다.")
    void updateInfo_NullValuesKeepExisting() {
        // given
        Item item = createDefaultItem();

        // when
        item.updateInfo(null, null, null, null, null, null, null, null, null);

        // then
        assertEquals("Test Product", item.getItemName());
        assertEquals("테스트 제품", item.getItemNameKr());
        assertEquals(new BigDecimal("1500.00"), item.getItemUnitPrice());
    }

    // === @PrePersist / @PreUpdate 라이프사이클 메서드 ===

    @Test
    @DisplayName("onCreate 호출 시 createdAt과 updatedAt이 설정된다.")
    void onCreate_SetsTimestamps() {
        // given
        Item item = createDefaultItem();
        assertNull(item.getCreatedAt());
        assertNull(item.getUpdatedAt());

        // when
        item.onCreate();

        // then
        assertNotNull(item.getCreatedAt());
        assertNotNull(item.getUpdatedAt());
    }

    @Test
    @DisplayName("onUpdate 호출 시 updatedAt이 갱신된다.")
    void onUpdate_SetsUpdatedAt() {
        // given
        Item item = createDefaultItem();
        item.onCreate();

        // when
        item.onUpdate();

        // then
        assertNotNull(item.getUpdatedAt());
    }
}
