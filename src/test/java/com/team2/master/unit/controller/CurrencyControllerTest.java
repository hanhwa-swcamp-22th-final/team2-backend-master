package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateCurrencyRequest;
import com.team2.master.dto.UpdateCurrencyRequest;
import com.team2.master.entity.Currency;
import com.team2.master.controller.CurrencyController;
import com.team2.master.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
@WithMockUser
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    @DisplayName("전체 통화 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        Currency currency = new Currency("USD", "US Dollar", "$");
        given(currencyService.getAll()).willReturn(List.of(currency));

        // when & then
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[0].currencyName").value("US Dollar"));
    }

    @Test
    @DisplayName("통화 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        Currency currency = new Currency("USD", "US Dollar", "$");
        given(currencyService.getById(1)).willReturn(currency);

        // when & then
        mockMvc.perform(get("/api/currencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    @DisplayName("통화 생성 API 테스트")
    void create() throws Exception {
        // given
        CreateCurrencyRequest request = new CreateCurrencyRequest("USD", "US Dollar", "$");
        Currency currency = new Currency("USD", "US Dollar", "$");
        given(currencyService.create(any(CreateCurrencyRequest.class))).willReturn(currency);

        // when & then
        mockMvc.perform(post("/api/currencies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    @DisplayName("통화 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdateCurrencyRequest request = new UpdateCurrencyRequest("USD", "United States Dollar", "US$");
        Currency currency = new Currency("USD", "United States Dollar", "US$");
        given(currencyService.update(eq(1), any(UpdateCurrencyRequest.class))).willReturn(currency);

        // when & then
        mockMvc.perform(put("/api/currencies/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyName").value("United States Dollar"));
    }

    @Test
    @DisplayName("통화 삭제 API 테스트")
    void deleteCurrency() throws Exception {
        // given
        willDoNothing().given(currencyService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/currencies/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
