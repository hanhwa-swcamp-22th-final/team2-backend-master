package com.team2.master.unit.controller.query;

import com.team2.master.query.dto.PortResponse;
import com.team2.master.query.controller.PortQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.PortQueryService;
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

@WebMvcTest(PortQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PortQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PortQueryService portQueryService;

    private PortResponse createTestPortResponse() {
        return PortResponse.builder()
                .portCode("KRPUS").portName("Busan Port").portCity("Busan")
                .countryName("South Korea").build();
    }

    @Test
    @DisplayName("전체 항구 목록 조회 API 테스트")
    void getAll() throws Exception {
        given(portQueryService.getAll()).willReturn(List.of(createTestPortResponse()));

        mockMvc.perform(get("/api/ports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.portResponseList[0].portCode").value("KRPUS"))
                .andExpect(jsonPath("$._embedded.portResponseList[0].portName").value("Busan Port"))
                .andExpect(jsonPath("$._embedded.portResponseList[0].countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 ID로 조회 API 테스트")
    void getById() throws Exception {
        given(portQueryService.getById(1)).willReturn(createTestPortResponse());

        mockMvc.perform(get("/api/ports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 ID로 조회 - 존재하지 않는 항구 (404)")
    void getById_notFound() throws Exception {
        given(portQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/ports/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }
}
