package com.team2.master.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.*;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.repository.*;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class ClientIntegrationTest {

    @Autowired private MockMvc mockMvc;
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
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("통합테스트: 거래처 전체 조회 - 데이터 있음")
    void getAll_withData() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .departmentId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI002").clientName("Client B").clientNameKr("거래처B")
                .departmentId(2).build());

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientCode").value("CLI001"))
                .andExpect(jsonPath("$[1].clientCode").value("CLI002"));
    }

    // ==================== GET /api/clients/{id} ====================

    @Test
    @DisplayName("통합테스트: 거래처 단건 조회 - 성공")
    void getById_success() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .departmentId(1).build());

        mockMvc.perform(get("/api/clients/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientCode").value("CLI001"))
                .andExpect(jsonPath("$.clientName").value("Client A"))
                .andExpect(jsonPath("$.clientNameKr").value("거래처A"));
    }

    @Test
    @DisplayName("통합테스트: 거래처 단건 조회 - 존재하지 않는 ID")
    void getById_notFound() throws Exception {
        assertThatThrownBy(() ->
                mockMvc.perform(get("/api/clients/{id}", 9999))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    // ==================== POST /api/clients ====================

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 성공 (전체 필드 포함)")
    void create_success() throws Exception {
        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("Test Client")
                .clientNameKr("테스트거래처")
                .countryId(country.getId())
                .clientCity("New York")
                .portId(port.getId())
                .clientAddress("123 Main St")
                .clientTel("010-1234-5678")
                .clientEmail("test@example.com")
                .paymentTermId(paymentTerm.getId())
                .currencyId(currency.getId())
                .clientManager("홍길동")
                .departmentId(1)
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
                .andExpect(jsonPath("$.clientStatus").value("활성"));

        // DB 검증
        List<Client> all = clientRepository.findAll();
        assertThat(all).hasSize(1);
        Client saved = all.get(0);
        assertThat(saved.getClientCode()).isEqualTo("CLI001");
        assertThat(saved.getCountry()).isNotNull();
        assertThat(saved.getCountry().getId()).isEqualTo(country.getId());
        assertThat(saved.getPort()).isNotNull();
        assertThat(saved.getPort().getId()).isEqualTo(port.getId());
        assertThat(saved.getPaymentTerm()).isNotNull();
        assertThat(saved.getPaymentTerm().getId()).isEqualTo(paymentTerm.getId());
        assertThat(saved.getCurrency()).isNotNull();
        assertThat(saved.getCurrency().getId()).isEqualTo(currency.getId());
        assertThat(saved.getClientStatus()).isEqualTo(ClientStatus.활성);
    }

    @Test
    @DisplayName("통합테스트: 거래처 생성 - 중복 거래처 코드")
    void create_duplicateCode() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Existing").clientNameKr("기존")
                .departmentId(1).build());

        CreateClientRequest request = CreateClientRequest.builder()
                .clientCode("CLI001")
                .clientName("New Client")
                .clientNameKr("새거래처")
                .departmentId(2)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

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
                .departmentId(1)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

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
                .departmentId(1)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

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
                .departmentId(1)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

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
                .departmentId(1)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(post("/api/clients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

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
                .departmentId(1).build());

        Country newCountry = countryRepository.save(new Country("JP", "Japan", "일본"));
        Port newPort = portRepository.save(new Port("JPTYO", "Port of Tokyo", "Tokyo", newCountry));
        PaymentTerm newPt = paymentTermRepository.save(new PaymentTerm("LC", "Letter of Credit", "신용장"));
        Currency newCur = currencyRepository.save(new Currency("JPY", "Japanese Yen", "¥"));

        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Name")
                .clientNameKr("수정이름")
                .countryId(newCountry.getId())
                .clientCity("Tokyo")
                .portId(newPort.getId())
                .clientAddress("456 New St")
                .clientTel("010-9999-9999")
                .clientEmail("updated@test.com")
                .paymentTermId(newPt.getId())
                .currencyId(newCur.getId())
                .clientManager("새담당자")
                .departmentId(2)
                .build();

        mockMvc.perform(put("/api/clients/{id}", saved.getId())
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
        Client updated = clientRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getClientName()).isEqualTo("Updated Name");
        assertThat(updated.getCountry().getId()).isEqualTo(newCountry.getId());
        assertThat(updated.getPort().getId()).isEqualTo(newPort.getId());
        assertThat(updated.getPaymentTerm().getId()).isEqualTo(newPt.getId());
        assertThat(updated.getCurrency().getId()).isEqualTo(newCur.getId());
    }

    @Test
    @DisplayName("통합테스트: 거래처 수정 - 존재하지 않는 ID")
    void update_notFound() throws Exception {
        UpdateClientRequest request = UpdateClientRequest.builder()
                .clientName("Updated Name")
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(put("/api/clients/{id}", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("통합테스트: 거래처 수정 - 존재하지 않는 FK ID")
    void update_nonExistentFkIds() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .departmentId(1).build());

        UpdateClientRequest request = UpdateClientRequest.builder()
                .countryId(9999)
                .build();

        assertThatThrownBy(() ->
                mockMvc.perform(put("/api/clients/{id}", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    // ==================== PATCH /api/clients/{id}/status ====================

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 활성에서 비활성으로")
    void changeStatus_activeToInactive() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.활성).departmentId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("비활성");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("비활성"));

        // DB 검증
        Client updated = clientRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getClientStatus()).isEqualTo(ClientStatus.비활성);
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 비활성에서 활성으로")
    void changeStatus_inactiveToActive() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.비활성).departmentId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("활성");

        mockMvc.perform(patch("/api/clients/{id}/status", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientStatus").value("활성"));

        // DB 검증
        Client updated = clientRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getClientStatus()).isEqualTo(ClientStatus.활성);
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 존재하지 않는 ID")
    void changeStatus_notFound() throws Exception {
        ChangeStatusRequest request = new ChangeStatusRequest("비활성");

        assertThatThrownBy(() ->
                mockMvc.perform(patch("/api/clients/{id}/status", 9999)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("통합테스트: 거래처 상태 변경 - 동일 상태로 변경 시 실패")
    void changeStatus_sameStatus() throws Exception {
        Client saved = clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client").clientNameKr("거래처")
                .clientStatus(ClientStatus.활성).departmentId(1).build());

        ChangeStatusRequest request = new ChangeStatusRequest("활성");

        assertThatThrownBy(() ->
                mockMvc.perform(patch("/api/clients/{id}/status", saved.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        ).isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    // ==================== GET /api/clients/department/{departmentId} ====================

    @Test
    @DisplayName("통합테스트: 부서별 거래처 조회 - 성공")
    void getByDepartment_success() throws Exception {
        clientRepository.save(Client.builder()
                .clientCode("CLI001").clientName("Client A").clientNameKr("거래처A")
                .departmentId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI002").clientName("Client B").clientNameKr("거래처B")
                .departmentId(1).build());
        clientRepository.save(Client.builder()
                .clientCode("CLI003").clientName("Client C").clientNameKr("거래처C")
                .departmentId(2).build());

        mockMvc.perform(get("/api/clients/department/{departmentId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientCode").value("CLI001"))
                .andExpect(jsonPath("$[1].clientCode").value("CLI002"));
    }

    @Test
    @DisplayName("통합테스트: 부서별 거래처 조회 - 빈 결과")
    void getByDepartment_empty() throws Exception {
        mockMvc.perform(get("/api/clients/department/{departmentId}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
