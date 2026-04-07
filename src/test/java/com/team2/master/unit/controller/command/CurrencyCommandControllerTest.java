package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.CreateCurrencyRequest;
import com.team2.master.command.application.dto.UpdateCurrencyRequest;
import com.team2.master.command.domain.entity.Currency;
import com.team2.master.command.application.controller.CurrencyCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.application.service.CurrencyCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class CurrencyCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private CurrencyCommandService currencyCommandService;

    @Test
    @DisplayName("통화 생성 API 테스트")
    void create() throws Exception {
        CreateCurrencyRequest request = new CreateCurrencyRequest("USD", "US Dollar", "$");
        Currency currency = new Currency("USD", "US Dollar", "$");
        ReflectionTestUtils.setField(currency, "currencyId", 1);
        given(currencyCommandService.create(any(CreateCurrencyRequest.class))).willReturn(currency);

        mockMvc.perform(post("/api/currencies").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    @DisplayName("통화 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        CreateCurrencyRequest request = new CreateCurrencyRequest("USD", "US Dollar", "$");
        given(currencyCommandService.create(any(CreateCurrencyRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 통화 코드입니다: USD"));

        mockMvc.perform(post("/api/currencies").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 통화 코드입니다: USD"));
    }

    @Test
    @DisplayName("통화 수정 API 테스트")
    void update() throws Exception {
        UpdateCurrencyRequest request = new UpdateCurrencyRequest("USD", "United States Dollar", "US$");
        Currency currency = new Currency("USD", "United States Dollar", "US$");
        given(currencyCommandService.update(eq(1), any(UpdateCurrencyRequest.class))).willReturn(currency);

        mockMvc.perform(put("/api/currencies/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyName").value("United States Dollar"));
    }

    @Test
    @DisplayName("통화 수정 - 존재하지 않는 통화 (404)")
    void update_notFound() throws Exception {
        UpdateCurrencyRequest request = new UpdateCurrencyRequest("USD", "United States Dollar", "US$");
        given(currencyCommandService.update(eq(999), any(UpdateCurrencyRequest.class)))
                .willThrow(new ResourceNotFoundException("통화를 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/currencies/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("통화를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("통화 삭제 API 테스트")
    void deleteCurrency() throws Exception {
        willDoNothing().given(currencyCommandService).delete(1);
        mockMvc.perform(delete("/api/currencies/1").with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("통화 삭제 - 존재하지 않는 통화 (404)")
    void deleteCurrency_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("통화를 찾을 수 없습니다: 999"))
                .given(currencyCommandService).delete(999);
        mockMvc.perform(delete("/api/currencies/999").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("통화를 찾을 수 없습니다: 999"));
    }
}
