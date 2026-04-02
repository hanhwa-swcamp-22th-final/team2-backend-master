package com.team2.master.unit.common;

import com.team2.master.common.PagedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PagedResponseTest {

    @Test
    @DisplayName("PagedResponse.of - 정상적인 페이징 응답 생성")
    void of_success() {
        // given
        List<String> content = List.of("a", "b", "c");

        // when
        PagedResponse<String> result = PagedResponse.of(content, 25L, 0, 10);

        // then
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.totalElements()).isEqualTo(25L);
        assertThat(result.totalPages()).isEqualTo(3);
        assertThat(result.currentPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("PagedResponse.of - 빈 콘텐츠")
    void of_emptyContent() {
        // when
        PagedResponse<String> result = PagedResponse.of(List.of(), 0L, 0, 10);

        // then
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
        assertThat(result.currentPage()).isZero();
    }

    @Test
    @DisplayName("PagedResponse.of - size가 0인 경우 totalPages는 0")
    void of_zeroSize() {
        // when
        PagedResponse<String> result = PagedResponse.of(List.of(), 5L, 0, 0);

        // then
        assertThat(result.totalPages()).isZero();
    }

    @Test
    @DisplayName("PagedResponse.of - totalElements가 size의 정확한 배수일 때")
    void of_exactMultiple() {
        // when
        PagedResponse<String> result = PagedResponse.of(List.of("a"), 20L, 1, 10);

        // then
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.currentPage()).isEqualTo(1);
    }
}
