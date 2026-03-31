package com.team2.master.service;

import com.team2.master.dto.PortResponse;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.mapper.PortQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortQueryService {

    private final PortQueryMapper portQueryMapper;

    public List<PortResponse> getAll() {
        return portQueryMapper.findAll();
    }

    public PortResponse getById(Integer id) {
        PortResponse response = portQueryMapper.findById(id);
        if (response == null) {
            throw new ResourceNotFoundException("항구를 찾을 수 없습니다. ID: " + id);
        }
        return response;
    }
}
