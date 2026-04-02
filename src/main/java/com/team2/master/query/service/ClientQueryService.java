package com.team2.master.query.service;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.ClientQueryMapper;
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
        return clientQueryMapper.findByClientStatus(status.getDbValue());
    }

    public PagedResponse<ClientListResponse> getClients(String clientName, Integer countryId,
                                                        String clientStatus, Integer departmentId,
                                                        int page, int size) {
        int offset = page * size;
        List<ClientListResponse> content = clientQueryMapper.findByCondition(clientName, countryId, clientStatus, departmentId, size, offset);
        long totalElements = clientQueryMapper.countByCondition(clientName, countryId, clientStatus, departmentId);
        return PagedResponse.of(content, totalElements, page, size);
    }
}
