package com.team2.master.integration.repository;

import com.team2.master.entity.Currency;
import com.team2.master.command.repository.CurrencyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    @DisplayName("통화 코드로 조회 테스트")
    void findByCurrencyCode() {
        // given
        Currency currency = new Currency("USD", "US Dollar", "$");
        currencyRepository.save(currency);

        // when
        Optional<Currency> found = currencyRepository.findByCurrencyCode("USD");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCurrencyName()).isEqualTo("US Dollar");
    }

    @Test
    @DisplayName("존재하지 않는 통화 코드 조회 테스트")
    void findByCurrencyCode_notFound() {
        // when
        Optional<Currency> found = currencyRepository.findByCurrencyCode("XXX");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("통화 코드 존재 여부 확인 테스트")
    void existsByCurrencyCode() {
        // given
        Currency currency = new Currency("KRW", "Korean Won", "₩");
        currencyRepository.save(currency);

        // when & then
        assertThat(currencyRepository.existsByCurrencyCode("KRW")).isTrue();
        assertThat(currencyRepository.existsByCurrencyCode("XXX")).isFalse();
    }
}
