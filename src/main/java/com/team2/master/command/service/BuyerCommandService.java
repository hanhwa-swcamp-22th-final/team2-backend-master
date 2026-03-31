package com.team2.master.command.service;

import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.entity.Client;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.repository.BuyerRepository;
import com.team2.master.command.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyerCommandService {

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

    @Transactional
    public Buyer updateBuyer(Integer id, UpdateBuyerRequest request) {
        Buyer buyer = buyerRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("바이어를 찾을 수 없습니다."));
        buyer.updateInfo(
                request.getBuyerName(), request.getBuyerPosition(),
                request.getBuyerEmail(), request.getBuyerTel()
        );
        return buyer;
    }

    @Transactional
    public void deleteBuyer(Integer id) {
        Buyer buyer = buyerRepository.findByIdWithClient(id)
                .orElseThrow(() -> new ResourceNotFoundException("바이어를 찾을 수 없습니다."));
        buyerRepository.delete(buyer);
    }
}
