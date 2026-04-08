package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.command.application.dto.CreateItemRequest;
import com.team2.master.command.application.dto.UpdateItemRequest;
import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.command.domain.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;
import org.springframework.http.MediaType;
import jakarta.persistence.EntityManager;
import org.springframework.security.test.context.support.WithMockUser;
import jakarta.persistence.EntityManager;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.persistence.EntityManager;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import jakarta.persistence.EntityManager;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import jakarta.persistence.EntityManager;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
class ItemIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ItemRepository itemRepository;

    // ==================== GET /api/items ====================

    @Test
    @DisplayName("통합테스트: 품목 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("통합테스트: 품목 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Item A").itemNameKr("품목A")
                .build());
        itemRepository.save(Item.builder()
                .itemCode("ITM002").itemName("Item B").itemNameKr("품목B")
                .build());

        entityManager.flush();
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].itemCode").value("ITM002"))
                .andExpect(jsonPath("$.content[1].itemCode").value("ITM001"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    // ==================== GET /api/items/{id} ====================

    @Test
    @DisplayName("통합테스트: 품목 단건 조회 - 성공")
    void getById_success() throws Exception {
        Item saved = itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Item A").itemNameKr("품목A")
                .itemSpec("100x50").itemUnit("EA").itemPackUnit("BOX")
                .itemUnitPrice(new BigDecimal("1500.50"))
                .itemWeight(new BigDecimal("2.500"))
                .itemHsCode("8501.10").itemCategory("전자부품")
                .build());

        entityManager.flush();
        mockMvc.perform(get("/api/items/{id}", saved.getItemId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCode").value("ITM001"))
                .andExpect(jsonPath("$.itemName").value("Item A"))
                .andExpect(jsonPath("$.itemNameKr").value("품목A"))
                .andExpect(jsonPath("$.itemSpec").value("100x50"))
                .andExpect(jsonPath("$.itemUnit").value("EA"))
                .andExpect(jsonPath("$.itemPackUnit").value("BOX"));
    }

    @Test
    @DisplayName("통합테스트: 품목 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/items/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/items ====================

    @Test
    @DisplayName("통합테스트: 품목 생성 - 성공 (전체 필드 포함)")
    void create_success() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001")
                .itemName("Test Item")
                .itemNameKr("테스트품목")
                .itemSpec("200x100mm")
                .itemUnit("EA")
                .itemPackUnit("BOX")
                .itemUnitPrice(new BigDecimal("2500.00"))
                .itemWeight(new BigDecimal("1.250"))
                .itemHsCode("8501.10")
                .itemCategory("전자부품")
                .itemRegDate(LocalDate.of(2025, 3, 1))
                .build();

        mockMvc.perform(post("/api/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemCode").value("ITM001"))
                .andExpect(jsonPath("$.itemName").value("Test Item"))
                .andExpect(jsonPath("$.itemNameKr").value("테스트품목"))
                .andExpect(jsonPath("$.itemSpec").value("200x100mm"))
                .andExpect(jsonPath("$.itemUnit").value("EA"))
                .andExpect(jsonPath("$.itemPackUnit").value("BOX"))
                .andExpect(jsonPath("$.itemUnitPrice").value(2500.00))
                .andExpect(jsonPath("$.itemWeight").value(1.250))
                .andExpect(jsonPath("$.itemHsCode").value("8501.10"))
                .andExpect(jsonPath("$.itemCategory").value("전자부품"))
                .andExpect(jsonPath("$.itemStatus").value("ACTIVE"));

        // DB 검증
        List<Item> all = itemRepository.findAll();
        assertThat(all).hasSize(1);
        Item saved = all.get(0);
        assertThat(saved.getItemCode()).isEqualTo("ITM001");
        assertThat(saved.getItemUnitPrice()).isEqualByComparingTo(new BigDecimal("2500.00"));
        assertThat(saved.getItemWeight()).isEqualByComparingTo(new BigDecimal("1.250"));
        assertThat(saved.getItemStatus()).isEqualTo(ItemStatus.ACTIVE);
        assertThat(saved.getItemRegDate()).isEqualTo(LocalDate.of(2025, 3, 1));
    }

    @Test
    @DisplayName("통합테스트: 품목 생성 - 중복 품목 코드")
    void create_duplicateCode() throws Exception {
        itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Existing").itemNameKr("기존품목")
                .build());

        CreateItemRequest request = CreateItemRequest.builder()
                .itemCode("ITM001")
                .itemName("New Item")
                .itemNameKr("새품목")
                .build();

        mockMvc.perform(post("/api/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // DB에 1건만 존재
        assertThat(itemRepository.findAll()).hasSize(1);
    }

    // ==================== PUT /api/items/{id} ====================

    @Test
    @DisplayName("통합테스트: 품목 수정 - 성공")
    void update_success() throws Exception {
        Item saved = itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Old Name").itemNameKr("이전이름")
                .itemSpec("old spec").itemUnit("EA").itemPackUnit("BOX")
                .itemUnitPrice(new BigDecimal("1000.00"))
                .itemWeight(new BigDecimal("1.000"))
                .itemHsCode("0000.00").itemCategory("기타")
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName("Updated Name")
                .itemNameKr("수정이름")
                .itemSpec("new spec 300x200")
                .itemUnit("KG")
                .itemPackUnit("PALLET")
                .itemUnitPrice(new BigDecimal("5000.00"))
                .itemWeight(new BigDecimal("3.500"))
                .itemHsCode("9999.99")
                .itemCategory("기계부품")
                .build();

        mockMvc.perform(put("/api/items/{id}", saved.getItemId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Updated Name"))
                .andExpect(jsonPath("$.itemNameKr").value("수정이름"))
                .andExpect(jsonPath("$.itemSpec").value("new spec 300x200"))
                .andExpect(jsonPath("$.itemUnit").value("KG"))
                .andExpect(jsonPath("$.itemPackUnit").value("PALLET"))
                .andExpect(jsonPath("$.itemUnitPrice").value(5000.00))
                .andExpect(jsonPath("$.itemWeight").value(3.500))
                .andExpect(jsonPath("$.itemHsCode").value("9999.99"))
                .andExpect(jsonPath("$.itemCategory").value("기계부품"));

        // DB 검증
        Item updated = itemRepository.findById(saved.getItemId()).orElseThrow();
        assertThat(updated.getItemName()).isEqualTo("Updated Name");
        assertThat(updated.getItemUnitPrice()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(updated.getItemWeight()).isEqualByComparingTo(new BigDecimal("3.500"));
    }

    @Test
    @DisplayName("통합테스트: 품목 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName("Updated Name")
                .build();

        mockMvc.perform(put("/api/items/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== PATCH /api/items/{id}/status ====================

    @Test
    @DisplayName("통합테스트: 품목 상태 변경 - 활성에서 비활성으로")
    void changeStatus_activeToInactive() throws Exception {
        Item saved = itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Item").itemNameKr("품목")
                .itemStatus(ItemStatus.ACTIVE).build());

        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");

        mockMvc.perform(patch("/api/items/{id}/status", saved.getItemId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemStatus").value("INACTIVE"));

        // DB 검증
        Item updated = itemRepository.findById(saved.getItemId()).orElseThrow();
        assertThat(updated.getItemStatus()).isEqualTo(ItemStatus.INACTIVE);
    }

    @Test
    @DisplayName("통합테스트: 품목 상태 변경 - 존재하지 않는 ID")
    void changeStatus_notFound() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");

        mockMvc.perform(patch("/api/items/{id}/status", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("통합테스트: 품목 상태 변경 - 동일 상태로 변경 시 실패")
    void changeStatus_sameStatus() throws Exception {
        Item saved = itemRepository.save(Item.builder()
                .itemCode("ITM001").itemName("Item").itemNameKr("품목")
                .itemStatus(ItemStatus.ACTIVE).build());

        ChangeStatusRequest request = new ChangeStatusRequest("ACTIVE");

        mockMvc.perform(patch("/api/items/{id}/status", saved.getItemId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
