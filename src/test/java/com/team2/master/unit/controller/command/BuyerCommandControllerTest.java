package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.application.controller.BuyerCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.application.service.BuyerCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyerCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class BuyerCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BuyerCommandService buyerCommandService;

    private Client createTestClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
    }

    private Buyer createTestBuyer() {
        Buyer buyer = Buyer.builder()
                .client(createTestClient())
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        ReflectionTestUtils.setField(buyer, "buyerId", 1);
        return buyer;
    }

    @Test
    @DisplayName("POST /api/buyers - 바이어 생성 성공")
    void createBuyer_success() throws Exception {
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(1)
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        given(buyerCommandService.createBuyer(any(CreateBuyerRequest.class))).willReturn(createTestBuyer());

        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buyerName").value("John Doe"))
                .andExpect(jsonPath("$.clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("POST /api/buyers - 존재하지 않는 거래처로 생성 실패 (404)")
    void createBuyer_clientNotFound() throws Exception {
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(999)
                .buyerName("John Doe")
                .build();
        given(buyerCommandService.createBuyer(any(CreateBuyerRequest.class)))
                .willThrow(new ResourceNotFoundException("거래처를 찾을 수 없습니다: 999"));

        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("거래처를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("PUT /api/buyers/{id} - 바이어 수정 성공")
    void updateBuyer_success() throws Exception {
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
        given(buyerCommandService.updateBuyer(eq(1), any(UpdateBuyerRequest.class))).willReturn(updatedBuyer);

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
        UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                .buyerName("Jane Smith")
                .build();
        given(buyerCommandService.updateBuyer(eq(999), any(UpdateBuyerRequest.class)))
                .willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"));

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
        willDoNothing().given(buyerCommandService).deleteBuyer(1);

        mockMvc.perform(delete("/api/buyers/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/buyers/{id} - 존재하지 않는 바이어 삭제 (404)")
    void deleteBuyer_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("바이어를 찾을 수 없습니다: 999"))
                .given(buyerCommandService).deleteBuyer(999);

        mockMvc.perform(delete("/api/buyers/999")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("바이어를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("POST /api/clients/{clientId}/buyers - 거래처 하위 바이어 생성 (중첩 URL)")
    void createBuyerNested_success() throws Exception {
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .buyerName("John Doe")
                .buyerPosition("Manager")
                .buyerEmail("john@test.com")
                .buyerTel("010-1234-5678")
                .build();
        given(buyerCommandService.createBuyer(any(CreateBuyerRequest.class))).willReturn(createTestBuyer());

        mockMvc.perform(post("/api/clients/1/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buyerName").value("John Doe"))
                .andExpect(jsonPath("$.clientName").value("Test Corp"));
    }
}
