package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.PortResponse;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.controller.PortController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.service.PortCommandService;
import com.team2.master.service.PortQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PortControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PortCommandService portCommandService;

    @MockitoBean
    private PortQueryService portQueryService;

    private PortResponse createTestPortResponse() {
        return PortResponse.builder()
                .portCode("KRPUS")
                .portName("Busan Port")
                .portCity("Busan")
                .countryName("South Korea")
                .build();
    }

    @Test
    @DisplayName("전체 항구 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        given(portQueryService.getAll()).willReturn(List.of(createTestPortResponse()));

        // when & then
        mockMvc.perform(get("/api/ports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].portCode").value("KRPUS"))
                .andExpect(jsonPath("$[0].portName").value("Busan Port"))
                .andExpect(jsonPath("$[0].portCity").value("Busan"))
                .andExpect(jsonPath("$[0].countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        given(portQueryService.getById(1)).willReturn(createTestPortResponse());

        // when & then
        mockMvc.perform(get("/api/ports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.portName").value("Busan Port"))
                .andExpect(jsonPath("$.countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 ID로 조회 - 존재하지 않는 항구 (404)")
    void getById_notFound() throws Exception {
        // given
        given(portQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/ports/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("항구 생성 API 테스트")
    void create() throws Exception {
        // given
        CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        given(portCommandService.create(any(CreatePortRequest.class))).willReturn(port);

        // when & then
        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        // given
        CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
        given(portCommandService.create(any(CreatePortRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 항구 코드입니다: KRPUS"));

        // when & then
        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 항구 코드입니다: KRPUS"));
    }

    @Test
    @DisplayName("항구 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdatePortRequest request = new UpdatePortRequest("KRPUS2", "Busan New Port", "Busan City", 1);
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS2", "Busan New Port", "Busan City", country);
        given(portCommandService.update(eq(1), any(UpdatePortRequest.class))).willReturn(port);

        // when & then
        mockMvc.perform(put("/api/ports/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS2"))
                .andExpect(jsonPath("$.portName").value("Busan New Port"));
    }

    @Test
    @DisplayName("항구 수정 - 존재하지 않는 항구 (404)")
    void update_notFound() throws Exception {
        // given
        UpdatePortRequest request = new UpdatePortRequest("KRPUS2", "Busan New Port", "Busan City", 1);
        given(portCommandService.update(eq(999), any(UpdatePortRequest.class)))
                .willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/ports/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("항구 삭제 API 테스트")
    void deletePort() throws Exception {
        // given
        willDoNothing().given(portCommandService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/ports/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("항구 삭제 - 존재하지 않는 항구 (404)")
    void deletePort_notFound() throws Exception {
        // given
        willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"))
                .given(portCommandService).delete(999);

        // when & then
        mockMvc.perform(delete("/api/ports/999")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }
}
