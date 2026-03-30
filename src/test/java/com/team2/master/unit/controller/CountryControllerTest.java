package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.CreateCountryRequest;
import com.team2.master.dto.UpdateCountryRequest;
import com.team2.master.entity.Country;
import com.team2.master.controller.CountryController;
import com.team2.master.service.CountryService;
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

@WebMvcTest(CountryController.class)
@WithMockUser
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CountryService countryService;

    @Test
    @DisplayName("전체 국가 목록 조회 API 테스트")
    void getAll() throws Exception {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryService.getAll()).willReturn(List.of(country));

        // when & then
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode").value("KR"))
                .andExpect(jsonPath("$[0].countryName").value("South Korea"));
    }

    @Test
    @DisplayName("국가 ID로 조회 API 테스트")
    void getById() throws Exception {
        // given
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryService.getById(1)).willReturn(country);

        // when & then
        mockMvc.perform(get("/api/countries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("KR"));
    }

    @Test
    @DisplayName("국가 생성 API 테스트")
    void create() throws Exception {
        // given
        CreateCountryRequest request = new CreateCountryRequest("KR", "South Korea", "대한민국");
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryService.create(any(CreateCountryRequest.class))).willReturn(country);

        // when & then
        mockMvc.perform(post("/api/countries")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryCode").value("KR"));
    }

    @Test
    @DisplayName("국가 수정 API 테스트")
    void update() throws Exception {
        // given
        UpdateCountryRequest request = new UpdateCountryRequest("KOR", "Korea", "한국");
        Country country = new Country("KOR", "Korea", "한국");
        given(countryService.update(eq(1), any(UpdateCountryRequest.class))).willReturn(country);

        // when & then
        mockMvc.perform(put("/api/countries/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("KOR"));
    }

    @Test
    @DisplayName("국가 삭제 API 테스트")
    void deleteCountry() throws Exception {
        // given
        willDoNothing().given(countryService).delete(1);

        // when & then
        mockMvc.perform(delete("/api/countries/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
