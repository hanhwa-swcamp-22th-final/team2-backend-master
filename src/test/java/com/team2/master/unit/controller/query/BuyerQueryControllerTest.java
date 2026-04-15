package com.team2.master.unit.controller.query;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.query.controller.BuyerQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.BuyerQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyerQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class BuyerQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuyerQueryService buyerQueryService;

    private BuyerResponse createTestBuyerResponse() {
        return BuyerResponse.builder()
                .clientName("Test Corp")
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("GET /api/buyers - 전체 바이어 목록 조회")
    void getAllBuyers_success() throws Exception {
        given(buyerQueryService.getAllBuyersPaged(0, 1000))
                .willReturn(PagedResponse.of(List.of(createTestBuyerResponse()), 1L, 0, 1000));

        mockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].buyerName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("GET /api/buyers/{id} - 바이어 상세 조회")
    void getBuyer_success() throws Exception {
        given(buyerQueryService.getBuyer(1)).willReturn(createTestBuyerResponse());

        mockMvc.perform(get("/api/buyers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("John Doe"))
                .andExpect(jsonPath("$.buyerEmail").value("john@test.com"));
    }

    @Test
    @DisplayName("GET /api/buyers/{id} - 존재하지 않는 바이어 조회 (404)")
    void getBuyer_notFound() throws Exception {
        given(buyerQueryService.getBuyer(999))
                .willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/buyers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("바이어를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("GET /api/buyers/client/{clientId} - 거래처별 바이어 목록 조회")
    void getBuyersByClient_success() throws Exception {
        given(buyerQueryService.getBuyersByClientIdPaged(1, 0, 1000))
                .willReturn(PagedResponse.of(List.of(createTestBuyerResponse()), 1L, 0, 1000));

        mockMvc.perform(get("/api/buyers/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].buyerName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("GET /api/clients/{clientId}/buyers - 거래처 하위 바이어 목록 조회 (중첩 URL)")
    void getBuyersByClientNested_success() throws Exception {
        given(buyerQueryService.getBuyersByClientId(1)).willReturn(List.of(createTestBuyerResponse()));

        mockMvc.perform(get("/api/clients/1/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.buyerResponseList[0].buyerName").value("John Doe"))
                .andExpect(jsonPath("$._embedded.buyerResponseList[0].clientName").value("Test Corp"));
    }
}
