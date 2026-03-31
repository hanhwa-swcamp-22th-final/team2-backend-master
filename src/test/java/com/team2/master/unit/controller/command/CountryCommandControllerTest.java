package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateCountryRequest;
import com.team2.master.dto.UpdateCountryRequest;
import com.team2.master.entity.Country;
import com.team2.master.command.controller.CountryCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.service.CountryCommandService;
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

@WebMvcTest(CountryCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class CountryCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private CountryCommandService countryCommandService;

    @Test
    @DisplayName("국가 생성 API 테스트")
    void create() throws Exception {
        CreateCountryRequest request = new CreateCountryRequest("KR", "South Korea", "대한민국");
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryCommandService.create(any(CreateCountryRequest.class))).willReturn(country);

        mockMvc.perform(post("/api/countries").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryCode").value("KR"));
    }

    @Test
    @DisplayName("국가 생성 - 중복 코드 (409)")
    void create_duplicate() throws Exception {
        CreateCountryRequest request = new CreateCountryRequest("KR", "South Korea", "대한민국");
        given(countryCommandService.create(any(CreateCountryRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 국가 코드입니다: KR"));

        mockMvc.perform(post("/api/countries").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 국가 코드입니다: KR"));
    }

    @Test
    @DisplayName("국가 수정 API 테스트")
    void update() throws Exception {
        UpdateCountryRequest request = new UpdateCountryRequest("KOR", "Korea", "한국");
        Country country = new Country("KOR", "Korea", "한국");
        given(countryCommandService.update(eq(1), any(UpdateCountryRequest.class))).willReturn(country);

        mockMvc.perform(put("/api/countries/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("KOR"));
    }

    @Test
    @DisplayName("국가 수정 - 존재하지 않는 국가 (404)")
    void update_notFound() throws Exception {
        UpdateCountryRequest request = new UpdateCountryRequest("KOR", "Korea", "한국");
        given(countryCommandService.update(eq(999), any(UpdateCountryRequest.class)))
                .willThrow(new ResourceNotFoundException("국가를 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/countries/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("국가를 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("국가 삭제 API 테스트")
    void deleteCountry() throws Exception {
        willDoNothing().given(countryCommandService).delete(1);
        mockMvc.perform(delete("/api/countries/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("국가 삭제 - 존재하지 않는 국가 (404)")
    void deleteCountry_notFound() throws Exception {
        willThrow(new ResourceNotFoundException("국가를 찾을 수 없습니다: 999"))
                .given(countryCommandService).delete(999);
        mockMvc.perform(delete("/api/countries/999").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("국가를 찾을 수 없습니다: 999"));
    }
}
