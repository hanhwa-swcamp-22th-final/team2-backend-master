package com.team2.master.unit.entity;

import com.team2.master.command.domain.entity.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    @DisplayName("통화 엔티티 생성 테스트")
    void createCurrency() {
        // given & when
        Currency currency = new Currency("USD", "US Dollar", "$");

        // then
        assertEquals("USD", currency.getCurrencyCode());
        assertEquals("US Dollar", currency.getCurrencyName());
        assertEquals("$", currency.getCurrencySymbol());
    }

    @Test
    @DisplayName("통화 정보 수정 테스트")
    void updateCurrency() {
        // given
        Currency currency = new Currency("USD", "US Dollar", "$");

        // when
        currency.update("USD", "United States Dollar", "US$");

        // then
        assertEquals("USD", currency.getCurrencyCode());
        assertEquals("United States Dollar", currency.getCurrencyName());
        assertEquals("US$", currency.getCurrencySymbol());
    }

    @Test
    @DisplayName("통화 코드 필수값 검증 테스트")
    void currencyCodeRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency(null, "US Dollar", "$"));
    }

    @Test
    @DisplayName("통화명 필수값 검증 테스트")
    void currencyNameRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Currency("USD", null, "$"));
    }
}
