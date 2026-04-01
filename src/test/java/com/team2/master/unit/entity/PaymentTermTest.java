package com.team2.master.unit.entity;

import com.team2.master.command.domain.entity.PaymentTerm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTermTest {

    @Test
    @DisplayName("결제조건 엔티티 생성 테스트")
    void createPaymentTerm() {
        // given & when
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");

        // then
        assertEquals("TT", paymentTerm.getPaymentTermCode());
        assertEquals("Telegraphic Transfer", paymentTerm.getPaymentTermName());
        assertEquals("전신환 송금", paymentTerm.getPaymentTermDescription());
    }

    @Test
    @DisplayName("결제조건 정보 수정 테스트")
    void updatePaymentTerm() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");

        // when
        paymentTerm.update("T/T", "T/T Transfer", "T/T 송금");

        // then
        assertEquals("T/T", paymentTerm.getPaymentTermCode());
        assertEquals("T/T Transfer", paymentTerm.getPaymentTermName());
        assertEquals("T/T 송금", paymentTerm.getPaymentTermDescription());
    }

    @Test
    @DisplayName("결제조건 코드 필수값 검증 테스트")
    void paymentTermCodeRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new PaymentTerm(null, "Telegraphic Transfer", "전신환 송금"));
    }

    @Test
    @DisplayName("결제조건명 필수값 검증 테스트")
    void paymentTermNameRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new PaymentTerm("TT", null, "전신환 송금"));
    }
}
