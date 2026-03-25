package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.entity.Currency;
import com.team2.master.repository.CurrencyRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class CurrencyIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CurrencyRepository currencyRepository;

    // ==================== GET /api/currencies ====================

    @Test
    @DisplayName("통합테스트: 통화 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 통화 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        currencyRepository.save(new Currency("USD", "US Dollar", "$"));
        currencyRepository.save(new Currency("KRW", "Korean Won", "₩"));

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[1].currencyCode").value("KRW"));
    }

    // ==================== GET /api/currencies/{id} ====================

    @Test
    @DisplayName("통합테스트: 통화 단건 조회 - 성공")
    void getById_success() throws Exception {
        Currency saved = currencyRepository.save(new Currency("USD", "US Dollar", "$"));

        mockMvc.perform(get("/api/currencies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.currencyName").value("US Dollar"))
                .andExpect(jsonPath("$.currencySymbol").value("$"));
    }

    @Test
    @DisplayName("통합테스트: 통화 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() {
        assertThatThrownBy(() ->
                mockMvc.perform(get("/api/currencies/{id}", 9999))
        ).isInstanceOf(ServletException.class)
         .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    // ==================== POST /api/currencies ====================

    @Test
    @DisplayName("통합테스트: 통화 생성 - 성공")
    void create_success() throws Exception {
        Map<String, String> request = Map.of(
                "currencyCode", "EUR",
                "currencyName", "Euro",
                "currencySymbol", "€"
        );

        mockMvc.perform(post("/api/currencies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currencyCode").value("EUR"))
                .andExpect(jsonPath("$.currencyName").value("Euro"))
                .andExpect(jsonPath("$.currencySymbol").value("€"));

        // DB 검증
        List<Currency> all = currencyRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("통합테스트: 통화 생성 - 중복 코드")
    void create_duplicateCode() {
        currencyRepository.save(new Currency("USD", "US Dollar", "$"));

        Map<String, String> request = Map.of(
                "currencyCode", "USD",
                "currencyName", "Another Dollar",
                "currencySymbol", "$"
        );

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/currencies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
         .hasCauseInstanceOf(IllegalStateException.class);

        assertThat(currencyRepository.findAll()).hasSize(1);
    }

    // ==================== PUT /api/currencies/{id} ====================

    @Test
    @DisplayName("통합테스트: 통화 수정 - 성공")
    void update_success() throws Exception {
        Currency saved = currencyRepository.save(new Currency("USD", "US Dollar", "$"));

        Map<String, String> request = Map.of(
                "currencyCode", "USD",
                "currencyName", "United States Dollar",
                "currencySymbol", "US$"
        );

        mockMvc.perform(put("/api/currencies/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyName").value("United States Dollar"))
                .andExpect(jsonPath("$.currencySymbol").value("US$"));

        // DB 검증
        Currency updated = currencyRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getCurrencyName()).isEqualTo("United States Dollar");
        assertThat(updated.getCurrencySymbol()).isEqualTo("US$");
    }

    @Test
    @DisplayName("통합테스트: 통화 수정 - 존재하지 않는 ID")
    void update_notFound() {
        Map<String, String> request = Map.of(
                "currencyCode", "USD",
                "currencyName", "US Dollar",
                "currencySymbol", "$"
        );

        assertThatThrownBy(() ->
                mockMvc.perform(put("/api/currencies/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
         .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    // ==================== DELETE /api/currencies/{id} ====================

    @Test
    @DisplayName("통합테스트: 통화 삭제 - 성공")
    void delete_success() throws Exception {
        Currency saved = currencyRepository.save(new Currency("USD", "US Dollar", "$"));

        mockMvc.perform(delete("/api/currencies/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isOk());

        Optional<Currency> deleted = currencyRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 통화 삭제 - 존재하지 않는 ID")
    void delete_notFound() {
        assertThatThrownBy(() ->
                mockMvc.perform(delete("/api/currencies/{id}", 9999)
                        .with(csrf()))
        ).isInstanceOf(ServletException.class)
         .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
