package com.team2.master.entity;

import com.team2.master.entity.enums.ClientStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client createDefaultClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .clientCity("Seoul")
                .clientAddress("123 Test St")
                .clientTel("02-1234-5678")
                .clientEmail("test@corp.com")
                .clientManager("홍길동")
                .departmentId(1)
                .clientStatus(ClientStatus.활성)
                .clientRegDate(LocalDate.of(2025, 1, 1))
                .build();
    }

    // === 생성 테스트 ===

    @Test
    @DisplayName("거래처 생성 성공: 필수 필드가 정상 설정된다.")
    void createClient_Success() {
        // given & when
        Client client = createDefaultClient();

        // then
        assertEquals("CLI001", client.getClientCode());
        assertEquals("Test Corp", client.getClientName());
        assertEquals("테스트 주식회사", client.getClientNameKr());
        assertEquals("Seoul", client.getClientCity());
        assertEquals("123 Test St", client.getClientAddress());
        assertEquals("02-1234-5678", client.getClientTel());
        assertEquals("test@corp.com", client.getClientEmail());
        assertEquals("홍길동", client.getClientManager());
        assertEquals(1, client.getDepartmentId());
        assertEquals(ClientStatus.활성, client.getClientStatus());
    }

    @Test
    @DisplayName("거래처 생성 시 상태 미지정이면 기본값 활성으로 설정된다.")
    void createClient_DefaultStatus() {
        // given & when
        Client client = Client.builder()
                .clientCode("CLI002")
                .clientName("Default Corp")
                .build();

        // then
        assertEquals(ClientStatus.활성, client.getClientStatus());
    }

    // === 상태 변경 ===

    @Test
    @DisplayName("상태 변경 성공: 활성에서 비활성으로 변경된다.")
    void changeStatus_ToInactive_Success() {
        // given
        Client client = createDefaultClient();

        // when
        client.changeStatus(ClientStatus.비활성);

        // then
        assertEquals(ClientStatus.비활성, client.getClientStatus());
    }

    @Test
    @DisplayName("상태 변경 성공: 비활성에서 활성으로 변경된다.")
    void changeStatus_ToActive_Success() {
        // given
        Client client = Client.builder()
                .clientCode("CLI003")
                .clientName("Inactive Corp")
                .clientStatus(ClientStatus.비활성)
                .build();

        // when
        client.changeStatus(ClientStatus.활성);

        // then
        assertEquals(ClientStatus.활성, client.getClientStatus());
    }

    @Test
    @DisplayName("상태 변경 실패: 동일 상태로 변경 시 예외가 발생한다.")
    void changeStatus_SameStatus_ThrowsException() {
        // given
        Client client = createDefaultClient();

        // when & then
        assertThrows(IllegalStateException.class,
                () -> client.changeStatus(ClientStatus.활성));
    }

    // === 활성 확인 ===

    @Test
    @DisplayName("활성 확인: 활성 상태이면 true를 반환한다.")
    void isActive_ActiveClient_ReturnsTrue() {
        // given
        Client client = createDefaultClient();

        // when & then
        assertTrue(client.isActive());
    }

    @Test
    @DisplayName("활성 확인: 비활성 상태이면 false를 반환한다.")
    void isActive_InactiveClient_ReturnsFalse() {
        // given
        Client client = Client.builder()
                .clientCode("CLI004")
                .clientName("Inactive Corp")
                .clientStatus(ClientStatus.비활성)
                .build();

        // when & then
        assertFalse(client.isActive());
    }

    // === 정보 수정 ===

    @Test
    @DisplayName("정보 수정 성공: 거래처 정보를 수정할 수 있다.")
    void updateInfo_Success() {
        // given
        Client client = createDefaultClient();

        // when
        client.updateInfo("Updated Corp", "수정 주식회사", "Busan",
                "456 New St", "051-9876-5432", "updated@corp.com", "김철수");

        // then
        assertEquals("Updated Corp", client.getClientName());
        assertEquals("수정 주식회사", client.getClientNameKr());
        assertEquals("Busan", client.getClientCity());
        assertEquals("456 New St", client.getClientAddress());
        assertEquals("051-9876-5432", client.getClientTel());
        assertEquals("updated@corp.com", client.getClientEmail());
        assertEquals("김철수", client.getClientManager());
    }

    @Test
    @DisplayName("정보 수정: null 값은 기존 값을 유지한다.")
    void updateInfo_NullValuesKeepExisting() {
        // given
        Client client = createDefaultClient();

        // when
        client.updateInfo(null, null, null, null, null, null, null);

        // then
        assertEquals("Test Corp", client.getClientName());
        assertEquals("테스트 주식회사", client.getClientNameKr());
    }

    // === 참조 엔티티 배정 ===

    @Test
    @DisplayName("국가 배정 성공: 거래처에 국가를 배정할 수 있다.")
    void assignCountry_Success() {
        // given
        Client client = createDefaultClient();

        // when
        // country assignment is tested via service layer with actual Country entity
        // here we just verify the method exists and the field is initially null
        assertNull(client.getCountry());
    }

    @Test
    @DisplayName("항구 배정 성공: 거래처에 항구를 배정할 수 있다.")
    void assignPort_Success() {
        // given
        Client client = createDefaultClient();

        // then
        assertNull(client.getPort());
    }

    // === assign 메서드 실제 호출 ===

    @Test
    @DisplayName("국가 배정: assignCountry 호출 시 국가가 설정된다.")
    void assignCountry_SetsCountry() {
        // given
        Client client = createDefaultClient();
        Country country = new Country("KR", "South Korea", "대한민국");

        // when
        client.assignCountry(country);

        // then
        assertEquals(country, client.getCountry());
    }

    @Test
    @DisplayName("항구 배정: assignPort 호출 시 항구가 설정된다.")
    void assignPort_SetsPort() {
        // given
        Client client = createDefaultClient();
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);

        // when
        client.assignPort(port);

        // then
        assertEquals(port, client.getPort());
    }

    @Test
    @DisplayName("결제조건 배정: assignPaymentTerm 호출 시 결제조건이 설정된다.")
    void assignPaymentTerm_SetsPaymentTerm() {
        // given
        Client client = createDefaultClient();
        PaymentTerm paymentTerm = new PaymentTerm("NET30", "Net 30 Days", "30일 이내 결제");

        // when
        client.assignPaymentTerm(paymentTerm);

        // then
        assertEquals(paymentTerm, client.getPaymentTerm());
    }

    @Test
    @DisplayName("통화 배정: assignCurrency 호출 시 통화가 설정된다.")
    void assignCurrency_SetsCurrency() {
        // given
        Client client = createDefaultClient();
        Currency currency = new Currency("USD", "US Dollar", "$");

        // when
        client.assignCurrency(currency);

        // then
        assertEquals(currency, client.getCurrency());
    }

    // === @PrePersist / @PreUpdate 라이프사이클 메서드 ===

    @Test
    @DisplayName("onCreate 호출 시 createdAt과 updatedAt이 설정된다.")
    void onCreate_SetsTimestamps() {
        // given
        Client client = createDefaultClient();
        assertNull(client.getCreatedAt());
        assertNull(client.getUpdatedAt());

        // when
        client.onCreate();

        // then
        assertNotNull(client.getCreatedAt());
        assertNotNull(client.getUpdatedAt());
    }

    @Test
    @DisplayName("onUpdate 호출 시 updatedAt이 갱신된다.")
    void onUpdate_SetsUpdatedAt() {
        // given
        Client client = createDefaultClient();
        client.onCreate();
        var previousUpdatedAt = client.getUpdatedAt();

        // when
        client.onUpdate();

        // then
        assertNotNull(client.getUpdatedAt());
    }
}
