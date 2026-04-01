package com.team2.master.integration.repository;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.command.domain.repository.PaymentTermRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentTermRepositoryTest {

    @Autowired
    private PaymentTermRepository paymentTermRepository;

    @Test
    @DisplayName("결제조건 코드로 조회 테스트")
    void findByPaymentTermCode() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        paymentTermRepository.save(paymentTerm);

        // when
        Optional<PaymentTerm> found = paymentTermRepository.findByPaymentTermCode("TT");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPaymentTermName()).isEqualTo("Telegraphic Transfer");
    }

    @Test
    @DisplayName("존재하지 않는 결제조건 코드 조회 테스트")
    void findByPaymentTermCode_notFound() {
        // when
        Optional<PaymentTerm> found = paymentTermRepository.findByPaymentTermCode("XXX");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("결제조건 코드 존재 여부 확인 테스트")
    void existsByPaymentTermCode() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("LC", "Letter of Credit", "신용장");
        paymentTermRepository.save(paymentTerm);

        // when & then
        assertThat(paymentTermRepository.existsByPaymentTermCode("LC")).isTrue();
        assertThat(paymentTermRepository.existsByPaymentTermCode("XXX")).isFalse();
    }
}
