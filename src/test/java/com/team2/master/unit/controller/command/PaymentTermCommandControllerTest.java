package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.CreatePaymentTermRequest;
import com.team2.master.command.application.dto.UpdatePaymentTermRequest;
import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.command.application.controller.PaymentTermCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.application.service.PaymentTermCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentTermCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PaymentTermCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private PaymentTermCommandService paymentTermCommandService;

    @Test
    @DisplayName("결제조건 생성 API 테스트")
    void create() throws Exception {
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermCommandService.create(any(CreatePaymentTermRequest.class))).willReturn(paymentTerm);

        mockMvc.perform(post("/api/payment-terms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentTermCode").value("TT"));
    }

    @Test
    @DisplayName("결제조건 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        CreatePaymentTermRequest request = new CreatePaymentTermRequest("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermCommandService.create(any(CreatePaymentTermRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 결제조건 코드입니다: TT"));

        mockMvc.perform(post("/api/payment-terms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 결제조건 코드입니다: TT"));
    }

    @Test
    @DisplayName("결제조건 수정 API 테스트")
    void update() throws Exception {
        UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
        PaymentTerm paymentTerm = new PaymentTerm("T/T", "T/T Transfer", "T/T 송금");
        given(paymentTermCommandService.update(eq(1), any(UpdatePaymentTermRequest.class))).willReturn(paymentTerm);

        mockMvc.perform(put("/api/payment-terms/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermCode").value("T/T"));
    }

    @Test
    @DisplayName("결제조건 수정 - 존재하지 않는 결제조건 (404)")
    void update_notFound() throws Exception {
        UpdatePaymentTermRequest request = new UpdatePaymentTermRequest("T/T", "T/T Transfer", "T/T 송금");
        given(paymentTermCommandService.update(eq(999), any(UpdatePaymentTermRequest.class)))
                .willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/payment-terms/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("결제조건 삭제 API 테스트")
    void deletePaymentTerm() throws Exception {
        willDoNothing().given(paymentTermCommandService).delete(1);
        mockMvc.perform(delete("/api/payment-terms/1").with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("결제조건 삭제 - 존재하지 않는 결제조건 (404)")
    void deletePaymentTerm_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"))
                .given(paymentTermCommandService).delete(999);
        mockMvc.perform(delete("/api/payment-terms/999").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }
}
