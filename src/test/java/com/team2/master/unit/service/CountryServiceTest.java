package com.team2.master.unit.service;

import com.team2.master.dto.CreateCountryRequest;
import com.team2.master.dto.UpdateCountryRequest;
import com.team2.master.entity.Country;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.CountryRepository;
import com.team2.master.service.CountryService;
import org.junit.jupiter.api.DisplayName;
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
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("전체 국가 목록 조회 테스트")
    void getAll() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryRepository.findAll()).willReturn(List.of(country));

        // when
        List<Country> result = countryService.getAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCountryCode()).isEqualTo("KR");
    }

    @Test
    @DisplayName("국가 ID로 조회 테스트")
    void getById() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryRepository.findById(1)).willReturn(Optional.of(country));

        // when
        Country result = countryService.getById(1);

        // then
        assertThat(result.getCountryCode()).isEqualTo("KR");
    }

    @Test
    @DisplayName("존재하지 않는 국가 ID 조회 시 예외 발생 테스트")
    void getById_notFound() {
        // given
        given(countryRepository.findById(999)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> countryService.getById(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("국가 생성 테스트")
    void create() {
        // given
        CreateCountryRequest request = new CreateCountryRequest("KR", "South Korea", "대한민국");
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryRepository.existsByCountryCode("KR")).willReturn(false);
        given(countryRepository.save(any(Country.class))).willReturn(country);

        // when
        Country result = countryService.create(request);

        // then
        assertThat(result.getCountryCode()).isEqualTo("KR");
    }

    @Test
    @DisplayName("중복 국가 코드로 생성 시 예외 발생 테스트")
    void create_duplicateCode() {
        // given
        CreateCountryRequest request = new CreateCountryRequest("KR", "South Korea", "대한민국");
        given(countryRepository.existsByCountryCode("KR")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> countryService.create(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("국가 수정 테스트")
    void update() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        ReflectionTestUtils.setField(country, "countryId", 1);
        UpdateCountryRequest request = new UpdateCountryRequest("KOR", "Korea", "한국");
        given(countryRepository.findById(1)).willReturn(Optional.of(country));
        given(countryRepository.findByCountryCode("KOR")).willReturn(Optional.empty());

        // when
        Country result = countryService.update(1, request);

        // then
        assertThat(result.getCountryCode()).isEqualTo("KOR");
        assertThat(result.getCountryName()).isEqualTo("Korea");
    }

    @Test
    @DisplayName("국가 수정 시 중복 코드 예외 발생 테스트")
    void update_duplicateCode() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        ReflectionTestUtils.setField(country, "countryId", 1);
        Country existing = new Country("KOR", "Korea", "한국");
        ReflectionTestUtils.setField(existing, "countryId", 2);
        UpdateCountryRequest request = new UpdateCountryRequest("KOR", "Korea Updated", "한국수정");
        given(countryRepository.findById(1)).willReturn(Optional.of(country));
        given(countryRepository.findByCountryCode("KOR")).willReturn(Optional.of(existing));

        // when & then
        assertThatThrownBy(() -> countryService.update(1, request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("국가 삭제 테스트")
    void delete() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryRepository.findById(1)).willReturn(Optional.of(country));

        // when
        countryService.delete(1);

        // then
        then(countryRepository).should().delete(country);
    }
}
