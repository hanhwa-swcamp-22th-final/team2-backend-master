package com.team2.master.unit.service;

import com.team2.master.dto.CreatePaymentTermRequest;
import com.team2.master.dto.UpdatePaymentTermRequest;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.repository.PaymentTermRepository;
import com.team2.master.service.PaymentTermService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentTermServiceTest {

    @Mock
    private PaymentTermRepository paymentTermRepository;

    @InjectMocks
    private PaymentTermService paymentTermService;

    @Test
    @DisplayName("전체 결제조건 목록 조회 테스트")
    void getAll() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermRepository.findAll()).willReturn(List.of(paymentTerm));

        // when
        List<PaymentTerm> result = paymentTermService.getAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPaymentTermCode()).isEqualTo("TT");
    }

    @Test
    @DisplayName("결제조건 ID로 조회 테스트")
    void getById() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));

        // when
        PaymentTerm result = paymentTermService.getById(1);

        // then
        assertThat(result.getPaymentTermCode()).isEqualTo("TT");
    }

    @Test
    @DisplayName("존재하지 않는 결제조건 ID 조회 시 예외 발생 테스트")
    void getById_notFound() {
        // given
        given(paymentTermRepository.findById(999)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentTermService.getById(999))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("결제조건 생성 테스트")
    void create() {
        // given
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermRepository.existsByPaymentTermCode("TT")).willReturn(false);
        given(paymentTermRepository.save(any(PaymentTerm.class))).willReturn(paymentTerm);

        // when
        PaymentTerm result = paymentTermService.create(request);

        // then
        assertThat(result.getPaymentTermCode()).isEqualTo("TT");
    }

    @Test
    @DisplayName("중복 결제조건 코드로 생성 시 예외 발생 테스트")
    void create_duplicateCode() {
        // given
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermRepository.existsByPaymentTermCode("TT")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> paymentTermService.create(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("결제조건 수정 테스트")
    void update() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
        given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));

        // when
        PaymentTerm result = paymentTermService.update(1, request);

        // then
        assertThat(result.getPaymentTermCode()).isEqualTo("T/T");
        assertThat(result.getPaymentTermName()).isEqualTo("T/T Transfer");
    }

    @Test
    @DisplayName("결제조건 삭제 테스트")
    void delete() {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));

        // when
        paymentTermService.delete(1);

        // then
        then(paymentTermRepository).should().delete(paymentTerm);
    }
}
