package com.team2.master.service;

import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.entity.Client;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.BuyerRepository;
import com.team2.master.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public Buyer createBuyer(CreateBuyerRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("거래처를 찾을 수 없습니다."));

        Buyer buyer = Buyer.builder()
                .client(client)
                .buyerName(request.getBuyerName())
                .buyerPosition(request.getBuyerPosition())
                .buyerEmail(request.getBuyerEmail())
                .buyerTel(request.getBuyerTel())
                .build();

        return buyerRepository.save(buyer);
    }

    public Buyer getBuyer(Integer id) {
        return buyerRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("바이어를 찾을 수 없습니다."));
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAllWithClient();
    }

    public List<Buyer> getBuyersByClientId(Integer clientId) {
        return buyerRepository.findByClientIdWithClient(clientId);
    }

    @Transactional
    public Buyer updateBuyer(Integer id, UpdateBuyerRequest request) {
        Buyer buyer = getBuyer(id);
        buyer.updateInfo(
                request.getBuyerName(), request.getBuyerPosition(),
                request.getBuyerEmail(), request.getBuyerTel()
        );
        return buyer;
    }

    @Transactional
    public void deleteBuyer(Integer id) {
        Buyer buyer = getBuyer(id);
        buyerRepository.delete(buyer);
    }
}
