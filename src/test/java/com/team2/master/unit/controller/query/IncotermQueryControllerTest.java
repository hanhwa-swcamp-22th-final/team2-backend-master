package com.team2.master.unit.controller.query;

import com.team2.master.entity.Incoterm;
import com.team2.master.query.controller.IncotermQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.IncotermQueryService;
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

@WebMvcTest(IncotermQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class IncotermQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private IncotermQueryService incotermQueryService;

    @Test
    @DisplayName("전체 인코텀 목록 조회 API 테스트")
    void getAll() throws Exception {
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도", null, "Sea", null, null);
        given(incotermQueryService.getAll()).willReturn(List.of(incoterm));

        mockMvc.perform(get("/api/incoterms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].incotermCode").value("FOB"))
                .andExpect(jsonPath("$[0].incotermName").value("Free On Board"));
    }

    @Test
    @DisplayName("인코텀 ID로 조회 API 테스트")
    void getById() throws Exception {
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도", null, null, null, null);
        given(incotermQueryService.getById(1)).willReturn(incoterm);

        mockMvc.perform(get("/api/incoterms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incotermCode").value("FOB"));
    }

    @Test
    @DisplayName("인코텀 ID로 조회 - 존재하지 않는 인코텀 (404)")
    void getById_notFound() throws Exception {
        given(incotermQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("인코텀을 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/incoterms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("인코텀을 찾을 수 없습니다: 999"));
    }
}
