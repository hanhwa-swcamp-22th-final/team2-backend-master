package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.controller.IncotermController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.service.IncotermService;
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

@WebMvcTest(IncotermController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class IncotermControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IncotermService incotermService;

    @Test
    @DisplayName("전체 인코텀 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, "Sea", null, null);
        given(incotermService.getAll()).willReturn(List.of(incoterm));

        // when & then
        mockMvc.perform(get("/api/incoterms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].incotermCode").value("FOB"))
                .andExpect(jsonPath("$[0].incotermName").value("Free On Board"));
    }

    @Test
    @DisplayName("인코텀 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);
        given(incotermService.getById(1)).willReturn(incoterm);

        // when & then
        mockMvc.perform(get("/api/incoterms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermCode").value("FOB"));
    }

    @Test
    @DisplayName("인코텀 ID로 조회 - 존재하지 않는 인코텀 (404)")
    void getById_notFound() throws Exception {
        // given
        given(incotermService.getById(999))
                .willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/incoterms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("인코텀 생성 API 테스트")
    void create() throws Exception {
        // given
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        given(incotermService.create(any(CreateIncotermRequest.class))).willReturn(incoterm);

        // when & then
        mockMvc.perform(post("/api/incoterms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incotermCode").value("FOB"));
    }

    @Test
    @DisplayName("인코텀 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        // given
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        given(incotermService.create(any(CreateIncotermRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 인코텀 코드입니다: FOB"));

        // when & then
        mockMvc.perform(post("/api/incoterms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 인코텀 코드입니다: FOB"));
    }

    @Test
    @DisplayName("인코텀 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdateIncotermRequest request = new UpdateIncotermRequest("FOB", "FOB Updated", "본선인도수정",
                null, null, null, null);
        Incoterm incoterm = new Incoterm("FOB", "FOB Updated", "본선인도수정",
                null, null, null, null);
        given(incotermService.update(eq(1), any(UpdateIncotermRequest.class))).willReturn(incoterm);

        // when & then
        mockMvc.perform(put("/api/incoterms/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermName").value("FOB Updated"));
    }

    @Test
    @DisplayName("인코텀 수정 - 존재하지 않는 인코텀 (404)")
    void update_notFound() throws Exception {
        // given
        UpdateIncotermRequest request = new UpdateIncotermRequest("FOB", "FOB Updated", "본선인도수정",
                null, null, null, null);
        given(incotermService.update(eq(999), any(UpdateIncotermRequest.class)))
                .willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/incoterms/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("인코텀 삭제 API 테스트")
    void deleteIncoterm() throws Exception {
        // given
        willDoNothing().given(incotermService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/incoterms/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("인코텀 삭제 - 존재하지 않는 인코텀 (404)")
    void deleteIncoterm_notFound() throws Exception {
        // given
        willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"))
                .given(incotermService).delete(999);

        // when & then
        mockMvc.perform(delete("/api/incoterms/999")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }
}
