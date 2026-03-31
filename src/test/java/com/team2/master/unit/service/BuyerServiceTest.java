package com.team2.master.unit.service;

import com.team2.master.dto.BuyerResponse;
import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.BuyerQueryMapper;
import com.team2.master.command.repository.BuyerRepository;
import com.team2.master.command.repository.ClientRepository;
import com.team2.master.command.service.BuyerCommandService;
import com.team2.master.query.service.BuyerQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @Nested
    @DisplayName("BuyerQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private BuyerQueryMapper buyerQueryMapper;

        @InjectMocks
        private BuyerQueryService buyerQueryService;

        private BuyerResponse buyerResponse;

        @BeforeEach
        void setUp() {
            buyerResponse = BuyerResponse.builder()
                    .clientName("Test Corp")
                    .buyerName("John Doe")
                    .buyerPosition("Manager")
                    .buyerEmail("john@test.com")
                    .buyerTel("010-1234-5678")
                    .build();
        }

        @Test
        @DisplayName("ID로 바이어를 조회할 수 있다")
        void getBuyer_success() {
            // given
            given(buyerQueryMapper.findById(1)).willReturn(buyerResponse);

            // when
            BuyerResponse result = buyerQueryService.getBuyer(1);

            // then
            assertThat(result.getBuyerName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getBuyer_notFound() {
            // given
            given(buyerQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> buyerQueryService.getBuyer(999))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("바이어를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("전체 바이어 목록을 조회할 수 있다")
        void getAllBuyers() {
            // given
            given(buyerQueryMapper.findAll()).willReturn(List.of(buyerResponse));

            // when
            List<BuyerResponse> result = buyerQueryService.getAllBuyers();

            // then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("거래처 ID로 바이어 목록을 조회할 수 있다")
        void getBuyersByClientId() {
            // given
            given(buyerQueryMapper.findByClientId(1)).willReturn(List.of(buyerResponse));

            // when
            List<BuyerResponse> result = buyerQueryService.getBuyersByClientId(1);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getBuyerName()).isEqualTo("John Doe");
        }
    }

    @Nested
    @DisplayName("BuyerCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private BuyerRepository buyerRepository;

        @Mock
        private ClientRepository clientRepository;

        @InjectMocks
        private BuyerCommandService buyerCommandService;

        private Client client;
        private Buyer buyer;

        @BeforeEach
        void setUp() {
            client = Client.builder()
                    .clientCode("CLI001")
                    .clientName("Test Corp")
                    .clientStatus(ClientStatus.ACTIVE)
                    .build();

            buyer = Buyer.builder()
                    .client(client)
                    .buyerName("John Doe")
                    .buyerPosition("Manager")
                    .buyerEmail("john@test.com")
                    .buyerTel("010-1234-5678")
                    .build();
        }

        @Test
        @DisplayName("바이어를 생성할 수 있다")
        void createBuyer_success() {
            // given
            CreateBuyerRequest request = CreateBuyerRequest.builder()
                    .clientId(1)
                    .buyerName("John Doe")
                    .buyerPosition("Manager")
                    .buyerEmail("john@test.com")
                    .buyerTel("010-1234-5678")
                    .build();
            given(clientRepository.findById(1)).willReturn(Optional.of(client));
            given(buyerRepository.save(any(Buyer.class))).willReturn(buyer);

            // when
            Buyer result = buyerCommandService.createBuyer(request);

            // then
            assertThat(result.getBuyerName()).isEqualTo("John Doe");
            assertThat(result.getClient()).isEqualTo(client);
            verify(buyerRepository).save(any(Buyer.class));
        }

        @Test
        @DisplayName("존재하지 않는 거래처로 바이어 생성 시 예외가 발생한다")
        void createBuyer_clientNotFound() {
            // given
            CreateBuyerRequest request = CreateBuyerRequest.builder()
                    .clientId(999)
                    .buyerName("John Doe")
                    .build();
            given(clientRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> buyerCommandService.createBuyer(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("거래처를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("바이어 정보를 수정할 수 있다")
        void updateBuyer_success() {
            // given
            UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                    .buyerName("Jane Smith")
                    .buyerPosition("Director")
                    .buyerEmail("jane@test.com")
                    .buyerTel("010-9876-5432")
                    .build();
            given(buyerRepository.findByIdWithClient(1)).willReturn(Optional.of(buyer));

            // when
            Buyer result = buyerCommandService.updateBuyer(1, request);

            // then
            assertThat(result.getBuyerName()).isEqualTo("Jane Smith");
            assertThat(result.getBuyerPosition()).isEqualTo("Director");
        }

        @Test
        @DisplayName("바이어를 삭제할 수 있다 (hard delete)")
        void deleteBuyer_success() {
            // given
            given(buyerRepository.findByIdWithClient(1)).willReturn(Optional.of(buyer));

            // when
            buyerCommandService.deleteBuyer(1);

            // then
            verify(buyerRepository).delete(buyer);
        }

        @Test
        @DisplayName("존재하지 않는 바이어 삭제 시 예외가 발생한다")
        void deleteBuyer_notFound() {
            // given
            given(buyerRepository.findByIdWithClient(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> buyerCommandService.deleteBuyer(999))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("바이어를 찾을 수 없습니다");
        }
    }
}
