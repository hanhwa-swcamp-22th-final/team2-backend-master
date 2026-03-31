package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.command.controller.IncotermCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.service.IncotermCommandService;
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

@WebMvcTest(IncotermCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class IncotermCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private IncotermCommandService incotermCommandService;

    @Test
    @DisplayName("인코텀 생성 API 테스트")
    void create() throws Exception {
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도", "desc", "Sea", "E", "Port");
        given(incotermCommandService.create(any(CreateIncotermRequest.class))).willReturn(incoterm);

        mockMvc.perform(post("/api/incoterms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incotermCode").value("FOB"));
    }

    @Test
    @DisplayName("인코텀 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        given(incotermCommandService.create(any(CreateIncotermRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 인코텀 코드입니다: FOB"));

        mockMvc.perform(post("/api/incoterms").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 인코텀 코드입니다: FOB"));
    }

    @Test
    @DisplayName("인코텀 수정 API 테스트")
    void update() throws Exception {
        UpdateIncotermRequest request = new UpdateIncotermRequest("FOB", "FOB Updated", "본선인도수정",
                null, null, null, null);
        Incoterm incoterm = new Incoterm("FOB", "FOB Updated", "본선인도수정", null, null, null, null);
        given(incotermCommandService.update(eq(1), any(UpdateIncotermRequest.class))).willReturn(incoterm);

        mockMvc.perform(put("/api/incoterms/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermName").value("FOB Updated"));
    }

    @Test
    @DisplayName("인코텀 수정 - 존재하지 않는 인코텀 (404)")
    void update_notFound() throws Exception {
        UpdateIncotermRequest request = new UpdateIncotermRequest("FOB", "FOB Updated", "본선인도수정",
                null, null, null, null);
        given(incotermCommandService.update(eq(999), any(UpdateIncotermRequest.class)))
                .willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/incoterms/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("인코텀 삭제 API 테스트")
    void deleteIncoterm() throws Exception {
        willDoNothing().given(incotermCommandService).delete(1);
        mockMvc.perform(delete("/api/incoterms/1").with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("인코텀 삭제 - 존재하지 않는 인코텀 (404)")
    void deleteIncoterm_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"))
                .given(incotermCommandService).delete(999);
        mockMvc.perform(delete("/api/incoterms/999").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }
}
