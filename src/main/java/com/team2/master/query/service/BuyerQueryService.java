package com.team2.master.query.service;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.BuyerQueryMapper;
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

    public PagedResponse<BuyerResponse> getAllBuyersPaged(int page, int size) {
        int offset = page * size;
        List<BuyerResponse> content = buyerQueryMapper.findAllPage(size, offset);
        long totalElements = buyerQueryMapper.countAll();
        return PagedResponse.of(content, totalElements, page, size);
    }

    public PagedResponse<BuyerResponse> getBuyersByClientIdPaged(Integer clientId, int page, int size) {
        int offset = page * size;
        List<BuyerResponse> content = buyerQueryMapper.findByClientIdPage(clientId, size, offset);
        long totalElements = buyerQueryMapper.countByClientId(clientId);
        return PagedResponse.of(content, totalElements, page, size);
    }
}
