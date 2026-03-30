package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.controller.PortController;
import com.team2.master.service.PortService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortController.class)
@WithMockUser
class PortControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PortService portService;

    @Test
    @DisplayName("전체 항구 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        given(portService.getAll()).willReturn(List.of(port));

        // when & then
        mockMvc.perform(get("/api/ports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].portCode").value("KRPUS"))
                .andExpect(jsonPath("$[0].portName").value("Busan Port"));
    }

    @Test
    @DisplayName("항구 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        given(portService.getById(1)).willReturn(port);

        // when & then
        mockMvc.perform(get("/api/ports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS"));
    }

    @Test
    @DisplayName("항구 생성 API 테스트")
    void create() throws Exception {
        // given
        CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS", "Busan Port", "Busan", country);
        given(portService.create(any(CreatePortRequest.class))).willReturn(port);

        // when & then
        mockMvc.perform(post("/api/ports")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.portCode").value("KRPUS"));
    }

    @Test
    @DisplayName("항구 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdatePortRequest request = new UpdatePortRequest("KRPUS2", "Busan New Port", "Busan City", 1);
        Country country = new Country("KR", "South Korea", "대한민국");
        Port port = new Port("KRPUS2", "Busan New Port", "Busan City", country);
        given(portService.update(eq(1), any(UpdatePortRequest.class))).willReturn(port);

        // when & then
        mockMvc.perform(put("/api/ports/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portCode").value("KRPUS2"));
    }

    @Test
    @DisplayName("항구 삭제 API 테스트")
    void deletePort() throws Exception {
        // given
        willDoNothing().given(portService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/ports/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
