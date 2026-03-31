package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.entity.Country;
import com.team2.master.command.repository.CountryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;
import org.springframework.http.MediaType;
import jakarta.persistence.EntityManager;
import org.springframework.security.test.context.support.WithMockUser;
import jakarta.persistence.EntityManager;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.persistence.EntityManager;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import jakarta.persistence.EntityManager;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import jakarta.persistence.EntityManager;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class CountryIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CountryRepository countryRepository;

    // ==================== GET /api/countries ====================

    @Test
    @DisplayName("통합테스트: 국가 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 국가 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        countryRepository.save(new Country("KR", "Korea", "한국"));
        countryRepository.save(new Country("US", "United States", "미국"));

        entityManager.flush();
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].countryCode").value("KR"))
                .andExpect(jsonPath("$[1].countryCode").value("US"));
    }

    // ==================== GET /api/countries/{id} ====================

    @Test
    @DisplayName("통합테스트: 국가 단건 조회 - 성공")
    void getById_success() throws Exception {
        Country saved = countryRepository.save(new Country("KR", "Korea", "한국"));

        entityManager.flush();
        mockMvc.perform(get("/api/countries/{id}", saved.getCountryId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("KR"))
                .andExpect(jsonPath("$.countryName").value("Korea"))
                .andExpect(jsonPath("$.countryNameKr").value("한국"));
    }

    @Test
    @DisplayName("통합테스트: 국가 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/countries/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/countries ====================

    @Test
    @DisplayName("통합테스트: 국가 생성 - 성공")
    void create_success() throws Exception {
        Map<String, String> request = Map.of(
                "countryCode", "JP",
                "countryName", "Japan",
                "countryNameKr", "일본"
        );

        mockMvc.perform(post("/api/countries")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryCode").value("JP"))
                .andExpect(jsonPath("$.countryName").value("Japan"))
                .andExpect(jsonPath("$.countryNameKr").value("일본"));

        // DB 검증
        List<Country> all = countryRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getCountryCode()).isEqualTo("JP");
    }

    @Test
    @DisplayName("통합테스트: 국가 생성 - 유효성 검증 실패 (빈 필드)")
    void create_validationFail() throws Exception {
        Map<String, String> request = Map.of(
                "countryCode", "",
                "countryName", ""
        );

        mockMvc.perform(post("/api/countries")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        assertThat(countryRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 국가 생성 - 중복 코드")
    void create_duplicateCode() throws Exception {
        countryRepository.save(new Country("KR", "Korea", "한국"));

        Map<String, String> request = Map.of(
                "countryCode", "KR",
                "countryName", "Korea2",
                "countryNameKr", "한국2"
        );

        mockMvc.perform(post("/api/countries")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // DB에 1건만 존재
        assertThat(countryRepository.findAll()).hasSize(1);
    }

    // ==================== PUT /api/countries/{id} ====================

    @Test
    @DisplayName("통합테스트: 국가 수정 - 성공")
    void update_success() throws Exception {
        Country saved = countryRepository.save(new Country("KR", "Korea", "한국"));

        Map<String, String> request = Map.of(
                "countryCode", "KR",
                "countryName", "South Korea",
                "countryNameKr", "대한민국"
        );

        mockMvc.perform(put("/api/countries/{id}", saved.getCountryId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("South Korea"))
                .andExpect(jsonPath("$.countryNameKr").value("대한민국"));

        // DB 검증
        Country updated = countryRepository.findById(saved.getCountryId()).orElseThrow();
        assertThat(updated.getCountryName()).isEqualTo("South Korea");
        assertThat(updated.getCountryNameKr()).isEqualTo("대한민국");
    }

    @Test
    @DisplayName("통합테스트: 국가 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        Map<String, String> request = Map.of(
                "countryCode", "KR",
                "countryName", "Korea",
                "countryNameKr", "한국"
        );

        mockMvc.perform(put("/api/countries/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/countries/{id} ====================

    @Test
    @DisplayName("통합테스트: 국가 삭제 - 성공")
    void delete_success() throws Exception {
        Country saved = countryRepository.save(new Country("KR", "Korea", "한국"));

        mockMvc.perform(delete("/api/countries/{id}", saved.getCountryId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // DB 검증
        Optional<Country> deleted = countryRepository.findById(saved.getCountryId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 국가 삭제 - 존재하지 않는 ID")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/countries/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
