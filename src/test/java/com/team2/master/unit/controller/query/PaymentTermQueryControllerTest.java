package com.team2.master.unit.controller.query;

import com.team2.master.entity.PaymentTerm;
import com.team2.master.query.controller.PaymentTermQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.PaymentTermQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentTermQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PaymentTermQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PaymentTermQueryService paymentTermQueryService;

    @Test
    @DisplayName("전체 결제조건 목록 조회 API 테스트")
    void getAll() throws Exception {
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermQueryService.getAll()).willReturn(List.of(paymentTerm));

        mockMvc.perform(get("/api/payment-terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentTermCode").value("TT"))
                .andExpect(jsonPath("$[0].paymentTermName").value("Telegraphic Transfer"));
    }

    @Test
    @DisplayName("결제조건 ID로 조회 API 테스트")
    void getById() throws Exception {
        PaymentTerm paymentTerm = new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금");
        given(paymentTermQueryService.getById(1)).willReturn(paymentTerm);

        mockMvc.perform(get("/api/payment-terms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentTermCode").value("TT"));
    }

    @Test
    @DisplayName("결제조건 ID로 조회 - 존재하지 않는 결제조건 (404)")
    void getById_notFound() throws Exception {
        given(paymentTermQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("결제조건을 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/payment-terms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결제조건을 찾을 수 없습니다: 999"));
    }
}
