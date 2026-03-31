package com.team2.master.service;

import com.team2.master.dto.BuyerResponse;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.mapper.BuyerQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuyerQueryService {

    private final BuyerQueryMapper buyerQueryMapper;

    public BuyerResponse getBuyer(Integer id) {
        BuyerResponse response = buyerQueryMapper.findById(id);
        if (response == null) {
            throw new ResourceNotFoundException("바이어를 찾을 수 없습니다.");
        }
        return response;
    }

    public List<BuyerResponse> getAllBuyers() {
        return buyerQueryMapper.findAll();
    }

    public List<BuyerResponse> getBuyersByClientId(Integer clientId) {
        return buyerQueryMapper.findByClientId(clientId);
    }
}
