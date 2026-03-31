package com.team2.master.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.CreateItemRequest;
import com.team2.master.dto.UpdateItemRequest;
import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.controller.ItemController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    private Item createTestItem() {
        return Item.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .itemNameKr("테스트 제품")
                .itemSpec("100x200mm")
                .itemUnit("EA")
                .itemUnitPrice(new BigDecimal("1500.00"))
                .itemCategory("전자부품")
                .itemStatus(ItemStatus.활성)
                .build();
    }

    @Test
    @DisplayName("POST /api/items - 품목 생성 성공")
    void createItem_success() throws Exception {
        // given
        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .itemNameKr("테스트 제품")
                .build();
        given(itemService.createItem(any(CreateItemRequest.class))).willReturn(createTestItem());

        // when & then
        mockMvc.perform(post("/api/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Test Product"))
                .andExpect(jsonPath("$.itemCode").value("ITM001"));
    }

    @Test
    @DisplayName("POST /api/items - 중복 코드로 생성 실패 (409)")
    void createItem_duplicate() throws Exception {
        // given
        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .build();
        given(itemService.createItem(any(CreateItemRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 품목 코드입니다: ITM001"));

        // when & then
        mockMvc.perform(post("/api/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 품목 코드입니다: ITM001"));
    }

    @Test
    @DisplayName("GET /api/items - 전체 품목 목록 조회")
    void getAllItems_success() throws Exception {
        // given
        given(itemService.getAllItems()).willReturn(List.of(createTestItem()));

        // when & then
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/items/{id} - 품목 상세 조회")
    void getItem_success() throws Exception {
        // given
        given(itemService.getItem(1)).willReturn(createTestItem());

        // when & then
        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/items/{id} - 존재하지 않는 품목 조회 (404)")
    void getItem_notFound() throws Exception {
        // given
        given(itemService.getItem(999))
                .willThrow(new ResourceNotFoundException("품목을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(get("/api/items/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("품목을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("PUT /api/items/{id} - 품목 수정 성공")
    void updateItem_success() throws Exception {
        // given
        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName("Updated Product")
                .itemNameKr("수정 제품")
                .build();
        Item updatedItem = Item.builder()
                .itemCode("ITM001")
                .itemName("Updated Product")
                .itemNameKr("수정 제품")
                .itemStatus(ItemStatus.활성)
                .build();
        given(itemService.updateItem(eq(1), any(UpdateItemRequest.class))).willReturn(updatedItem);

        // when & then
        mockMvc.perform(put("/api/items/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Updated Product"));
    }

    @Test
    @DisplayName("PUT /api/items/{id} - 존재하지 않는 품목 수정 (404)")
    void updateItem_notFound() throws Exception {
        // given
        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName("Updated Product")
                .build();
        given(itemService.updateItem(eq(999), any(UpdateItemRequest.class)))
                .willThrow(new ResourceNotFoundException("품목을 찾을 수 없습니다: 999"));

        // when & then
        mockMvc.perform(put("/api/items/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("품목을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("PATCH /api/items/{id}/status - 상태 변경 성공")
    void changeStatus_success() throws Exception {
        // given
        ChangeStatusRequest request = new ChangeStatusRequest("비활성");
        Item inactiveItem = Item.builder()
                .itemCode("ITM001")
                .itemName("Test Product")
                .itemStatus(ItemStatus.비활성)
                .build();
        given(itemService.changeStatus(eq(1), eq(ItemStatus.비활성))).willReturn(inactiveItem);

        // when & then
        mockMvc.perform(patch("/api/items/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemStatus").value("비활성"));
    }

    @Test
    @DisplayName("PATCH /api/items/{id}/status - 동일 상태로 변경 시 (409)")
    void changeStatus_conflict() throws Exception {
        // given
        ChangeStatusRequest request = new ChangeStatusRequest("활성");
        given(itemService.changeStatus(eq(1), eq(ItemStatus.활성)))
                .willThrow(new IllegalStateException("이미 활성 상태입니다."));

        // when & then
        mockMvc.perform(patch("/api/items/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 활성 상태입니다."));
    }
}
