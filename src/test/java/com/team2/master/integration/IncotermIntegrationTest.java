package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.entity.Incoterm;
import com.team2.master.repository.IncotermRepository;
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
class IncotermIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private IncotermRepository incotermRepository;

    private Incoterm createIncoterm(String code, String name) {
        return new Incoterm(code, name, name + "한글", "설명", "해상", "매도인", "부산항");
    }

    // ==================== GET /api/incoterms ====================

    @Test
    @DisplayName("통합테스트: 인코텀 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/incoterms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 인코텀 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        incotermRepository.save(createIncoterm("FOB", "Free On Board"));
        incotermRepository.save(createIncoterm("CIF", "Cost Insurance Freight"));

        mockMvc.perform(get("/api/incoterms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ==================== GET /api/incoterms/{id} ====================

    @Test
    @DisplayName("통합테스트: 인코텀 단건 조회 - 성공")
    void getById_success() throws Exception {
        Incoterm saved = incotermRepository.save(createIncoterm("FOB", "Free On Board"));

        mockMvc.perform(get("/api/incoterms/{id}", saved.getIncotermId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermCode").value("FOB"))
                .andExpect(jsonPath("$.incotermName").value("Free On Board"))
                .andExpect(jsonPath("$.incotermNameKr").value("Free On Board한글"))
                .andExpect(jsonPath("$.incotermDescription").value("설명"))
                .andExpect(jsonPath("$.incotermTransportMode").value("해상"))
                .andExpect(jsonPath("$.incotermSellerSegments").value("매도인"))
                .andExpect(jsonPath("$.incotermDefaultNamedPlace").value("부산항"));
    }

    @Test
    @DisplayName("통합테스트: 인코텀 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        mockMvc.perform(get("/api/incoterms/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/incoterms ====================

    @Test
    @DisplayName("통합테스트: 인코텀 생성 - 7개 필드 모두 포함하여 성공")
    void create_success_allFields() throws Exception {
        Map<String, String> request = new LinkedHashMap<>();
        request.put("incotermCode", "EXW");
        request.put("incotermName", "Ex Works");
        request.put("incotermNameKr", "공장인도");
        request.put("incotermDescription", "매도인의 영업장소에서 물품을 인도");
        request.put("incotermTransportMode", "복합운송");
        request.put("incotermSellerSegments", "매도인구간");
        request.put("incotermDefaultNamedPlace", "공장");

        mockMvc.perform(post("/api/incoterms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incotermCode").value("EXW"))
                .andExpect(jsonPath("$.incotermName").value("Ex Works"))
                .andExpect(jsonPath("$.incotermNameKr").value("공장인도"))
                .andExpect(jsonPath("$.incotermDescription").value("매도인의 영업장소에서 물품을 인도"))
                .andExpect(jsonPath("$.incotermTransportMode").value("복합운송"))
                .andExpect(jsonPath("$.incotermSellerSegments").value("매도인구간"))
                .andExpect(jsonPath("$.incotermDefaultNamedPlace").value("공장"));

        // DB 검증
        List<Incoterm> all = incotermRepository.findAll();
        assertThat(all).hasSize(1);
        Incoterm created = all.get(0);
        assertThat(created.getIncotermCode()).isEqualTo("EXW");
        assertThat(created.getIncotermDescription()).isEqualTo("매도인의 영업장소에서 물품을 인도");
        assertThat(created.getIncotermTransportMode()).isEqualTo("복합운송");
        assertThat(created.getIncotermSellerSegments()).isEqualTo("매도인구간");
        assertThat(created.getIncotermDefaultNamedPlace()).isEqualTo("공장");
    }

    @Test
    @DisplayName("통합테스트: 인코텀 생성 - 중복 코드")
    void create_duplicateCode() throws Exception {
        incotermRepository.save(createIncoterm("FOB", "Free On Board"));

        Map<String, String> request = new LinkedHashMap<>();
        request.put("incotermCode", "FOB");
        request.put("incotermName", "Duplicate");
        request.put("incotermNameKr", "중복");
        request.put("incotermDescription", "desc");
        request.put("incotermTransportMode", "mode");
        request.put("incotermSellerSegments", "seg");
        request.put("incotermDefaultNamedPlace", "place");

        mockMvc.perform(post("/api/incoterms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        assertThat(incotermRepository.findAll()).hasSize(1);
    }

    // ==================== PUT /api/incoterms/{id} ====================

    @Test
    @DisplayName("통합테스트: 인코텀 수정 - 성공")
    void update_success() throws Exception {
        Incoterm saved = incotermRepository.save(createIncoterm("FOB", "Free On Board"));

        Map<String, String> request = new LinkedHashMap<>();
        request.put("incotermCode", "FOB");
        request.put("incotermName", "Free On Board Updated");
        request.put("incotermNameKr", "본선인도수정");
        request.put("incotermDescription", "수정된 설명");
        request.put("incotermTransportMode", "해상수정");
        request.put("incotermSellerSegments", "매도인수정");
        request.put("incotermDefaultNamedPlace", "인천항");

        mockMvc.perform(put("/api/incoterms/{id}", saved.getIncotermId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermName").value("Free On Board Updated"))
                .andExpect(jsonPath("$.incotermDefaultNamedPlace").value("인천항"));

        // DB 검증
        Incoterm updated = incotermRepository.findById(saved.getIncotermId()).orElseThrow();
        assertThat(updated.getIncotermName()).isEqualTo("Free On Board Updated");
        assertThat(updated.getIncotermDefaultNamedPlace()).isEqualTo("인천항");
    }

    @Test
    @DisplayName("통합테스트: 인코텀 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        Map<String, String> request = new LinkedHashMap<>();
        request.put("incotermCode", "FOB");
        request.put("incotermName", "Free On Board");
        request.put("incotermNameKr", "본선인도");
        request.put("incotermDescription", "설명");
        request.put("incotermTransportMode", "해상");
        request.put("incotermSellerSegments", "매도인");
        request.put("incotermDefaultNamedPlace", "부산항");

        mockMvc.perform(put("/api/incoterms/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/incoterms/{id} ====================

    @Test
    @DisplayName("통합테스트: 인코텀 삭제 - 성공")
    void delete_success() throws Exception {
        Incoterm saved = incotermRepository.save(createIncoterm("FOB", "Free On Board"));

        mockMvc.perform(delete("/api/incoterms/{id}", saved.getIncotermId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Optional<Incoterm> deleted = incotermRepository.findById(saved.getIncotermId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 인코텀 삭제 - 존재하지 않는 ID")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/incoterms/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
