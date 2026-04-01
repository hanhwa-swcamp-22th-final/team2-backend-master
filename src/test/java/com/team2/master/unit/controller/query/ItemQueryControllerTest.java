package com.team2.master.unit.controller.query;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.query.controller.ItemQueryController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.service.ItemQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemQueryController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class ItemQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ItemQueryService itemQueryService;

    private Item createTestItem() {
        return Item.builder()
                .itemCode("ITM001").itemName("Test Product").itemNameKr("테스트 제품")
                .itemSpec("100x200mm").itemUnit("EA")
                .itemUnitPrice(new BigDecimal("1500.00"))
                .itemCategory("전자부품").itemStatus(ItemStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("GET /api/items - 전체 품목 목록 조회")
    void getAllItems_success() throws Exception {
        given(itemQueryService.getAllItems()).willReturn(List.of(createTestItem()));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/items/{id} - 품목 상세 조회")
    void getItem_success() throws Exception {
        given(itemQueryService.getItem(1)).willReturn(createTestItem());

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/items/{id} - 존재하지 않는 품목 조회 (404)")
    void getItem_notFound() throws Exception {
        given(itemQueryService.getItem(999))
                .willThrow(new ResourceNotFoundException("품목을 찾을 수 없습니다: 999"));

        mockMvc.perform(get("/api/items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("품목을 찾을 수 없습니다: 999"));
    }
}
