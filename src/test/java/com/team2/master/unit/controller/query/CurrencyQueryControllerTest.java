package com.team2.master.unit.controller.query;

import com.team2.master.command.domain.entity.Currency;
import com.team2.master.query.controller.CurrencyQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.CurrencyQueryService;
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

@WebMvcTest(CurrencyQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class CurrencyQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private CurrencyQueryService currencyQueryService;

    @Test
    @DisplayName("전체 통화 목록 조회 API 테스트")
    void getAll() throws Exception {
        Currency currency = new Currency("USD", "US Dollar", "$");
        given(currencyQueryService.getAll()).willReturn(List.of(currency));

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[0].currencyName").value("US Dollar"));
    }

    @Test
    @DisplayName("통화 ID로 조회 API 테스트")
    void getById() throws Exception {
        Currency currency = new Currency("USD", "US Dollar", "$");
        given(currencyQueryService.getById(1)).willReturn(currency);

        mockMvc.perform(get("/api/currencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    @DisplayName("통화 ID로 조회 - 존재하지 않는 통화 (404)")
    void getById_notFound() throws Exception {
        given(currencyQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("통화를 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/currencies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("통화를 찾을 수 없습니다: 999"));
    }
}
