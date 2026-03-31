package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.repository.CountryRepository;
import com.team2.master.repository.PortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class PortIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PortRepository portRepository;
    @Autowired private CountryRepository countryRepository;

    // ==================== GET /api/ports ====================

    @Test
    @DisplayName("통합테스트: 항구 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/ports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 항구 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));
        portRepository.save(new Port("KRICN", "Incheon Port", "Incheon", country));

        mockMvc.perform(get("/api/ports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ==================== GET /api/ports/{id} ====================

    @Test
    @DisplayName("통합테스트: 항구 단건 조회 - 성공")
    void getById_success() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        Port saved = portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));

        mockMvc.perform(get("/api/ports/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.portName").value("Busan Port"))
                .andExpect(jsonPath("$.portCity").value("Busan"));
    }

    @Test
    @DisplayName("통합테스트: 항구 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        mockMvc.perform(get("/api/ports/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/ports ====================

    @Test
    @DisplayName("통합테스트: 항구 생성 - 유효한 countryId로 성공")
    void create_success() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "KRPUS");
        request.put("portName", "Busan Port");
        request.put("portCity", "Busan");
        request.put("countryId", country.getId());

        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.portName").value("Busan Port"))
                .andExpect(jsonPath("$.portCity").value("Busan"));

        // DB 검증
        List<Port> all = portRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getPortCode()).isEqualTo("KRPUS");
        assertThat(all.get(0).getCountry().getId()).isEqualTo(country.getId());
    }

    @Test
    @DisplayName("통합테스트: 항구 생성 - 중복 코드")
    void create_duplicateCode() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "KRPUS");
        request.put("portName", "Duplicate Port");
        request.put("portCity", "City");
        request.put("countryId", country.getId());

        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        assertThat(portRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("통합테스트: 항구 생성 - 존재하지 않는 countryId")
    void create_invalidCountryId() throws Exception {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "KRPUS");
        request.put("portName", "Busan Port");
        request.put("portCity", "Busan");
        request.put("countryId", 9999);

        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(portRepository.findAll()).isEmpty();
    }

    // ==================== PUT /api/ports/{id} ====================

    @Test
    @DisplayName("통합테스트: 항구 수정 - 성공")
    void update_success() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        Country country2 = countryRepository.save(new Country("JP", "Japan", "일본"));
        Port saved = portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "JPTYO");
        request.put("portName", "Tokyo Port");
        request.put("portCity", "Tokyo");
        request.put("countryId", country2.getId());

        mockMvc.perform(put("/api/ports/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("JPTYO"))
                .andExpect(jsonPath("$.portName").value("Tokyo Port"));

        // DB 검증
        Port updated = portRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getPortCode()).isEqualTo("JPTYO");
        assertThat(updated.getCountry().getId()).isEqualTo(country2.getId());
    }

    @Test
    @DisplayName("통합테스트: 항구 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "KRPUS");
        request.put("portName", "Busan Port");
        request.put("portCity", "Busan");
        request.put("countryId", country.getId());

        mockMvc.perform(put("/api/ports/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("통합테스트: 항구 수정 - 존재하지 않는 countryId")
    void update_invalidCountryId() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        Port saved = portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("portCode", "KRPUS");
        request.put("portName", "Busan Port");
        request.put("portCity", "Busan");
        request.put("countryId", 9999);

        mockMvc.perform(put("/api/ports/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/ports/{id} ====================

    @Test
    @DisplayName("통합테스트: 항구 삭제 - 성공")
    void delete_success() throws Exception {
        Country country = countryRepository.save(new Country("KR", "Korea", "한국"));
        Port saved = portRepository.save(new Port("KRPUS", "Busan Port", "Busan", country));

        mockMvc.perform(delete("/api/ports/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Optional<Port> deleted = portRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 항구 삭제 - 존재하지 않는 ID")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/ports/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
