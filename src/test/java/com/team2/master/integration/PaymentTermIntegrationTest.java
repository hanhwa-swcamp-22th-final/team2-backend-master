package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.repository.PaymentTermRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class PaymentTermIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PaymentTermRepository paymentTermRepository;

    // ==================== GET /api/payment-terms ====================

    @Test
    @DisplayName("통합테스트: 결제조건 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/payment-terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 결제조건 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환송금"));
        paymentTermRepository.save(new PaymentTerm("LC", "Letter of Credit", "신용장"));

        mockMvc.perform(get("/api/payment-terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].paymentTermCode").value("TT"))
                .andExpect(jsonPath("$[1].paymentTermCode").value("LC"));
    }

    // ==================== GET /api/payment-terms/{id} ====================

    @Test
    @DisplayName("통합테스트: 결제조건 단건 조회 - 성공")
    void getById_success() throws Exception {
        PaymentTerm saved = paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환송금"));

        mockMvc.perform(get("/api/payment-terms/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermCode").value("TT"))
                .andExpect(jsonPath("$.paymentTermName").value("Telegraphic Transfer"))
                .andExpect(jsonPath("$.paymentTermDescription").value("전신환송금"));
    }

    @Test
    @DisplayName("통합테스트: 결제조건 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        mockMvc.perform(get("/api/payment-terms/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/payment-terms ====================

    @Test
    @DisplayName("통합테스트: 결제조건 생성 - 성공")
    void create_success() throws Exception {
        Map<String, String> request = Map.of(
                "paymentTermCode", "CAD",
                "paymentTermName", "Cash Against Documents",
                "paymentTermDescription", "서류상환불"
        );

        mockMvc.perform(post("/api/payment-terms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentTermCode").value("CAD"))
                .andExpect(jsonPath("$.paymentTermName").value("Cash Against Documents"))
                .andExpect(jsonPath("$.paymentTermDescription").value("서류상환불"));

        // DB 검증
        List<PaymentTerm> all = paymentTermRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getPaymentTermCode()).isEqualTo("CAD");
    }

    @Test
    @DisplayName("통합테스트: 결제조건 생성 - 중복 코드")
    void create_duplicateCode() throws Exception {
        paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환송금"));

        Map<String, String> request = Map.of(
                "paymentTermCode", "TT",
                "paymentTermName", "Duplicate",
                "paymentTermDescription", "중복"
        );

        mockMvc.perform(post("/api/payment-terms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        assertThat(paymentTermRepository.findAll()).hasSize(1);
    }

    // ==================== PUT /api/payment-terms/{id} ====================

    @Test
    @DisplayName("통합테스트: 결제조건 수정 - 성공")
    void update_success() throws Exception {
        PaymentTerm saved = paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환송금"));

        Map<String, String> request = Map.of(
                "paymentTermCode", "TT",
                "paymentTermName", "T/T Remittance",
                "paymentTermDescription", "전신환송금 수정"
        );

        mockMvc.perform(put("/api/payment-terms/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermName").value("T/T Remittance"))
                .andExpect(jsonPath("$.paymentTermDescription").value("전신환송금 수정"));

        // DB 검증
        PaymentTerm updated = paymentTermRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getPaymentTermName()).isEqualTo("T/T Remittance");
        assertThat(updated.getPaymentTermDescription()).isEqualTo("전신환송금 수정");
    }

    @Test
    @DisplayName("통합테스트: 결제조건 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        Map<String, String> request = Map.of(
                "paymentTermCode", "TT",
                "paymentTermName", "Telegraphic Transfer",
                "paymentTermDescription", "전신환송금"
        );

        mockMvc.perform(put("/api/payment-terms/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/payment-terms/{id} ====================

    @Test
    @DisplayName("통합테스트: 결제조건 삭제 - 성공")
    void delete_success() throws Exception {
        PaymentTerm saved = paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환송금"));

        mockMvc.perform(delete("/api/payment-terms/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Optional<PaymentTerm> deleted = paymentTermRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 결제조건 삭제 - 존재하지 않는 ID")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/payment-terms/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
