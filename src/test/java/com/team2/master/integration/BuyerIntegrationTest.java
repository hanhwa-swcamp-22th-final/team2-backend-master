package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.domain.repository.BuyerRepository;
import com.team2.master.command.domain.repository.ClientRepository;
import com.team2.master.command.domain.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.Optional;

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
@WithMockUser
class BuyerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BuyerRepository buyerRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private CountryRepository countryRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        countryRepository.save(new Country("US", "United States", "미국"));
        client = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Test Client").clientNameKr("테스트거래처")
                .departmentId(1).build());
    }

    // ==================== GET /api/buyers ====================

    @Test
    @DisplayName("통합테스트: 바이어 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    @DisplayName("통합테스트: 바이어 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Buyer A").buyerPosition("Manager")
                .buyerEmail("a@test.com").buyerTel("010-1111-1111").build());
        buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Buyer B").buyerPosition("Director")
                .buyerEmail("b@test.com").buyerTel("010-2222-2222").build());

        entityManager.flush();
        mockMvc.perform(get("/api/buyers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.buyerResponseList.length()").value(2))
                .andExpect(jsonPath("$._embedded.buyerResponseList[0].buyerName").value("Buyer A"))
                .andExpect(jsonPath("$._embedded.buyerResponseList[1].buyerName").value("Buyer B"));
    }

    // ==================== GET /api/buyers/{id} ====================

    @Test
    @DisplayName("통합테스트: 바이어 단건 조회 - 성공")
    void getById_success() throws Exception {
        Buyer saved = buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Buyer A").buyerPosition("Manager")
                .buyerEmail("a@test.com").buyerTel("010-1111-1111").build());

        entityManager.flush();
        mockMvc.perform(get("/api/buyers/{id}", saved.getBuyerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("Buyer A"))
                .andExpect(jsonPath("$.buyerPosition").value("Manager"))
                .andExpect(jsonPath("$.buyerEmail").value("a@test.com"))
                .andExpect(jsonPath("$.buyerTel").value("010-1111-1111"));
    }

    @Test
    @DisplayName("통합테스트: 바이어 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/buyers/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== GET /api/buyers/client/{clientId} ====================

    @Test
    @DisplayName("통합테스트: 거래처별 바이어 조회 - 성공")
    void getByClientId_success() throws Exception {
        buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Buyer A").buyerPosition("Manager")
                .buyerEmail("a@test.com").buyerTel("010-1111-1111").build());
        buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Buyer B").buyerPosition("Director")
                .buyerEmail("b@test.com").buyerTel("010-2222-2222").build());

        // 다른 거래처의 바이어
        Client otherClient = clientRepository.save(Client.builder()
                .clientCode("CLI002").clientName("Other Client").clientNameKr("다른거래처")
                .departmentId(2).build());
        buyerRepository.save(Buyer.builder()
                .client(otherClient).buyerName("Buyer C").buyerPosition("Staff")
                .buyerEmail("c@test.com").buyerTel("010-3333-3333").build());

        entityManager.flush();
        mockMvc.perform(get("/api/buyers/client/{clientId}", client.getClientId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.buyerResponseList.length()").value(2))
                .andExpect(jsonPath("$._embedded.buyerResponseList[0].buyerName").value("Buyer A"))
                .andExpect(jsonPath("$._embedded.buyerResponseList[1].buyerName").value("Buyer B"));
    }

    @Test
    @DisplayName("통합테스트: 거래처별 바이어 조회 - 빈 결과")
    void getByClientId_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/buyers/client/{clientId}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    // ==================== POST /api/buyers ====================

    @Test
    @DisplayName("통합테스트: 바이어 생성 - 성공")
    void create_success() throws Exception {
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(client.getClientId())
                .buyerName("New Buyer")
                .buyerPosition("CEO")
                .buyerEmail("new@test.com")
                .buyerTel("010-9999-9999")
                .build();

        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buyerName").value("New Buyer"))
                .andExpect(jsonPath("$.buyerPosition").value("CEO"))
                .andExpect(jsonPath("$.buyerEmail").value("new@test.com"))
                .andExpect(jsonPath("$.buyerTel").value("010-9999-9999"));

        // DB 검증
        List<Buyer> all = buyerRepository.findAll();
        assertThat(all).hasSize(1);
        Buyer saved = all.get(0);
        assertThat(saved.getBuyerName()).isEqualTo("New Buyer");
        assertThat(saved.getClient().getClientId()).isEqualTo(client.getClientId());
    }

    @Test
    @DisplayName("통합테스트: 바이어 생성 - 존재하지 않는 clientId")
    void create_nonExistentClientId() throws Exception {
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .clientId(9999)
                .buyerName("New Buyer")
                .buyerPosition("CEO")
                .buyerEmail("new@test.com")
                .buyerTel("010-9999-9999")
                .build();

        mockMvc.perform(post("/api/buyers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(buyerRepository.findAll()).isEmpty();
    }

    // ==================== PUT /api/buyers/{id} ====================

    @Test
    @DisplayName("통합테스트: 바이어 수정 - 성공")
    void update_success() throws Exception {
        Buyer saved = buyerRepository.save(Buyer.builder()
                .client(client).buyerName("Old Name").buyerPosition("Staff")
                .buyerEmail("old@test.com").buyerTel("010-0000-0000").build());

        UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                .buyerName("Updated Name")
                .buyerPosition("Director")
                .buyerEmail("updated@test.com")
                .buyerTel("010-8888-8888")
                .build();

        mockMvc.perform(put("/api/buyers/{id}", saved.getBuyerId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("Updated Name"))
                .andExpect(jsonPath("$.buyerPosition").value("Director"))
                .andExpect(jsonPath("$.buyerEmail").value("updated@test.com"))
                .andExpect(jsonPath("$.buyerTel").value("010-8888-8888"));

        // DB 검증
        Buyer updated = buyerRepository.findById(saved.getBuyerId()).orElseThrow();
        assertThat(updated.getBuyerName()).isEqualTo("Updated Name");
        assertThat(updated.getBuyerPosition()).isEqualTo("Director");
        assertThat(updated.getBuyerEmail()).isEqualTo("updated@test.com");
        assertThat(updated.getBuyerTel()).isEqualTo("010-8888-8888");
    }

    @Test
    @DisplayName("통합테스트: 바이어 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        UpdateBuyerRequest request = UpdateBuyerRequest.builder()
                .buyerName("Updated Name")
                .build();

        mockMvc.perform(put("/api/buyers/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /api/buyers/{id} ====================

    @Test
    @DisplayName("통합테스트: 바이어 삭제 - 성공")
    void delete_success() throws Exception {
        Buyer saved = buyerRepository.save(Buyer.builder()
                .client(client).buyerName("To Delete").buyerPosition("Staff")
                .buyerEmail("delete@test.com").buyerTel("010-0000-0000").build());

        mockMvc.perform(delete("/api/buyers/{id}", saved.getBuyerId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // DB 검증 - 완전히 삭제되었는지 확인
        Optional<Buyer> deleted = buyerRepository.findById(saved.getBuyerId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 바이어 삭제 - 존재하지 않는 ID")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/buyers/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
