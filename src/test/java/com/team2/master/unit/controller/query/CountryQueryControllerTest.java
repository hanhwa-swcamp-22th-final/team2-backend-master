package com.team2.master.unit.controller.query;

import com.team2.master.command.domain.entity.Country;
import com.team2.master.query.controller.CountryQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.CountryQueryService;
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

@WebMvcTest(CountryQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class CountryQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private CountryQueryService countryQueryService;

    @Test
    @DisplayName("전체 국가 목록 조회 API 테스트")
    void getAll() throws Exception {
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryQueryService.getAll()).willReturn(List.of(country));

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.countryList[0].countryCode").value("KR"))
                .andExpect(jsonPath("$._embedded.countryList[0].countryName").value("South Korea"));
    }

    @Test
    @DisplayName("국가 ID로 조회 API 테스트")
    void getById() throws Exception {
        Country country = new Country("KR", "South Korea", "대한민국");
        given(countryQueryService.getById(1)).willReturn(country);

        mockMvc.perform(get("/api/countries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("KR"));
    }

    @Test
    @DisplayName("국가 ID로 조회 - 존재하지 않는 국가 (404)")
    void getById_notFound() throws Exception {
        given(countryQueryService.getById(999))
                .willThrow(new ResourceNotFoundException("국가를 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/countries/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("국가를 찾을 수 없습니다: 999"));
    }
}
