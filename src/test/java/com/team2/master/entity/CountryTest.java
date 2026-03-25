package com.team2.master.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    @DisplayName("국가 엔티티 생성 테스트")
    void createCountry() {
        // given & when
        Country country = new Country("KR", "South Korea", "대한민국");

        // then
        assertEquals("KR", country.getCountryCode());
        assertEquals("South Korea", country.getCountryName());
        assertEquals("대한민국", country.getCountryNameKr());
    }

    @Test
    @DisplayName("국가 정보 수정 테스트")
    void updateCountry() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");

        // when
        country.update("KOR", "Korea", "한국");

        // then
        assertEquals("KOR", country.getCountryCode());
        assertEquals("Korea", country.getCountryName());
        assertEquals("한국", country.getCountryNameKr());
    }

    @Test
    @DisplayName("국가 코드 필수값 검증 테스트")
    void countryCodeRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Country(null, "South Korea", "대한민국"));
    }

    @Test
    @DisplayName("국가명 필수값 검증 테스트")
    void countryNameRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Country("KR", null, "대한민국"));
    }
}
