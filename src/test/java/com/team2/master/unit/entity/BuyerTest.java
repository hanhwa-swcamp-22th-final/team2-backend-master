package com.team2.master.unit.entity;

import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyerTest {

    private Client createDefaultClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
    }

    private Buyer createDefaultBuyer() {
        return Buyer.builder()
                .client(createDefaultClient())
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
    }

    // === 생성 테스트 ===

    @Test
    @DisplayName("바이어 생성 성공: 필수 필드가 정상 설정된다.")
    void createBuyer_Success() {
        // given & when
        Buyer buyer = createDefaultBuyer();

        // then
        assertNotNull(buyer.getClient());
        assertEquals("John Doe", buyer.getBuyerName());
        assertEquals("Manager", buyer.getBuyerPosition());
        assertEquals("john@test.com", buyer.getBuyerEmail());
        assertEquals("010-1234-5678", buyer.getBuyerTel());
    }

    @Test
    @DisplayName("바이어 생성 실패: 거래처가 null이면 예외가 발생한다.")
    void createBuyer_NullClient_ThrowsException() {
        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> Buyer.builder()
                        .client(null)
                        .buyerName("John Doe")
                        .build());
    }

    // === 정보 수정 ===

    @Test
    @DisplayName("정보 수정 성공: 바이어 정보를 수정할 수 있다.")
    void updateInfo_Success() {
        // given
        Buyer buyer = createDefaultBuyer();

        // when
        buyer.updateInfo("Jane Smith", "Director", "jane@test.com", "010-9876-5432");

        // then
        assertEquals("Jane Smith", buyer.getBuyerName());
        assertEquals("Director", buyer.getBuyerPosition());
        assertEquals("jane@test.com", buyer.getBuyerEmail());
        assertEquals("010-9876-5432", buyer.getBuyerTel());
    }

    @Test
    @DisplayName("정보 수정: null 값은 기존 값을 유지한다.")
    void updateInfo_NullValuesKeepExisting() {
        // given
        Buyer buyer = createDefaultBuyer();

        // when
        buyer.updateInfo(null, null, null, null);

        // then
        assertEquals("John Doe", buyer.getBuyerName());
        assertEquals("Manager", buyer.getBuyerPosition());
        assertEquals("john@test.com", buyer.getBuyerEmail());
        assertEquals("010-1234-5678", buyer.getBuyerTel());
    }

    @Test
    @DisplayName("바이어 생성 실패: 바이어명이 null이면 예외가 발생한다.")
    void createBuyer_NullBuyerName_ThrowsException() {
        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> Buyer.builder()
                        .client(createDefaultClient())
                        .buyerName(null)
                        .build());
    }

    // === 거래처 관계 ===

    @Test
    @DisplayName("바이어는 거래처에 속한다.")
    void buyer_BelongsToClient() {
        // given
        Client client = createDefaultClient();
        Buyer buyer = Buyer.builder()
                .client(client)
                .buyerName("John Doe")
                .build();

        // then
        assertEquals(client, buyer.getClient());
        assertEquals("Test Corp", buyer.getClient().getClientName());
    }

    // === @PrePersist / @PreUpdate 라이프사이클 메서드 ===

    @Test
    @DisplayName("onCreate 호출 시 createdAt과 updatedAt이 설정된다.")
    void onCreate_SetsTimestamps() {
        // given
        Buyer buyer = createDefaultBuyer();
        assertNull(buyer.getCreatedAt());
        assertNull(buyer.getUpdatedAt());

        // when
        buyer.onCreate();

        // then
        assertNotNull(buyer.getCreatedAt());
        assertNotNull(buyer.getUpdatedAt());
    }

    @Test
    @DisplayName("onUpdate 호출 시 updatedAt이 갱신된다.")
    void onUpdate_SetsUpdatedAt() {
        // given
        Buyer buyer = createDefaultBuyer();
        buyer.onCreate();

        // when
        buyer.onUpdate();

        // then
        assertNotNull(buyer.getUpdatedAt());
    }
}
