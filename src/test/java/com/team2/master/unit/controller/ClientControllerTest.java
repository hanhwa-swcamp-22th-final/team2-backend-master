package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.controller.ClientController;
import com.team2.master.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@WithMockUser
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    private Client createTestClient() {
        return Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .clientCity("Seoul")
                .clientEmail("test@corp.com")
                .clientManager("홍길동")
                .departmentId(1)
                .clientStatus(ClientStatus.활성)
                .clientRegDate(LocalDate.of(2025, 1, 1))
                .build();
    }

    @Test
    @DisplayName("POST /api/clients - 거래처 생성 성공")
    void createClient_success() throws Exception {
        // given
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientNameKr("테스트 주식회사")
                .build();
        given(clientService.createClient(any(CreateClientRequest.class))).willReturn(createTestClient());

        // when & then
        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value("Test Corp"))
                .andExpect(jsonPath("$.clientCode").value("CLI001"));
    }

    @Test
    @DisplayName("GET /api/clients - 전체 거래처 목록 조회")
    void getAllClients_success() throws Exception {
        // given
        given(clientService.getAllClients()).willReturn(List.of(createTestClient()));

        // when & then
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("GET /api/clients/{id} - 거래처 상세 조회")
    void getClient_success() throws Exception {
        // given
        given(clientService.getClient(1)).willReturn(createTestClient());

        // when & then
        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Test Corp"));
    }

    @Test
    @DisplayName("PUT /api/clients/{id} - 거래처 수정 성공")
    void updateClient_success() throws Exception {
        // given
        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Corp")
                .clientNameKr("수정 주식회사")
                .build();
        Client updatedClient = Client.builder()
                .clientCode("CLI001")
                .clientName("Updated Corp")
                .clientNameKr("수정 주식회사")
                .clientStatus(ClientStatus.활성)
                .build();
        given(clientService.updateClient(eq(1), any(UpdateClientRequest.class))).willReturn(updatedClient);

        // when & then
        mockMvc.perform(put("/api/clients/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Updated Corp"));
    }

    @Test
    @DisplayName("PATCH /api/clients/{id}/status - 상태 변경 성공")
    void changeStatus_success() throws Exception {
        // given
        ChangeStatusRequest request = new ChangeStatusRequest("비활성");
        Client inactiveClient = Client.builder()
                .clientCode("CLI001")
                .clientName("Test Corp")
                .clientStatus(ClientStatus.비활성)
                .build();
        given(clientService.changeStatus(eq(1), eq(ClientStatus.비활성))).willReturn(inactiveClient);

        // when & then
        mockMvc.perform(patch("/api/clients/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("비활성"));
    }

    @Test
    @DisplayName("GET /api/clients/department/{departmentId} - 부서별 거래처 조회 (RBAC)")
    void getClientsByDepartment_success() throws Exception {
        // given
        given(clientService.getClientsByDepartmentId(1)).willReturn(List.of(createTestClient()));

        // when & then
        mockMvc.perform(get("/api/clients/department/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"))
                .andExpect(jsonPath("$[0].departmentId").value(1));
    }
}
