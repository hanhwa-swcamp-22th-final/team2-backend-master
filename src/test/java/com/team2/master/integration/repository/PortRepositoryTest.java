package com.team2.master.integration.repository;

import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.repository.CountryRepository;
import com.team2.master.repository.PortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PortRepositoryTest {

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("항구 코드로 조회 테스트")
    void findByPortCode() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        countryRepository.save(country);
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        portRepository.save(port);

        // when
        Optional<Port> found = portRepository.findByPortCode("KRPUS");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPortName()).isEqualTo("Busan Port");
    }

    @Test
    @DisplayName("존재하지 않는 항구 코드 조회 테스트")
    void findByPortCode_notFound() {
        // when
        Optional<Port> found = portRepository.findByPortCode("XXXXX");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("국가 ID로 항구 목록 조회 테스트")
    void findByCountryId() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        countryRepository.save(country);

        Port port1 = new Port("KRPUS", "Busan Port", "Busan", country);
        Port port2 = new Port("KRICN", "Incheon Port", "Incheon", country);
        portRepository.save(port1);
        portRepository.save(port2);

        entityManager.flush();
        entityManager.clear();

        // when
        List<Port> ports = portRepository.findByCountryId(country.getId());

        // then
        assertThat(ports).hasSize(2);
    }

    @Test
    @DisplayName("항구 코드 존재 여부 확인 테스트")
    void existsByPortCode() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        countryRepository.save(country);
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        portRepository.save(port);

        // when & then
        assertThat(portRepository.existsByPortCode("KRPUS")).isTrue();
        assertThat(portRepository.existsByPortCode("XXXXX")).isFalse();
    }
}
