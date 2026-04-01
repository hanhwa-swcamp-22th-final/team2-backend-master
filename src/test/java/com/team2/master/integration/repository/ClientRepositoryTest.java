package com.team2.master.integration.repository;

import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.domain.repository.ClientRepository;
import com.team2.master.command.domain.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CountryRepository countryRepository;

    private Client savedClient;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .clientCity("Seoul")
                .clientTel("02-1234-5678")
                .clientEmail("test@corp.com")
                .clientManager("홍길동")
                .departmentId(1)
                .clientStatus(ClientStatus.ACTIVE)
                .clientRegDate(LocalDate.of(2025, 1, 1))
                .build();

        Country country = countryRepository.save(new Country("KR", "South Korea", "대한민국"));
        client.assignCountry(country);
        savedClient = clientRepository.save(client);
    }

    @Test
    @DisplayName("거래처 코드로 조회할 수 있다")
    void findByClientCode() {
        // when
        Optional<Client> result = clientRepository.findByClientCode("CLI001");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo("Test Corp");
    }

    @Test
    @DisplayName("존재하지 않는 거래처 코드로 조회하면 빈 Optional을 반환한다")
    void findByClientCode_notFound() {
        // when
        Optional<Client> result = clientRepository.findByClientCode("NOTEXIST");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("거래처 코드 존재 여부를 확인할 수 있다")
    void existsByClientCode() {
        // when & then
        assertThat(clientRepository.existsByClientCode("CLI001")).isTrue();
        assertThat(clientRepository.existsByClientCode("NOTEXIST")).isFalse();
    }

    @Test
    @DisplayName("상태별로 거래처를 조회할 수 있다")
    void findByClientStatus() {
        // given
        Client inactiveClient = Client.builder()
                .clientCode("CLI002")
                .clientName("Inactive Corp")
                .clientStatus(ClientStatus.INACTIVE)
                .build();
        clientRepository.save(inactiveClient);

        // when
        List<Client> activeClients = clientRepository.findByClientStatus(ClientStatus.ACTIVE);
        List<Client> inactiveClients = clientRepository.findByClientStatus(ClientStatus.INACTIVE);

        // then
        assertThat(activeClients).hasSize(1);
        assertThat(inactiveClients).hasSize(1);
    }

    @Test
    @DisplayName("부서 ID로 거래처 목록을 조회할 수 있다")
    void findByDepartmentId() {
        // given
        Client client2 = Client.builder()
                .clientCode("CLI003")
                .clientName("Same Dept Corp")
                .departmentId(1)
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        clientRepository.save(client2);

        Client client3 = Client.builder()
                .clientCode("CLI004")
                .clientName("Other Dept Corp")
                .departmentId(2)
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        clientRepository.save(client3);

        // when
        List<Client> dept1Clients = clientRepository.findByDepartmentId(1);
        List<Client> dept2Clients = clientRepository.findByDepartmentId(2);

        // then
        assertThat(dept1Clients).hasSize(2);
        assertThat(dept2Clients).hasSize(1);
    }
}
