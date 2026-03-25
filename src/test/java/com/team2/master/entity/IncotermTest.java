package com.team2.master.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncotermTest {

    @Test
    @DisplayName("인코텀 엔티티 생성 테스트")
    void createIncoterm() {
        // given & when
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                "FOB description", "Sea", "E", "Port of Shipment");

        // then
        assertEquals("FOB", incoterm.getIncotermCode());
        assertEquals("Free On Board", incoterm.getIncotermName());
        assertEquals("본선인도", incoterm.getIncotermNameKr());
        assertEquals("FOB description", incoterm.getIncotermDescription());
        assertEquals("Sea", incoterm.getIncotermTransportMode());
        assertEquals("E", incoterm.getIncotermSellerSegments());
        assertEquals("Port of Shipment", incoterm.getIncotermDefaultNamedPlace());
    }

    @Test
    @DisplayName("인코텀 정보 수정 테스트")
    void updateIncoterm() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);

        // when
        incoterm.update("FOB", "Free On Board Updated", "본선인도수정",
                "Updated desc", "Any", "F", "Named Place");

        // then
        assertEquals("Free On Board Updated", incoterm.getIncotermName());
        assertEquals("본선인도수정", incoterm.getIncotermNameKr());
    }

    @Test
    @DisplayName("인코텀 코드 필수값 검증 테스트")
    void incotermCodeRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Incoterm(null, "Free On Board", null,
                        null, null, null, null));
    }

    @Test
    @DisplayName("인코텀명 필수값 검증 테스트")
    void incotermNameRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> new Incoterm("FOB", null, null,
                        null, null, null, null));
    }
}
