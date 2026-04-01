package com.team2.master.unit.service;

import com.team2.master.command.application.dto.CreatePaymentTermRequest;
import com.team2.master.command.application.dto.UpdatePaymentTermRequest;
import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.PaymentTermQueryMapper;
import com.team2.master.command.domain.repository.PaymentTermRepository;
import com.team2.master.command.application.service.PaymentTermCommandService;
import com.team2.master.query.service.PaymentTermQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentTermServiceTest {

    @Nested
    @DisplayName("PaymentTermQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private PaymentTermQueryMapper paymentTermQueryMapper;

        @InjectMocks
        private PaymentTermQueryService paymentTermQueryService;

        @Test
        @DisplayName("전체 결제조건 목록 조회 테스트")
        void getAll() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            given(paymentTermQueryMapper.findAll()).willReturn(List.of(paymentTerm));

            // when
            List<PaymentTerm> result = paymentTermQueryService.getAll();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPaymentTermCode()).isEqualTo("TT");
        }

        @Test
        @DisplayName("결제조건 ID로 조회 테스트")
        void getById() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            given(paymentTermQueryMapper.findById(1)).willReturn(paymentTerm);

            // when
            PaymentTerm result = paymentTermQueryService.getById(1);

            // then
            assertThat(result.getPaymentTermCode()).isEqualTo("TT");
        }

        @Test
        @DisplayName("존재하지 않는 결제조건 ID 조회 시 예외 발생 테스트")
        void getById_notFound() {
            // given
            given(paymentTermQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> paymentTermQueryService.getById(999))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("PaymentTermCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private PaymentTermRepository paymentTermRepository;

        @InjectMocks
        private PaymentTermCommandService paymentTermCommandService;

        @Test
        @DisplayName("결제조건 생성 테스트")
        void create() {
            // given
            CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            given(paymentTermRepository.existsByPaymentTermCode("TT")).willReturn(false);
            given(paymentTermRepository.save(any(PaymentTerm.class))).willReturn(paymentTerm);

            // when
            PaymentTerm result = paymentTermCommandService.create(request);

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
            assertThatThrownBy(() -> paymentTermCommandService.create(request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("결제조건 수정 테스트")
        void update() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            ReflectionTestUtils.setField(paymentTerm, "paymentTermId", 1);
            UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
            given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));
            given(paymentTermRepository.findByPaymentTermCode("T/T")).willReturn(Optional.empty());

            // when
            PaymentTerm result = paymentTermCommandService.update(1, request);

            // then
            assertThat(result.getPaymentTermCode()).isEqualTo("T/T");
            assertThat(result.getPaymentTermName()).isEqualTo("T/T Transfer");
        }

        @Test
        @DisplayName("결제조건 수정 시 중복 코드 예외 발생 테스트")
        void update_duplicateCode() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            ReflectionTestUtils.setField(paymentTerm, "paymentTermId", 1);
            PaymentTerm existing = new PaymentTerm("LC", "Letter of Credit", "신용장");
            ReflectionTestUtils.setField(existing, "paymentTermId", 2);
            UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("LC", "LC Updated", "신용장수정");
            given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));
            given(paymentTermRepository.findByPaymentTermCode("LC")).willReturn(Optional.of(existing));

            // when & then
            assertThatThrownBy(() -> paymentTermCommandService.update(1, request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("결제조건 삭제 테스트")
        void delete() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
            given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));

            // when
            paymentTermCommandService.delete(1);

            // then
            then(paymentTermRepository).should().delete(paymentTerm);
        }
    }
}
