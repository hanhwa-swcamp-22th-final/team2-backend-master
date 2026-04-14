package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.*;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientCommandService {

    private final ClientRepository clientRepository;
    private final CountryRepository countryRepository;
    private final PortRepository portRepository;
    private final PaymentTermRepository paymentTermRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional
    public Client createClient(CreateClientRequest request) {
        // code 미제공 시 서버에서 자동 생성 (동시성 이슈 방지)
        String clientCode = (request.getClientCode() == null || request.getClientCode().isBlank())
                ? generateNextClientCode()
                : request.getClientCode();

        if (clientRepository.existsByClientCode(clientCode)) {
            throw new IllegalStateException("이미 사용 중인 거래처 코드입니다.");
        }

        Client client = Client.builder()
                .clientCode(clientCode)
                .clientName(request.getClientName())
                .clientNameKr(request.getClientNameKr())
                .clientCity(request.getClientCity())
                .clientAddress(request.getClientAddress())
                .clientTel(request.getClientTel())
                .clientEmail(request.getClientEmail())
                .clientManager(request.getClientManager())
                .departmentId(request.getDepartmentId())
                .clientStatus(ClientStatus.ACTIVE)
                .clientRegDate(request.getClientRegDate())
                .build();

        if (request.getCountryId() != null) {
            Country country = countryRepository.findById(request.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다."));
            client.assignCountry(country);
        }

        if (request.getPortId() != null) {
            Port port = portRepository.findById(request.getPortId())
                    .orElseThrow(() -> new ResourceNotFoundException("항구를 찾을 수 없습니다."));
            client.assignPort(port);
        }

        if (request.getPaymentTermId() != null) {
            PaymentTerm paymentTerm = paymentTermRepository.findById(request.getPaymentTermId())
                    .orElseThrow(() -> new ResourceNotFoundException("결제조건을 찾을 수 없습니다."));
            client.assignPaymentTerm(paymentTerm);
        }

        if (request.getCurrencyId() != null) {
            Currency currency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("통화를 찾을 수 없습니다."));
            client.assignCurrency(currency);
        }

        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Integer id, UpdateClientRequest request) {
        Client client = clientRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("거래처를 찾을 수 없습니다."));
        client.updateInfo(
                request.getClientName(), request.getClientNameKr(),
                request.getClientCity(), request.getClientAddress(),
                request.getClientTel(), request.getClientEmail(),
                request.getClientManager(), request.getDepartmentId()
        );

        if (request.getCountryId() != null) {
            Country country = countryRepository.findById(request.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다."));
            client.assignCountry(country);
        }

        if (request.getPortId() != null) {
            Port port = portRepository.findById(request.getPortId())
                    .orElseThrow(() -> new ResourceNotFoundException("항구를 찾을 수 없습니다."));
            client.assignPort(port);
        }

        if (request.getPaymentTermId() != null) {
            PaymentTerm paymentTerm = paymentTermRepository.findById(request.getPaymentTermId())
                    .orElseThrow(() -> new ResourceNotFoundException("결제조건을 찾을 수 없습니다."));
            client.assignPaymentTerm(paymentTerm);
        }

        if (request.getCurrencyId() != null) {
            Currency currency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("통화를 찾을 수 없습니다."));
            client.assignCurrency(currency);
        }

        return client;
    }

    @Transactional
    public Client changeStatus(Integer id, ClientStatus status) {
        Client client = clientRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("거래처를 찾을 수 없습니다."));
        client.changeStatus(status);
        return client;
    }

    private String generateNextClientCode() {
        Integer max = clientRepository.findMaxClientCodeNumber();
        int next = (max == null ? 0 : max) + 1;
        return String.format("CLI%03d", next);
    }
}
