package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.*;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.domain.repository.*;
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
class ClientIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EntityManager entityManager;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ClientRepository clientRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private PortRepository portRepository;
    @Autowired private PaymentTermRepository paymentTermRepository;
    @Autowired private CurrencyRepository currencyRepository;

    private Country country;
    private Port port;
    private PaymentTerm paymentTerm;
    private Currency currency;

    @BeforeEach
    void setUp() {
        country = countryRepository.save(new Country("US", "United States", "미국"));
        port = portRepository.save(new Port("USNYC", "Port of New York", "New York", country));
        paymentTerm = paymentTermRepository.save(new PaymentTerm("TT", "Telegraphic Transfer", "전신환 송금"));
        currency = currencyRepository.save(new Currency("USD", "US Dollar", "$"));
    }

    // ==================== GET /api/clients ====================

    @Test
    @DisplayName("통합테스트: 거래처 전체 조회 - 빈 목록")
    void getAll_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("통합테스트: 거래처 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .teamId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI002").clientName("Client B").clientNameKr("거래처B")
                .teamId(2).build());

        entityManager.flush();
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].clientCode").value("CLI002"))
                .andExpect(jsonPath("$.content[1].clientCode").value("CLI001"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    // ==================== GET /api/clients/{id} ====================

    @Test
    @DisplayName("통합테스트: 거래처 단건 조회 - 성공")
    void getById_success() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .teamId(1).build());

        entityManager.flush();
        mockMvc.perform(get("/api/clients/{id}", saved.getClientId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientCode").value("CLI001"))
                .andExpect(jsonPath("$.clientName").value("Client A"))
                .andExpect(jsonPath("$.clientNameKr").value("거래처A"));
    }

    @Test
    @DisplayName("통합테스트: 거래처 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/clients/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/clients ====================

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 성공 (전체 필드 포함)")
    void create_success() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .countryId(country.getCountryId())
                .clientCity("New York")
                .portId(port.getPortId())
                .clientAddress("123 Main St")
                .clientTel("010-1234-5678")
                .clientEmail("test@example.com")
                .paymentTermId(paymentTerm.getPaymentTermId())
                .currencyId(currency.getCurrencyId())
                .clientManager("홍길동")
                .teamId(1)
                .clientRegDate(LocalDate.of(2025, 1, 15))
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientCode").value("CLI001"))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.clientNameKr").value("테스트거래처"))
                .andExpect(jsonPath("$.clientCity").value("New York"))
                .andExpect(jsonPath("$.clientAddress").value("123 Main St"))
                .andExpect(jsonPath("$.clientTel").value("010-1234-5678"))
                .andExpect(jsonPath("$.clientEmail").value("test@example.com"))
                .andExpect(jsonPath("$.clientManager").value("홍길동"))
                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.clientStatus").value("ACTIVE"));

        // DB 검증
        List<Client> all = clientRepository.findAll();
        assertThat(all).hasSize(1);
        Client saved = all.get(0);
        assertThat(saved.getClientCode()).isEqualTo("CLI001");
        assertThat(saved.getCountry()).isNotNull();
        assertThat(saved.getCountry().getCountryId()).isEqualTo(country.getCountryId());
        assertThat(saved.getPort()).isNotNull();
        assertThat(saved.getPort().getPortId()).isEqualTo(port.getPortId());
        assertThat(saved.getPaymentTerm()).isNotNull();
        assertThat(saved.getPaymentTerm().getPaymentTermId()).isEqualTo(paymentTerm.getPaymentTermId());
        assertThat(saved.getCurrency()).isNotNull();
        assertThat(saved.getCurrency().getCurrencyId()).isEqualTo(currency.getCurrencyId());
        assertThat(saved.getClientStatus()).isEqualTo(ClientStatus.ACTIVE);
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 중복 거래처 코드")
    void create_duplicateCode() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Existing").clientNameKr("기존")
                .teamId(1).build());

        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("New Client")
                .clientNameKr("새거래처")
                .teamId(2)
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // DB에 1건만 존재
        assertThat(clientRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 존재하지 않는 countryId")
    void create_nonExistentCountryId() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .countryId(9999)
                .teamId(1)
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(clientRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 존재하지 않는 portId")
    void create_nonExistentPortId() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .portId(9999)
                .teamId(1)
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(clientRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 존재하지 않는 paymentTermId")
    void create_nonExistentPaymentTermId() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .paymentTermId(9999)
                .teamId(1)
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(clientRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 존재하지 않는 currencyId")
    void create_nonExistentCurrencyId() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .currencyId(9999)
                .teamId(1)
                .build();

        mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertThat(clientRepository.findAll()).isEmpty();
    }

    // ==================== PUT /api/clients/{id} ====================

    @Test
    @DisplayName("통합테스트: 거래처 수정 - 성공 (FK 변경 포함)")
    void update_success() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Old Name").clientNameKr("이전이름")
                .clientCity("Old City").clientAddress("Old Addr").clientTel("000-0000")
                .clientEmail("old@test.com").clientManager("이전담당자")
                .teamId(1).build());

        Country newCountry = countryRepository.save(new Country("JP", "Japan", "일본"));
        Port newPort = portRepository.save(new Port("JPTYO", "Port of Tokyo", "Tokyo", newCountry));
        PaymentTerm newPt = paymentTermRepository.save(new PaymentTerm("LC", "Letter of Credit", "신용장"));
        Currency newCur = currencyRepository.save(new Currency("JPY", "Japanese Yen", "¥"));

        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Name")
                .clientNameKr("수정이름")
                .countryId(newCountry.getCountryId())
                .clientCity("Tokyo")
                .portId(newPort.getPortId())
                .clientAddress("456 New St")
                .clientTel("010-9999-9999")
                .clientEmail("updated@test.com")
                .paymentTermId(newPt.getPaymentTermId())
                .currencyId(newCur.getCurrencyId())
                .clientManager("새담당자")
                .teamId(2)
                .build();

        mockMvc.perform(put("/api/clients/{id}", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Updated Name"))
                .andExpect(jsonPath("$.clientNameKr").value("수정이름"))
                .andExpect(jsonPath("$.clientCity").value("Tokyo"))
                .andExpect(jsonPath("$.clientAddress").value("456 New St"))
                .andExpect(jsonPath("$.clientTel").value("010-9999-9999"))
                .andExpect(jsonPath("$.clientEmail").value("updated@test.com"))
                .andExpect(jsonPath("$.clientManager").value("새담당자"));

        // DB 검증
        Client updated = clientRepository.findById(saved.getClientId()).orElseThrow();
        assertThat(updated.getClientName()).isEqualTo("Updated Name");
        assertThat(updated.getCountry().getCountryId()).isEqualTo(newCountry.getCountryId());
        assertThat(updated.getPort().getPortId()).isEqualTo(newPort.getPortId());
        assertThat(updated.getPaymentTerm().getPaymentTermId()).isEqualTo(newPt.getPaymentTermId());
        assertThat(updated.getCurrency().getCurrencyId()).isEqualTo(newCur.getCurrencyId());
    }

    @Test
    @DisplayName("통합테스트: 거래처 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Name")
                .build();

        mockMvc.perform(put("/api/clients/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("통합테스트: 거래처 수정 - 존재하지 않는 FK ID")
    void update_nonExistentFkIds() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .teamId(1).build());

        UpdateClientRequest request = UpdateClientRequest.builder()
                .countryId(9999)
                .build();

        mockMvc.perform(put("/api/clients/{id}", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ==================== PATCH /api/clients/{id}/status ====================

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 활성에서 비활성으로")
    void changeStatus_activeToInactive() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.ACTIVE).teamId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("INACTIVE"));

        // DB 검증
        Client updated = clientRepository.findById(saved.getClientId()).orElseThrow();
        assertThat(updated.getClientStatus()).isEqualTo(ClientStatus.INACTIVE);
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 비활성에서 활성으로")
    void changeStatus_inactiveToActive() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.INACTIVE).teamId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("ACTIVE");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("ACTIVE"));

        // DB 검증
        Client updated = clientRepository.findById(saved.getClientId()).orElseThrow();
        assertThat(updated.getClientStatus()).isEqualTo(ClientStatus.ACTIVE);
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 존재하지 않는 ID")
    void changeStatus_notFound() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("INACTIVE");

        mockMvc.perform(patch("/api/clients/{id}/status", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 잘못된 상태값")
    void changeStatus_invalidStatus() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.ACTIVE).teamId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("INVALID");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 동일 상태로 변경 시 실패")
    void changeStatus_sameStatus() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.ACTIVE).teamId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("ACTIVE");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getClientId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // ==================== GET /api/clients/department/{departmentId} ====================

    @Test
    @DisplayName("통합테스트: 부서별 거래처 조회 - 성공")
    void getByDepartment_success() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .teamId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI002").clientName("Client B").clientNameKr("거래처B")
                .teamId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI003").clientName("Client C").clientNameKr("거래처C")
                .teamId(2).build());

        entityManager.flush();
        mockMvc.perform(get("/api/clients/department/{departmentId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("통합테스트: 부서별 거래처 조회 - 빈 결과")
    void getByDepartment_empty() throws Exception {
        entityManager.flush();
        mockMvc.perform(get("/api/clients/department/{departmentId}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
