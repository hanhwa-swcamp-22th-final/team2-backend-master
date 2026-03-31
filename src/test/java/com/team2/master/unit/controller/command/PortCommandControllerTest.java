package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.PortResponse;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.command.controller.PortCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.service.PortCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class PortCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private PortCommandService portCommandService;

    private PortResponse createTestPortResponse() {
        return PortResponse.builder()
                .portCode("KRPUS").portName("Busan Port").portCity("Busan")
                .countryName("South Korea").build();
    }

    @Test
    @DisplayName("항구 생성 API 테스트")
    void create() throws Exception {
        CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
        given(portCommandService.create(any(CreatePortRequest.class))).willReturn(createTestPortResponse());

        mockMvc.perform(post("/api/ports").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.portCode").value("KRPUS"))
                .andExpect(jsonPath("$.countryName").value("South Korea"));
    }

    @Test
    @DisplayName("항구 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
        given(portCommandService.create(any(CreatePortRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 항구 코드입니다: KRPUS"));

        mockMvc.perform(post("/api/ports").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 항구 코드입니다: KRPUS"));
    }

    @Test
    @DisplayName("항구 수정 API 테스트")
    void update() throws Exception {
        UpdatePortRequest request = new UpdatePortRequest("KRPUS2", "Busan New Port", "Busan City", 1);
        PortResponse response = PortResponse.builder()
                .portCode("KRPUS2").portName("Busan New Port").portCity("Busan City")
                .countryName("South Korea").build();
        given(portCommandService.update(eq(1), any(UpdatePortRequest.class))).willReturn(response);

        mockMvc.perform(put("/api/ports/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS2"))
                .andExpect(jsonPath("$.portName").value("Busan New Port"));
    }

    @Test
    @DisplayName("항구 수정 - 존재하지 않는 항구 (404)")
    void update_notFound() throws Exception {
        UpdatePortRequest request = new UpdatePortRequest("KRPUS2", "Busan New Port", "Busan City", 1);
        given(portCommandService.update(eq(999), any(UpdatePortRequest.class)))
                .willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/ports/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("항구 삭제 API 테스트")
    void deletePort() throws Exception {
        willDoNothing().given(portCommandService).delete(1);
        mockMvc.perform(delete("/api/ports/1").with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("항구 삭제 - 존재하지 않는 항구 (404)")
    void deletePort_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("항구를 찾을 수 없습니다: 999"))
                .given(portCommandService).delete(999);
        mockMvc.perform(delete("/api/ports/999").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("항구를 찾을 수 없습니다: 999"));
    }
}
