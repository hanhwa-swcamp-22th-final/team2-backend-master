package com.team2.master.query.service;

import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.infrastructure.client.AuthFeignClient;
import com.team2.master.common.PagedResponse;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.query.mapper.ClientQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientQueryService {

    private final ClientQueryMapper clientQueryMapper;
    private final AuthFeignClient authFeignClient;

    @Value("${internal.api.token:}")
    private String internalApiToken;

    public ClientResponse getClient(Integer id) {
        ClientResponse response = clientQueryMapper.findById(id);
        if (response == null) {
            throw new ResourceNotFoundException("거래처를 찾을 수 없습니다.");
        }
        enrichTeams(List.of(response));
        return response;
    }

    public List<ClientResponse> getAllClients() {
        List<ClientResponse> list = clientQueryMapper.findAll();
        enrichTeams(list);
        return list;
    }

    public PagedResponse<ClientResponse> getAllClientsPaged(int page, int size) {
        int offset = page * size;
        List<ClientResponse> content = clientQueryMapper.findAllPage(size, offset);
        long totalElements = clientQueryMapper.countAll();
        enrichTeams(content);
        return PagedResponse.of(content, totalElements, page, size);
    }

    public List<ClientResponse> getClientsByTeamId(Integer teamId) {
        List<ClientResponse> list = clientQueryMapper.findByTeamId(teamId);
        enrichTeams(list);
        return list;
    }

    public PagedResponse<ClientResponse> getClientsByTeamIdPaged(Integer teamId, int page, int size) {
        int offset = page * size;
        List<ClientResponse> content = clientQueryMapper.findByTeamIdPage(teamId, size, offset);
        long totalElements = clientQueryMapper.countByTeamId(teamId);
        enrichTeams(content);
        return PagedResponse.of(content, totalElements, page, size);
    }

    public List<ClientResponse> getClientsByDepartmentId(Integer departmentId) {
        List<Integer> teamIds = resolveTeamIdsOfDepartment(departmentId);
        if (teamIds.isEmpty()) return List.of();
        List<ClientResponse> list = clientQueryMapper.findByTeamIds(teamIds);
        enrichTeams(list);
        return list;
    }

    public PagedResponse<ClientResponse> getClientsByDepartmentIdPaged(Integer departmentId, int page, int size) {
        List<Integer> teamIds = resolveTeamIdsOfDepartment(departmentId);
        if (teamIds.isEmpty()) {
            return PagedResponse.of(List.of(), 0L, page, size);
        }
        int offset = page * size;
        List<ClientResponse> content = clientQueryMapper.findByTeamIdsPage(teamIds, size, offset);
        long totalElements = clientQueryMapper.countByTeamIds(teamIds);
        enrichTeams(content);
        return PagedResponse.of(content, totalElements, page, size);
    }

    public List<ClientResponse> getClientsByStatus(ClientStatus status) {
        List<ClientResponse> list = clientQueryMapper.findByClientStatus(status.getDbValue());
        enrichTeams(list);
        return list;
    }

    public PagedResponse<ClientListResponse> getClients(String clientName, Integer countryId,
                                                        String clientStatus, Integer teamId, Integer departmentId,
                                                        int page, int size) {
        List<Integer> teamIds = departmentId != null ? resolveTeamIdsOfDepartment(departmentId) : null;
        // departmentId 필터가 있는데 해당 부서 팀이 하나도 없으면 빈 페이지
        if (departmentId != null && (teamIds == null || teamIds.isEmpty())) {
            return PagedResponse.of(List.of(), 0L, page, size);
        }

        int offset = page * size;
        List<ClientListResponse> content = clientQueryMapper.findByCondition(
                clientName, countryId, clientStatus, teamId, teamIds, size, offset);
        long totalElements = clientQueryMapper.countByCondition(
                clientName, countryId, clientStatus, teamId, teamIds);
        enrichListTeams(content);
        return PagedResponse.of(content, totalElements, page, size);
    }

    /** ClientResponse 리스트에 teamName/departmentId/departmentName 주입 (Feign 1회 배치 호출). */
    private void enrichTeams(List<ClientResponse> list) {
        Map<Integer, AuthFeignClient.TeamBrief> map = fetchTeamBriefs(
                list.stream().map(ClientResponse::getTeamId).toList());
        if (map.isEmpty()) return;
        for (ClientResponse r : list) {
            AuthFeignClient.TeamBrief t = map.get(r.getTeamId());
            if (t != null) {
                r.setTeamName(t.teamName());
                r.setDepartmentId(t.departmentId());
                r.setDepartmentName(t.departmentName());
            }
        }
    }

    private void enrichListTeams(List<ClientListResponse> list) {
        Map<Integer, AuthFeignClient.TeamBrief> map = fetchTeamBriefs(
                list.stream().map(ClientListResponse::getTeamId).toList());
        if (map.isEmpty()) return;
        for (ClientListResponse r : list) {
            AuthFeignClient.TeamBrief t = map.get(r.getTeamId());
            if (t != null) {
                r.setTeamName(t.teamName());
                r.setDepartmentId(t.departmentId());
                r.setDepartmentName(t.departmentName());
            }
        }
    }

    private Map<Integer, AuthFeignClient.TeamBrief> fetchTeamBriefs(List<Integer> rawIds) {
        List<Integer> ids = rawIds.stream().filter(Objects::nonNull).distinct().toList();
        if (ids.isEmpty()) return Map.of();
        if (internalApiToken == null || internalApiToken.isBlank()) {
            log.warn("INTERNAL_API_TOKEN 미설정 — team enrich skip");
            return Map.of();
        }
        try {
            List<AuthFeignClient.TeamBrief> briefs = authFeignClient.getTeamsByIds(internalApiToken, ids);
            Map<Integer, AuthFeignClient.TeamBrief> m = new HashMap<>();
            for (AuthFeignClient.TeamBrief b : briefs) m.put(b.teamId(), b);
            return m;
        } catch (Exception e) {
            log.warn("Auth 팀 조회 실패 — enrich skip [reason={}]", e.getMessage());
            return Map.of();
        }
    }

    private List<Integer> resolveTeamIdsOfDepartment(Integer departmentId) {
        if (internalApiToken == null || internalApiToken.isBlank()) {
            log.warn("INTERNAL_API_TOKEN 미설정 — department→teamIds 해소 불가");
            return List.of();
        }
        try {
            return authFeignClient.getTeamsByDepartment(internalApiToken, departmentId).stream()
                    .map(AuthFeignClient.TeamBrief::teamId)
                    .toList();
        } catch (Exception e) {
            log.warn("부서 소속 팀 조회 실패 [departmentId={}, reason={}]", departmentId, e.getMessage());
            return List.of();
        }
    }
}
