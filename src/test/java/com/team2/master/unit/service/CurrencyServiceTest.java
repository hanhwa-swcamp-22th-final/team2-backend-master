package com.team2.master.unit.service;

import com.team2.master.dto.CreateCurrencyRequest;
import com.team2.master.dto.UpdateCurrencyRequest;
import com.team2.master.entity.Currency;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.CurrencyQueryMapper;
import com.team2.master.command.repository.CurrencyRepository;
import com.team2.master.command.service.CurrencyCommandService;
import com.team2.master.query.service.CurrencyQueryService;
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
class CurrencyServiceTest {

    @Nested
    @DisplayName("CurrencyQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private CurrencyQueryMapper currencyQueryMapper;

        @InjectMocks
        private CurrencyQueryService currencyQueryService;

        @Test
        @DisplayName("전체 통화 목록 조회 테스트")
        void getAll() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            given(currencyQueryMapper.findAll()).willReturn(List.of(currency));

            // when
            List<Currency> result = currencyQueryService.getAll();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCurrencyCode()).isEqualTo("USD");
        }

        @Test
        @DisplayName("통화 ID로 조회 테스트")
        void getById() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            given(currencyQueryMapper.findById(1)).willReturn(currency);

            // when
            Currency result = currencyQueryService.getById(1);

            // then
            assertThat(result.getCurrencyCode()).isEqualTo("USD");
        }

        @Test
        @DisplayName("존재하지 않는 통화 ID 조회 시 예외 발생 테스트")
        void getById_notFound() {
            // given
            given(currencyQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> currencyQueryService.getById(999))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("CurrencyCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private CurrencyRepository currencyRepository;

        @InjectMocks
        private CurrencyCommandService currencyCommandService;

        @Test
        @DisplayName("통화 생성 테스트")
        void create() {
            // given
            CreateCurrencyRequest request = new CreateCurrencyRequest("USD", "US Dollar", "$");
            Currency currency = new Currency("USD", "US Dollar", "$");
            given(currencyRepository.existsByCurrencyCode("USD")).willReturn(false);
            given(currencyRepository.save(any(Currency.class))).willReturn(currency);

            // when
            Currency result = currencyCommandService.create(request);

            // then
            assertThat(result.getCurrencyCode()).isEqualTo("USD");
        }

        @Test
        @DisplayName("중복 통화 코드로 생성 시 예외 발생 테스트")
        void create_duplicateCode() {
            // given
            CreateCurrencyRequest request = new CreateCurrencyRequest("USD", "US Dollar", "$");
            given(currencyRepository.existsByCurrencyCode("USD")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> currencyCommandService.create(request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("통화 수정 테스트")
        void update() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            ReflectionTestUtils.setField(currency, "currencyId", 1);
            UpdateCurrencyRequest request = new UpdateCurrencyRequest("USD", "United States Dollar", "US$");
            given(currencyRepository.findById(1)).willReturn(Optional.of(currency));
            given(currencyRepository.findByCurrencyCode("USD")).willReturn(Optional.of(currency));

            // when
            Currency result = currencyCommandService.update(1, request);

            // then
            assertThat(result.getCurrencyName()).isEqualTo("United States Dollar");
        }

        @Test
        @DisplayName("통화 수정 시 중복 코드 예외 발생 테스트")
        void update_duplicateCode() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            ReflectionTestUtils.setField(currency, "currencyId", 1);
            Currency existing = new Currency("EUR", "Euro", "E");
            ReflectionTestUtils.setField(existing, "currencyId", 2);
            UpdateCurrencyRequest request = new UpdateCurrencyRequest("EUR", "Euro Updated", "EU");
            given(currencyRepository.findById(1)).willReturn(Optional.of(currency));
            given(currencyRepository.findByCurrencyCode("EUR")).willReturn(Optional.of(existing));

            // when & then
            assertThatThrownBy(() -> currencyCommandService.update(1, request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("통화 삭제 테스트")
        void delete() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            given(currencyRepository.findById(1)).willReturn(Optional.of(currency));

            // when
            currencyCommandService.delete(1);

            // then
            then(currencyRepository).should().delete(currency);
        }
    }
}
