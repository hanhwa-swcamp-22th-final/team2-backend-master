package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.controller.BuyerController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.service.BuyerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyerController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class BuyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BuyerService buyerService;

    private Client createTestClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
    }

    private Buyer createTestBuyer() {
        return Buyer.builder()
                .client(createTestClient())
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("POST /api/buyers - 바이어 생성 성공")
    void createBuyer_success() throws Exception {
        // given
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(1)
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        given(buyerService.createBuyer(any(CreateBuyerRequest.class))).willReturn(createTestBuyer());

        // when & then
        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buyerName").value("John Doe"))
                .andExpect(jsonPath("$.buyerPosition").value("Manager"))
                .andExpect(jsonPath("$.buyerEmail").value("john@test.com"))
                .andExpect(jsonPath("$.buyerTel").value("010-1234-5678"))
                .andExpect(jsonPath("$.clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("POST /api/buyers - 존재하지 않는 거래처로 생성 실패 (404)")
    void createBuyer_clientNotFound() throws Exception {
        // given
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(999)
                .buyerName("John Doe")
                .build();
        given(buyerService.createBuyer(any(CreateBuyerRequest.class)))
                .willThrow(new ResourceNotFoundException("거래처를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("거래처를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("GET /api/buyers - 전체 바이어 목록 조회")
    void getAllBuyers_success() throws Exception {
        // given
        given(buyerService.getAllBuyers()).willReturn(List.of(createTestBuyer()));

        // when & then
        mockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].buyerName").value("John Doe"))
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("GET /api/buyers/{id} - 바이어 상세 조회")
    void getBuyer_success() throws Exception {
        // given
        given(buyerService.getBuyer(1)).willReturn(createTestBuyer());

        // when & then
        mockMvc.perform(get("/api/buyers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("John Doe"))
                .andExpect(jsonPath("$.buyerEmail").value("john@test.com"));
    }

    @Test
    @DisplayName("GET /api/buyers/{id} - 존재하지 않는 바이어 조회 (404)")
    void getBuyer_notFound() throws Exception {
        // given
        given(buyerService.getBuyer(999))
                .willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/buyers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("바이어를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("GET /api/buyers/client/{clientId} - 거래처별 바이어 목록 조회")
    void getBuyersByClient_success() throws Exception {
        // given
        given(buyerService.getBuyersByClientId(1)).willReturn(List.of(createTestBuyer()));

        // when & then
        mockMvc.perform(get("/api/buyers/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].buyerName").value("John Doe"))
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("PUT /api/buyers/{id} - 바이어 수정 성공")
    void updateBuyer_success() throws Exception {
        // given
        UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                .buyerName("Jane Smith")
                .buyerPosition("Director")
                .build();
        Buyer updatedBuyer = Buyer.builder()
                .client(createTestClient())
                .buyerName("Jane Smith")
                .buyerPosition("Director")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        given(buyerService.updateBuyer(eq(1), any(UpdateBuyerRequest.class))).willReturn(updatedBuyer);

        // when & then
        mockMvc.perform(put("/api/buyers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("Jane Smith"))
                .andExpect(jsonPath("$.buyerPosition").value("Director"));
    }

    @Test
    @DisplayName("PUT /api/buyers/{id} - 존재하지 않는 바이어 수정 (404)")
    void updateBuyer_notFound() throws Exception {
        // given
        UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                .buyerName("Jane Smith")
                .build();
        given(buyerService.updateBuyer(eq(999), any(UpdateBuyerRequest.class)))
                .willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/buyers/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("바이어를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("DELETE /api/buyers/{id} - 바이어 삭제 성공")
    void deleteBuyer_success() throws Exception {
        // given
        willDoNothing().given(buyerService).deleteBuyer(1);

        // when & then
        mockMvc.perform(delete("/api/buyers/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/buyers/{id} - 존재하지 않는 바이어 삭제 (404)")
    void deleteBuyer_notFound() throws Exception {
        // given
        willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"))
                .given(buyerService).deleteBuyer(999);

        // when & then
        mockMvc.perform(delete("/api/buyers/999")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("바이어를 찾을 수 없습니다: 999"));
    }
}
