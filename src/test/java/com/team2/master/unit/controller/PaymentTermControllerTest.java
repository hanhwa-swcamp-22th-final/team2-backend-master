package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreatePaymentTermRequest;
import com.team2.master.dto.UpdatePaymentTermRequest;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.controller.PaymentTermController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.service.PaymentTermService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentTermController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PaymentTermControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentTermService paymentTermService;

    @Test
    @DisplayName("전체 결제조건 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermService.getAll()).willReturn(List.of(paymentTerm));

        // when & then
        mockMvc.perform(get("/api/payment-terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentTermCode").value("TT"))
                .andExpect(jsonPath("$[0].paymentTermName").value("Telegraphic Transfer"));
    }

    @Test
    @DisplayName("결제조건 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermService.getById(1)).willReturn(paymentTerm);

        // when & then
        mockMvc.perform(get("/api/payment-terms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermCode").value("TT"));
    }

    @Test
    @DisplayName("결제조건 ID로 조회 - 존재하지 않는 결제조건 (404)")
    void getById_notFound() throws Exception {
        // given
        given(paymentTermService.getById(999))
                .willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/payment-terms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("결제조건 생성 API 테스트")
    void create() throws Exception {
        // given
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermService.create(any(CreatePaymentTermRequest.class))).willReturn(paymentTerm);

        // when & then
        mockMvc.perform(post("/api/payment-terms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentTermCode").value("TT"));
    }

    @Test
    @DisplayName("결제조건 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        // given
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermService.create(any(CreatePaymentTermRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 결제조건 코드입니다: TT"));

        // when & then
        mockMvc.perform(post("/api/payment-terms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 결제조건 코드입니다: TT"));
    }

    @Test
    @DisplayName("결제조건 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
        PaymentTerm paymentTerm = new PaymentTerm("T/T", "T/T Transfer", "T/T 송금");
        given(paymentTermService.update(eq(1), any(UpdatePaymentTermRequest.class))).willReturn(paymentTerm);

        // when & then
        mockMvc.perform(put("/api/payment-terms/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermCode").value("T/T"));
    }

    @Test
    @DisplayName("결제조건 수정 - 존재하지 않는 결제조건 (404)")
    void update_notFound() throws Exception {
        // given
        UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
        given(paymentTermService.update(eq(999), any(UpdatePaymentTermRequest.class)))
                .willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/payment-terms/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("결제조건 삭제 API 테스트")
    void deletePaymentTerm() throws Exception {
        // given
        willDoNothing().given(paymentTermService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/payment-terms/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("결제조건 삭제 - 존재하지 않는 결제조건 (404)")
    void deletePaymentTerm_notFound() throws Exception {
        // given
        willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"))
                .given(paymentTermService).delete(999);

        // when & then
        mockMvc.perform(delete("/api/payment-terms/999")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }
}
