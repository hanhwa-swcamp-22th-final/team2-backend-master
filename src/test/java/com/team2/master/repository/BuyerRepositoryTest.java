package com.team2.master.repository;

import com.team2.master.entity.Buyer;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BuyerRepositoryTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client savedClient;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.활성)
                .build();
        savedClient = clientRepository.save(client);

        Buyer buyer1 = Buyer.builder()
                .client(savedClient)
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        buyerRepository.save(buyer1);

        Buyer buyer2 = Buyer.builder()
                .client(savedClient)
                .buyerName("Jane Smith")
                .buyerPosition("Director")
                .buyerEmail("jane@test.com")
                .buyerTel("010-9876-5432")
                .build();
        buyerRepository.save(buyer2);
    }

    @Test
    @DisplayName("거래처 ID로 바이어 목록을 조회할 수 있다")
    void findByClientId() {
        // when
        List<Buyer> buyers = buyerRepository.findByClientId(savedClient.getId());

        // then
        assertThat(buyers).hasSize(2);
        assertThat(buyers).extracting(Buyer::getBuyerName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("존재하지 않는 거래처 ID로 조회하면 빈 목록을 반환한다")
    void findByClientId_notFound() {
        // when
        List<Buyer> buyers = buyerRepository.findByClientId(999);

        // then
        assertThat(buyers).isEmpty();
    }

    @Test
    @DisplayName("바이어를 삭제할 수 있다 (hard delete)")
    void deleteBuyer() {
        // given
        List<Buyer> buyers = buyerRepository.findByClientId(savedClient.getId());
        assertThat(buyers).hasSize(2);

        // when
        buyerRepository.delete(buyers.get(0));

        // then
        List<Buyer> remaining = buyerRepository.findByClientId(savedClient.getId());
        assertThat(remaining).hasSize(1);
    }
}
