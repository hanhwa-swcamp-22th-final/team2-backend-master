package com.team2.master.unit.converter;

import com.team2.master.command.domain.entity.converter.ItemStatusConverter;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemStatusConverterTest {

    private final ItemStatusConverter converter = new ItemStatusConverter();

    @Test
    @DisplayName("convertToDatabaseColumn - ACTIVE를 'active'로 변환")
    void convertToDatabaseColumn_active() {
        assertThat(converter.convertToDatabaseColumn(ItemStatus.ACTIVE)).isEqualTo("active");
    }

    @Test
    @DisplayName("convertToDatabaseColumn - INACTIVE를 'inactive'로 변환")
    void convertToDatabaseColumn_inactive() {
        assertThat(converter.convertToDatabaseColumn(ItemStatus.INACTIVE)).isEqualTo("inactive");
    }

    @Test
    @DisplayName("convertToDatabaseColumn - null 입력 시 null 반환")
    void convertToDatabaseColumn_null() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute - 'active'를 ACTIVE로 변환")
    void convertToEntityAttribute_active() {
        assertThat(converter.convertToEntityAttribute("active")).isEqualTo(ItemStatus.ACTIVE);
    }

    @Test
    @DisplayName("convertToEntityAttribute - 'inactive'를 INACTIVE로 변환")
    void convertToEntityAttribute_inactive() {
        assertThat(converter.convertToEntityAttribute("inactive")).isEqualTo(ItemStatus.INACTIVE);
    }

    @Test
    @DisplayName("convertToEntityAttribute - null 입력 시 null 반환")
    void convertToEntityAttribute_null() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute - 알 수 없는 값 입력 시 예외 발생")
    void convertToEntityAttribute_unknownValue() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown ItemStatus dbValue");
    }
}
