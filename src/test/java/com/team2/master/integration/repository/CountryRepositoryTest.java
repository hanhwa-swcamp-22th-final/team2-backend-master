package com.team2.master.integration.repository;

import com.team2.master.entity.Country;
import com.team2.master.command.repository.CountryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("국가 코드로 조회 테스트")
    void findByCountryCode() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        countryRepository.save(country);

        // when
        Optional<Country> found = countryRepository.findByCountryCode("KR");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCountryName()).isEqualTo("South Korea");
    }

    @Test
    @DisplayName("존재하지 않는 국가 코드 조회 테스트")
    void findByCountryCode_notFound() {
        // when
        Optional<Country> found = countryRepository.findByCountryCode("XX");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("국가 코드 존재 여부 확인 테스트")
    void existsByCountryCode() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        countryRepository.save(country);

        // when & then
        assertThat(countryRepository.existsByCountryCode("KR")).isTrue();
        assertThat(countryRepository.existsByCountryCode("XX")).isFalse();
    }
}
