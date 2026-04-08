package com.team2.master.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.command.application.dto.CreateItemRequest;
import com.team2.master.command.application.dto.UpdateItemRequest;
import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.command.application.controller.ItemCommandController;
import com.team2.master.exception.GlobalExceptionHandler;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.application.service.ItemCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemCommandController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser(roles = "ADMIN")
class ItemCommandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private ItemCommandService itemCommandService;

    private Item createTestItem() {
        Item item = Item.builder()
                .itemCode("ITM001").itemName("Test Product").itemNameKr("테스트 제품")
                .itemSpec("100x200mm").itemUnit("EA")
                .itemUnitPrice(new BigDecimal("1500.00"))
                .itemCategory("전자부품").itemStatus(ItemStatus.ACTIVE)
                .build();
        ReflectionTestUtils.setField(item, "itemId", 1);
        return item;
    }

    @Test
    @DisplayName("POST /api/items - 품목 생성 성공")
    void createItem_success() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001").itemName("Test Product").itemNameKr("테스트 제품").build();
        given(itemCommandService.createItem(any(CreateItemRequest.class))).willReturn(createTestItem());

        mockMvc.perform(post("/api/items").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Test Product"))
                .andExpect(jsonPath("$.itemCode").value("ITM001"));
    }

    @Test
    @DisplayName("POST /api/items - 중복 코드로 생성 실패 (409)")
    void createItem_duplicate() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001").itemName("Test Product").build();
        given(itemCommandService.createItem(any(CreateItemRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 품목 코드입니다: ITM001"));

        mockMvc.perform(post("/api/items").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 품목 코드입니다: ITM001"));
    }

    @Test
    @DisplayName("PUT /api/items/{id} - 품목 수정 성공")
    void updateItem_success() throws Exception {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName("Updated Product").itemNameKr("수정 제품").build();
        Item updatedItem = Item.builder()
                .itemCode("ITM001").itemName("Updated Product").itemNameKr("수정 제품")
                .itemStatus(ItemStatus.ACTIVE).build();
        given(itemCommandService.updateItem(eq(1), any(UpdateItemRequest.class))).willReturn(updatedItem);

        mockMvc.perform(put("/api/items/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Updated Product"));
    }

    @Test
    @DisplayName("PUT /api/items/{id} - 존재하지 않는 품목 수정 (404)")
    void updateItem_notFound() throws Exception {
        UpdateItemRequest request = UpdateItemRequest.builder().itemName("Updated Product").build();
        given(itemCommandService.updateItem(eq(999), any(UpdateItemRequest.class)))
                .willThrow(new ResourceNotFoundException("품목을 찾을 수 없습니다: 999"));

        mockMvc.perform(put("/api/items/999").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("품목을 찾을 수 없습니다: 999"));
    }

    @Test
    @DisplayName("PATCH /api/items/{id}/status - 상태 변경 성공")
    void changeStatus_success() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");
        Item inactiveItem = Item.builder()
                .itemCode("ITM001").itemName("Test Product").itemStatus(ItemStatus.INACTIVE).build();
        given(itemCommandService.changeStatus(eq(1), eq(ItemStatus.INACTIVE))).willReturn(inactiveItem);

        mockMvc.perform(patch("/api/items/1/status").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemStatus").value("INACTIVE"));
    }

    @Test
    @DisplayName("PATCH /api/items/{id}/status - 동일 상태로 변경 시 (409)")
    void changeStatus_conflict() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("ACTIVE");
        given(itemCommandService.changeStatus(eq(1), eq(ItemStatus.ACTIVE)))
                .willThrow(new IllegalStateException("이미 ACTIVE 상태입니다."));

        mockMvc.perform(patch("/api/items/1/status").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 ACTIVE 상태입니다."));
    }
}
