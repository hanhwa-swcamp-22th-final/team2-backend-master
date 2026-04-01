package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.application.controller.ClientCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.application.service.ClientCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class ClientCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientCommandService clientCommandService;

    private Client createTestClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .clientCity("Seoul")
                .clientEmail("test@corp.com")
                .clientManager("홍길동")
                .departmentId(1)
                .clientStatus(ClientStatus.ACTIVE)
                .clientRegDate(LocalDate.of(2025, 1, 1))
                .build();
    }

    @Test
    @DisplayName("POST /api/clients - 거래처 생성 성공")
    void createClient_success() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .build();
        given(clientCommandService.createClient(any(CreateClientRequest.class))).willReturn(createTestClient());

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value("Test Corp"))
                .andExpect(jsonPath("$.clientCode").value("CLI001"));
    }

    @Test
    @DisplayName("POST /api/clients - 중복 코드로 생성 실패 (409)")
    void createClient_duplicate() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .build();
        given(clientCommandService.createClient(any(CreateClientRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 거래처 코드입니다: CLI001"));

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 거래처 코드입니다: CLI001"));
    }

    @Test
    @DisplayName("PUT /api/clients/{id} - 거래처 수정 성공")
    void updateClient_success() throws Exception {
        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Corp")
                .clientNameKr("수정 주식회사")
                .build();
        Client updatedClient = Client.builder()
                .clientCode("CLI001")
                .clientName("Updated Corp")
                .clientNameKr("수정 주식회사")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        given(clientCommandService.updateClient(eq(1), any(UpdateClientRequest.class))).willReturn(updatedClient);

        mockMvc.perform(put("/api/clients/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Updated Corp"))
                .andExpect(jsonPath("$.clientNameKr").value("수정 주식회사"));
    }

    @Test
    @DisplayName("PUT /api/clients/{id} - 존재하지 않는 거래처 수정 (404)")
    void updateClient_notFound() throws Exception {
        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Corp")
                .build();
        given(clientCommandService.updateClient(eq(999), any(UpdateClientRequest.class)))
                .willThrow(new ResourceNotFoundException("거래처를 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/clients/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("거래처를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("PATCH /api/clients/{id}/status - 상태 변경 성공")
    void changeStatus_success() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");
        Client inactiveClient = Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.INACTIVE)
                .build();
        given(clientCommandService.changeStatus(eq(1), eq(ClientStatus.INACTIVE))).willReturn(inactiveClient);

        mockMvc.perform(patch("/api/clients/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("INACTIVE"));
    }

    @Test
    @DisplayName("PATCH /api/clients/{id}/status - 동일 상태로 변경 시 (409)")
    void changeStatus_conflict() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("ACTIVE");
        given(clientCommandService.changeStatus(eq(1), eq(ClientStatus.ACTIVE)))
                .willThrow(new IllegalStateException("이미 ACTIVE 상태입니다."));

        mockMvc.perform(patch("/api/clients/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 ACTIVE 상태입니다."));
    }
}
