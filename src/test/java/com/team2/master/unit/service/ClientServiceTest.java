package com.team2.master.unit.service;

import com.team2.master.query.dto.ClientResponse;
import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.*;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.ClientQueryMapper;
import com.team2.master.command.domain.repository.*;
import com.team2.master.command.application.service.ClientCommandService;
import com.team2.master.query.service.ClientQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Nested
    @DisplayName("ClientQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private ClientQueryMapper clientQueryMapper;

        @InjectMocks
        private ClientQueryService clientQueryService;

        private ClientResponse clientResponse;

        @BeforeEach
        void setUp() {
            clientResponse = ClientResponse.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .build();
        }

        @Test
        @DisplayName("ID로 거래처를 조회할 수 있다")
        void getClient_success() {
            // given
            given(clientQueryMapper.findById(1)).willReturn(clientResponse);

            // when
            ClientResponse result = clientQueryService.getClient(1);

            // then
            assertThat(result.getClientName()).isEqualTo("Test Corp");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getClient_notFound() {
            // given
            given(clientQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> clientQueryService.getClient(999))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("거래처를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("전체 거래처 목록을 조회할 수 있다")
        void getAllClients() {
            // given
            given(clientQueryMapper.findAll()).willReturn(List.of(clientResponse));

            // when
            List<ClientResponse> result = clientQueryService.getAllClients();

            // then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("상태별 거래처 목록을 조회할 수 있다")
        void getClientsByStatus() {
            // given
            given(clientQueryMapper.findByClientStatus("active")).willReturn(List.of(clientResponse));

            // when
            List<ClientResponse> result = clientQueryService.getClientsByStatus(ClientStatus.ACTIVE);

            // then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("부서 ID로 거래처 목록을 조회할 수 있다 (RBAC)")
        void getClientsByDepartmentId() {
            // given
            given(clientQueryMapper.findByDepartmentId(1)).willReturn(List.of(clientResponse));

            // when
            List<ClientResponse> result = clientQueryService.getClientsByDepartmentId(1);

            // then
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("ClientCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private ClientRepository clientRepository;

        @Mock
        private CountryRepository countryRepository;

        @Mock
        private PortRepository portRepository;

        @Mock
        private PaymentTermRepository paymentTermRepository;

        @Mock
        private CurrencyRepository currencyRepository;

        @InjectMocks
        private ClientCommandService clientCommandService;

        private Client client;

        @BeforeEach
        void setUp() {
            client = Client.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .clientNameKr("테스트 주식회사")
                    .clientCity("Seoul")
                    .clientEmail("test@corp.com")
                    .clientManager("홍길동")
                    .teamId(1)
                    .clientStatus(ClientStatus.ACTIVE)
                    .clientRegDate(LocalDate.of(2025, 1, 1))
                    .build();
        }

        @Test
        @DisplayName("거래처를 생성할 수 있다")
        void createClient_success() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .clientNameKr("테스트 주식회사")
                    .clientCity("Seoul")
                    .clientEmail("test@corp.com")
                    .clientManager("홍길동")
                    .teamId(1)
                    .clientRegDate(LocalDate.of(2025, 1, 1))
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(clientRepository.save(any(Client.class))).willReturn(client);

            // when
            Client result = clientCommandService.createClient(request);

            // then
            assertThat(result.getClientName()).isEqualTo("Test Corp");
            assertThat(result.getClientCode()).isEqualTo("CLI001");
            verify(clientRepository).save(any(Client.class));
        }

        @Test
        @DisplayName("중복 거래처 코드로 생성 시 예외가 발생한다")
        void createClient_duplicateCode() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> clientCommandService.createClient(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 사용 중인 거래처 코드");
        }

        @Test
        @DisplayName("거래처 생성 시 국가를 지정할 수 있다")
        void createClient_withCountry() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .countryId(1)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(countryRepository.findById(1)).willReturn(Optional.of(country));
            given(clientRepository.save(any(Client.class))).willReturn(client);

            // when
            Client result = clientCommandService.createClient(request);

            // then
            assertThat(result).isNotNull();
            verify(countryRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 생성 시 존재하지 않는 국가 ID로 지정하면 예외가 발생한다")
        void createClient_countryNotFound() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .countryId(999)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(countryRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.createClient(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("국가를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 정보를 수정할 수 있다")
        void updateClient_success() {
            // given
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .clientName("Updated Corp")
                    .clientNameKr("수정 주식회사")
                    .clientCity("Busan")
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));

            // when
            Client result = clientCommandService.updateClient(1, request);

            // then
            assertThat(result.getClientName()).isEqualTo("Updated Corp");
            assertThat(result.getClientNameKr()).isEqualTo("수정 주식회사");
            assertThat(result.getClientCity()).isEqualTo("Busan");
        }

        @Test
        @DisplayName("거래처 상태를 변경할 수 있다 (soft delete)")
        void changeStatus_success() {
            // given
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));

            // when
            Client result = clientCommandService.changeStatus(1, ClientStatus.INACTIVE);

            // then
            assertThat(result.getClientStatus()).isEqualTo(ClientStatus.INACTIVE);
        }

        @Test
        @DisplayName("동일 상태로 변경 시 예외가 발생한다")
        void changeStatus_sameStatus() {
            // given
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));

            // when & then
            assertThatThrownBy(() -> clientCommandService.changeStatus(1, ClientStatus.ACTIVE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 ACTIVE 상태입니다");
        }

        @Test
        @DisplayName("거래처 생성 시 항구를 지정할 수 있다")
        void createClient_withPort() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .portId(1)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(portRepository.findById(1)).willReturn(Optional.of(port));
            given(clientRepository.save(any(Client.class))).willReturn(client);

            // when
            Client result = clientCommandService.createClient(request);

            // then
            assertThat(result).isNotNull();
            verify(portRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 생성 시 존재하지 않는 항구 ID로 지정하면 예외가 발생한다")
        void createClient_portNotFound() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .portId(999)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(portRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.createClient(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("항구를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 생성 시 결제조건을 지정할 수 있다")
        void createClient_withPaymentTerm() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("NET30", "Net 30 Days", "30일 이내 결제");
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .paymentTermId(1)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));
            given(clientRepository.save(any(Client.class))).willReturn(client);

            // when
            Client result = clientCommandService.createClient(request);

            // then
            assertThat(result).isNotNull();
            verify(paymentTermRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 생성 시 존재하지 않는 결제조건 ID로 지정하면 예외가 발생한다")
        void createClient_paymentTermNotFound() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .paymentTermId(999)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(paymentTermRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.createClient(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("결제조건을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 생성 시 통화를 지정할 수 있다")
        void createClient_withCurrency() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .currencyId(1)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(currencyRepository.findById(1)).willReturn(Optional.of(currency));
            given(clientRepository.save(any(Client.class))).willReturn(client);

            // when
            Client result = clientCommandService.createClient(request);

            // then
            assertThat(result).isNotNull();
            verify(currencyRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 생성 시 존재하지 않는 통화 ID로 지정하면 예외가 발생한다")
        void createClient_currencyNotFound() {
            // given
            CreateClientRequest request = CreateClientRequest.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .currencyId(999)
                    .build();
            given(clientRepository.existsByClientCode("CLI001")).willReturn(false);
            given(currencyRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.createClient(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("통화를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 수정 시 국가를 변경할 수 있다")
        void updateClient_withCountry() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .clientName("Updated Corp")
                    .countryId(1)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(countryRepository.findById(1)).willReturn(Optional.of(country));

            // when
            Client result = clientCommandService.updateClient(1, request);

            // then
            assertThat(result.getClientName()).isEqualTo("Updated Corp");
            verify(countryRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 수정 시 존재하지 않는 국가 ID로 지정하면 예외가 발생한다")
        void updateClient_countryNotFound() {
            // given
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .countryId(999)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(countryRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.updateClient(1, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("국가를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 수정 시 항구를 변경할 수 있다")
        void updateClient_withPort() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .portId(1)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(portRepository.findById(1)).willReturn(Optional.of(port));

            // when
            Client result = clientCommandService.updateClient(1, request);

            // then
            assertThat(result).isNotNull();
            verify(portRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 수정 시 존재하지 않는 항구 ID로 지정하면 예외가 발생한다")
        void updateClient_portNotFound() {
            // given
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .portId(999)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(portRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.updateClient(1, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("항구를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 수정 시 결제조건을 변경할 수 있다")
        void updateClient_withPaymentTerm() {
            // given
            PaymentTerm paymentTerm = new PaymentTerm("NET30", "Net 30 Days", "30일 이내 결제");
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .paymentTermId(1)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(paymentTermRepository.findById(1)).willReturn(Optional.of(paymentTerm));

            // when
            Client result = clientCommandService.updateClient(1, request);

            // then
            assertThat(result).isNotNull();
            verify(paymentTermRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 수정 시 존재하지 않는 결제조건 ID로 지정하면 예외가 발생한다")
        void updateClient_paymentTermNotFound() {
            // given
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .paymentTermId(999)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(paymentTermRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.updateClient(1, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("결제조건을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("거래처 수정 시 통화를 변경할 수 있다")
        void updateClient_withCurrency() {
            // given
            Currency currency = new Currency("USD", "US Dollar", "$");
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .currencyId(1)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(currencyRepository.findById(1)).willReturn(Optional.of(currency));

            // when
            Client result = clientCommandService.updateClient(1, request);

            // then
            assertThat(result).isNotNull();
            verify(currencyRepository).findById(1);
        }

        @Test
        @DisplayName("거래처 수정 시 존재하지 않는 통화 ID로 지정하면 예외가 발생한다")
        void updateClient_currencyNotFound() {
            // given
            UpdateClientRequest request = UpdateClientRequest.builder()
                    .currencyId(999)
                    .build();
            given(clientRepository.findByIdWithRelations(1)).willReturn(Optional.of(client));
            given(currencyRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clientCommandService.updateClient(1, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("통화를 찾을 수 없습니다");
        }
    }
}
