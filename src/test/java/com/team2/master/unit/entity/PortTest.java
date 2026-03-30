package com.team2.master.unit.entity;

import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortTest {

    @Test
    @DisplayName("항구 엔티티 생성 테스트")
    void createPort() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");

        // when
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);

        // then
        assertEquals("KRPUS", port.getPortCode());
        assertEquals("Busan Port", port.getPortName());
        assertEquals("Busan", port.getPortCity());
        assertEquals(country, port.getCountry());
    }

    @Test
    @DisplayName("항구 정보 수정 테스트")
    void updatePort() {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);

        Country newCountry = new Country("JP", "Japan", "일본");

        // when
        port.update("KRPUS2", "Busan New Port", "Busan City", newCountry);

        // then
        assertEquals("KRPUS2", port.getPortCode());
        assertEquals("Busan New Port", port.getPortName());
        assertEquals("Busan City", port.getPortCity());
        assertEquals(newCountry, port.getCountry());
    }

    @Test
    @DisplayName("항구 코드 필수값 검증 테스트")
    void portCodeRequired() {
        Country country = new Country("KR", "South Korea", "대한민국");
        assertThrows(IllegalArgumentException.class,
                () -> new Port(null, "Busan Port", "Busan", country));
    }

    @Test
    @DisplayName("항구명 필수값 검증 테스트")
    void portNameRequired() {
        Country country = new Country("KR", "South Korea", "대한민국");
        assertThrows(IllegalArgumentException.class,
                () -> new Port("KRPUS", null, "Busan", country));
    }

    @Test
    @DisplayName("항구 국가 필수값 검증 테스트")
    void portCountryRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Port("KRPUS", "Busan Port", "Busan", null));
    }
}
