package com.team2.master.service;

import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.*;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final CountryRepository countryRepository;
    private final PortRepository portRepository;
    private final PaymentTermRepository paymentTermRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional
    public Client createClient(CreateClientRequest request) {
        if (clientRepository.existsByClientCode(request.getClientCode())) {
            throw new IllegalStateException("이미 사용 중인 거래처 코드입니다.");
        }

        Client client = Client.builder()
                .clientCode(request.getClientCode())
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

    public Client getClient(Integer id) {
        return clientRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("거래처를 찾을 수 없습니다."));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAllWithRelations();
    }

    public List<Client> getClientsByDepartmentId(Integer departmentId) {
        return clientRepository.findByDepartmentIdWithRelations(departmentId);
    }

    @Transactional
    public Client updateClient(Integer id, UpdateClientRequest request) {
        Client client = getClient(id);
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
        Client client = getClient(id);
        client.changeStatus(status);
        return client;
    }
}
