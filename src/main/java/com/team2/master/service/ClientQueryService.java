package com.team2.master.service;

import com.team2.master.dto.ClientResponse;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.mapper.ClientQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientQueryService {

    private final ClientQueryMapper clientQueryMapper;

    public ClientResponse getClient(Integer id) {
        ClientResponse response = clientQueryMapper.findById(id);
        if (response == null) {
            throw new ResourceNotFoundException("거래처를 찾을 수 없습니다.");
        }
        return response;
    }

    public List<ClientResponse> getAllClients() {
        return clientQueryMapper.findAll();
    }

    public List<ClientResponse> getClientsByDepartmentId(Integer departmentId) {
        return clientQueryMapper.findByDepartmentId(departmentId);
    }

    public List<ClientResponse> getClientsByStatus(ClientStatus status) {
        return clientQueryMapper.findByClientStatus(status.name());
    }
}
