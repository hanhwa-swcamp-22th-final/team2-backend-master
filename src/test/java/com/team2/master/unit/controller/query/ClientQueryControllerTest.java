package com.team2.master.unit.controller.query;

import com.team2.master.dto.ClientResponse;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.query.controller.ClientQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.ClientQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class ClientQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientQueryService clientQueryService;

    private ClientResponse createTestClientResponse() {
        return ClientResponse.builder()
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
    @DisplayName("GET /api/clients - 전체 거래처 목록 조회")
    void getAllClients_success() throws Exception {
        given(clientQueryService.getAllClients()).willReturn(List.of(createTestClientResponse()));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"))
                .andExpect(jsonPath("$[0].clientCode").value("CLI001"))
                .andExpect(jsonPath("$[0].clientStatus").value("ACTIVE"));
    }

    @Test
    @DisplayName("GET /api/clients/{id} - 거래처 상세 조회")
    void getClient_success() throws Exception {
        given(clientQueryService.getClient(1)).willReturn(createTestClientResponse());

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Test Corp"))
                .andExpect(jsonPath("$.clientCode").value("CLI001"));
    }

    @Test
    @DisplayName("GET /api/clients/{id} - 존재하지 않는 거래처 조회 (404)")
    void getClient_notFound() throws Exception {
        given(clientQueryService.getClient(999))
                .willThrow(new ResourceNotFoundException("거래처를 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/clients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("거래처를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("GET /api/clients/department/{departmentId} - 부서별 거래처 조회")
    void getClientsByDepartment_success() throws Exception {
        given(clientQueryService.getClientsByDepartmentId(1)).willReturn(List.of(createTestClientResponse()));

        mockMvc.perform(get("/api/clients/department/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Test Corp"))
                .andExpect(jsonPath("$[0].departmentId").value(1));
    }
}
